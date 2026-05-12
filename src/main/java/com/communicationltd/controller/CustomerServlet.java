package com.communicationltd.controller;

import com.communicationltd.dao.CustomerDao;
import com.communicationltd.security.InputSanitizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/customer-details")
public class CustomerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("email") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String email = (String) session.getAttribute("email");

        String customerName = InputSanitizer.sanitize(request.getParameter("customerName"));
        String phone = InputSanitizer.normalize(request.getParameter("phone"));
        String address = InputSanitizer.sanitize(request.getParameter("address"));

        if (!InputSanitizer.isReasonableText(customerName, 80)
                || !phone.matches("^[0-9+\\- ]{7,20}$")
                || !InputSanitizer.isReasonableText(address, 160)) {
            request.setAttribute("customerError", "Customer details are invalid");
            request.getRequestDispatcher("/customer-details.jsp").forward(request, response);
            return;
        }

        int packageId;
        int sectorId;
        try {
            packageId = Integer.parseInt(request.getParameter("packageId"));
            sectorId = Integer.parseInt(request.getParameter("sectorId"));
        } catch (NumberFormatException e) {
            request.setAttribute("customerError", "Selected package or sector is invalid");
            request.getRequestDispatcher("/customer-details.jsp").forward(request, response);
            return;
        }

        if (packageId < 1 || packageId > 3 || sectorId < 1 || sectorId > 4) {
            request.setAttribute("customerError", "Selected package or sector is invalid");
            request.getRequestDispatcher("/customer-details.jsp").forward(request, response);
            return;
        }

        CustomerDao.addCustomer(email, customerName, phone, address, packageId, sectorId);

        session.setAttribute("customerName", customerName);
        response.sendRedirect("dashboard.jsp");
    }
}
