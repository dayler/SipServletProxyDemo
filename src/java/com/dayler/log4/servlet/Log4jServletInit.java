/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dayler.log4.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.extras.DOMConfigurator;

/**
 * This servlet is responsible to load the initial configuration for Log4j.xml.
 * It must to set 'load-on-startup=true'. The relative path is configuring as
 * init parameter.
 *
 * @author asalazar
 */
public class Log4jServletInit extends HttpServlet {

    private static Logger helperLogger = Logger.getLogger("Log4jServletInit");

    /**
     * Name of the init parameter to contains the name of the configuration
     * file. This file must to be XML.
     */
    public static final String LOG4J_INIT_FILE = "log4j-init-xml-file";

    /**
     * Relative root path.
     */
    public static final String ROOT_PATH = "/";

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        try {
            String relativePath = getServletContext().getRealPath(ROOT_PATH);
            String log4jFile = getInitParameter(LOG4J_INIT_FILE);

            // if the log4j-init-file is not set, then no point in trying
            if (log4jFile != null && !log4jFile.isEmpty()) {
                // Load property configuration.
                DOMConfigurator.configure(String.format("%s%s", relativePath, log4jFile));
            }
        } catch (Throwable ex) {
            helperLogger.log(Level.SEVERE, "Critical LOG4J cannot be initialized.", ex);
            System.out.println("Critical LOG4J cannot be initialized.");
        }
    }

    /** 
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Initialize LOG4J properties";
    }

}
