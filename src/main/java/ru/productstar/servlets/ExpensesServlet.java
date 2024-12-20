package ru.productstar.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.productstar.servlets.model.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExpensesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var context = req.getServletContext();

        var session = req.getSession(false);
        if (session == null) {
            resp.getWriter().println("Not authorized");
            return;
        }

        int freeMoney = (int)context.getAttribute("freeMoney");
        List<Transaction> transactions = (List<Transaction>)context.getAttribute("transactions");
        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        for (var k : req.getParameterMap().keySet()) {
            int value = Integer.parseInt(req.getParameter(k));
            freeMoney -= value;
            transactions.add(new Transaction("expense", k, value));
        }

        context.setAttribute("transactions", transactions);
        context.setAttribute("freeMoney", freeMoney);
        resp.sendRedirect(req.getContextPath() + "/summary");
    }

}
