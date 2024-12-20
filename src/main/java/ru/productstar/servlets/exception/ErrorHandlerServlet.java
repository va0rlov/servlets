package ru.productstar.servlets.exception;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;

import java.io.IOException;

public class ErrorHandlerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleError(req, resp);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object statusCode = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (statusCode != null && ((Integer) statusCode == 404)) {
            resp.getWriter().write("Error (404) — page not found");
        } else if (statusCode != null && ((Integer) statusCode == 500)) {
            Throwable throwable = (Throwable) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
            resp.getWriter().write("Error (500) — " + throwable.getClass().getSimpleName() + ": " +
                    throwable.getMessage());
        } else {
            resp.getWriter().write("An unknown error occurred.");
        }
    }
}
