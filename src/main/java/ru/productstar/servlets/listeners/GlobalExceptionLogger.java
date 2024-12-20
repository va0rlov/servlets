package ru.productstar.servlets.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GlobalExceptionLogger implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        Thread.UncaughtExceptionHandler threadUncaughtExceptionHandler = (t, e) ->
                logAndHandleException(ctx, t, e);
        Thread.setDefaultUncaughtExceptionHandler(threadUncaughtExceptionHandler);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}


    private void logAndHandleException(ServletContext ctx, Thread t, Throwable e) {
        ctx.log("GlobalExceptionLogger caught exception in thread " + t.getName() + ": " + e.toString());
        try {
            HttpServletRequest req = (HttpServletRequest) ctx.getAttribute("jakarta.servlet.http.HttpRequest");
            HttpServletResponse res = (HttpServletResponse) ctx.getAttribute("jakarta.servlet.http.HttpResponse");
            if (res == null || req == null) {
                return;
            }
            int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            res.reset();
            res.setContentType("text/html;charset=UTF-8");
            res.setStatus(statusCode);
            res.getOutputStream().print("<h3>Error (" + statusCode + ") — " + e.getClass().getSimpleName() + ": " +
                    e.getMessage());
            res.flushBuffer();
        } catch (IOException ex) {
            ctx.log("GlobalExceptionLogger failed to handle exception: " + ex.toString());
        }
    }
}
