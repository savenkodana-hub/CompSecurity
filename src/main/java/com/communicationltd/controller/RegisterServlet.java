package com.communicationltd.controller;

import com.communicationltd.dao.UserDao;
import com.communicationltd.security.InputSanitizer;
import com.communicationltd.security.PasswordHasher;
import com.communicationltd.security.PasswordValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = InputSanitizer.normalize(request.getParameter("username"));
        String email = InputSanitizer.normalize(request.getParameter("email")).toLowerCase();
        String password = request.getParameter("password");

        if (!InputSanitizer.isValidUsername(username)) {
            showRegisterError(request, response, "Username must be 3-40 characters and contain only letters, numbers, dot, dash or underscore");
            return;
        }

        if (!InputSanitizer.isValidEmail(email)) {
            showRegisterError(request, response, "Email address is invalid");
            return;
        }

        String validation = PasswordValidator.validate(password);
        if (validation != null) {
            showRegisterError(request, response, validation);
            return;
        }

        String salt = PasswordHasher.generateSalt();
        String hashedPassword = PasswordHasher.hash(password, salt);

        boolean registered = UserDao.registerUser(username, email, hashedPassword, salt);

        if (!registered) {
            showRegisterError(request, response, "Username or email already exists");
            return;
        }

        response.sendRedirect("login.jsp");
    }

    private void showRegisterError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {

        request.setAttribute("registerError", message);
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
