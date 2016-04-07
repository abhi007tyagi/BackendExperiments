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

    private static final Logger logger = Logger.getLogger(CronServlet.class.getName());
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            logger.warning("Cron Job executing");
            MyEndpoint endpoint = new MyEndpoint();
            endpoint.cleanInvitation(null, null);
        }
        catch (Exception ex) {
            logger.severe("CronJob Error : " + ex.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
