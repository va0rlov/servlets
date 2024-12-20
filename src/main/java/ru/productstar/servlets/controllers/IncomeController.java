package ru.productstar.servlets.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.productstar.servlets.model.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/incomes/add")
public class IncomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var context = req.getServletContext();

        var session = req.getSession(false);
        if (session == null) {
            resp.getWriter().println("Not authorized");
            return;
        }

        String name = req.getParameter("name");
        String amountStr = req.getParameter("amount");

        if (name == null || name.isBlank() || amountStr == null || amountStr.isBlank()) {
            resp.getWriter().println("Invalid parameters provided.");
            return;
        }

        try {
            int amount = Integer.parseInt(amountStr);

            int freeMoney = (int) context.getAttribute("freeMoney");
            List<Transaction> transactions = (List<Transaction>) context.getAttribute("transactions");
            if (transactions == null) {
                transactions = new ArrayList<>();
            }

            freeMoney += amount;
            transactions.add(new Transaction("income", name, amount));

            context.setAttribute("transactions", transactions);
            context.setAttribute("freeMoney", freeMoney);
            resp.sendRedirect(req.getContextPath() + "/summary");
        } catch (NumberFormatException e) {
            resp.getWriter().println("Invalid amount parameter provided.");
        }
    }
}
