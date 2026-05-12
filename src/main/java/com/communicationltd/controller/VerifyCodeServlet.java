package com.communicationltd.controller;

import com.communicationltd.security.InputSanitizer;
import com.communicationltd.security.SecurityUtil;
import com.communicationltd.security.TokenGenerator;
import com.communicationltd.util.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/verify-code")
public class VerifyCodeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String email = InputSanitizer.normalize(request.getParameter("email")).toLowerCase();
        String code = InputSanitizer.normalize(request.getParameter("code"));

        if (!InputSanitizer.isValidEmail(email) || !code.matches("^[a-f0-9]{40}$")) {
            showVerifyCodeError(request, response, "Invalid code");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = """
                    SELECT id, token_hash
                    FROM password_reset_tokens
                    WHERE email = ?
                      AND used = 0
                      AND created_at >= datetime('now', '-15 minutes')
                    ORDER BY id DESC
                    LIMIT 1
                    """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            String providedTokenHash = TokenGenerator.hashToken(email, code);
            if (!rs.next() || !SecurityUtil.constantTimeEquals(rs.getString("token_hash"), providedTokenHash)) {
                showVerifyCodeError(request, response, "Invalid code");
                return;
            }

            PreparedStatement markUsed = conn.prepareStatement(
                    "UPDATE password_reset_tokens SET used = 1 WHERE id = ?"
            );
            markUsed.setInt(1, rs.getInt("id"));
            markUsed.executeUpdate();

            HttpSession session = request.getSession();
            session.setAttribute("resetEmail", email);

            response.sendRedirect("new-password.jsp");

        } catch (Exception e) {
            showVerifyCodeError(request, response, "Could not verify code, please try again");
        }
    }

    private void showVerifyCodeError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {

        request.setAttribute("verifyCodeError", message);
        request.getRequestDispatcher("/verify-code.jsp").forward(request, response);
    }
}
