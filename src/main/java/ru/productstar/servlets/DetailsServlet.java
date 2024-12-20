package ru.productstar.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.productstar.servlets.model.Transaction;

import java.io.IOException;
import java.util.List;

public class DetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var context = req.getServletContext();

        var session = req.getSession(false);
        if (session == null) {
            resp.getWriter().println("Not authorized");
            return;
        }

        resp.getWriter().println("Transactions: ");
        for (Transaction t : (List<Transaction>)context.getAttribute("transactions")) {
            resp.getWriter().println(String.format("%s: %s (%d)", t.getType(), t.getName(), t.getAmount()));
        }
        resp.getWriter().println("\n");
    }
}
