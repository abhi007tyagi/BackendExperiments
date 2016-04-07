package com.tyagiabhinav.backend;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by abhinavtyagi on 06/04/16.
 */
public class CronServlet extends HttpServlet {

    private static final Logger _logger = Logger.getLogger(CronServlet.class.getName());
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            _logger.warning("Cron Job has been executed");
            System.out.print("Chal gya !!");
            MyEndpoint endpoint = new MyEndpoint();
            endpoint.cleanInvitation(null, null);
            resp.getWriter().write("Hello Backend!!");
        }
        catch (Exception ex) {
//Log any exceptions in your Cron Job
            _logger.severe("CronJob Error : " + ex.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
