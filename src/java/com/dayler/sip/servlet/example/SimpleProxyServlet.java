/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

// http://sip.downv.com/Linux-software-download/sip-protocol-traffic-generator

package com.dayler.sip.servlet.example;

import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipServlet;
import javax.servlet.ServletContext;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletConfig;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TimerService;
import javax.ws.rs.core.Context;
import org.apache.log4j.Logger;

/**
 *
 * @author asalazar
 */
@javax.servlet.sip.annotation.SipServlet
public class SimpleProxyServlet extends SipServlet implements TimerListener{

    private static Logger logger = Logger.getLogger(SimpleProxyServlet.class);

    private static long DEFAULT_TIMEOUT_MS = 20000L;

    private static final long serialVersionUID = 3978425801979081269L;

    private ServletTimer sTimer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); 

        System.out.println("Initializing the servlet: SimpleProxyServlet:\n");
        logger.debug("Initializing the servlet: SimpleProxyServlet:\n");
    }

    /*
     * Demonstrates extension with a new "REPUBLISH" method
     */
    @Override
    protected void doRequest(SipServletRequest request) throws ServletException, IOException {
        System.out.println("SimpleProxyServlet: Get request:\n" + request);
        logger.debug("SimpleProxyServlet: Get request:\n" + request);
        super.doRequest(request);
    }

    @Override
    protected void doResponse(SipServletResponse response) throws ServletException, IOException {
        System.out.println("SimpleProxyServlet: Got response:\n" + response);
        logger.debug("SimpleProxyServlet: Got response:\n" + response);
        super.doResponse(response);
    }

    @Override
    protected void doInvite(SipServletRequest request) throws ServletException, IOException {
        if (request.isInitial()) {
            Proxy proxy = request.getProxy();
            proxy.setRecordRoute(true);
            proxy.setSupervised(true);
            proxy.proxyTo(request.getRequestURI());

            logger.debug("URI: " + request.getRequestURI());
            System.out.println("URI: " + request.getRequestURI());
        }

        logger.debug("SimpleProxyServlet: Got request:\n" + request);
        System.out.println("SimpleProxyServlet: Got request:\n" + request);

        logger.debug("Creating Servlet Timer");
        TimerService tService = getTimerService();
        // servletTimer = timer.createTimer(request.getApplicationSession(), byeDelay, false, request.getSession().getId());

//        sTimer = tService.createTimer(request.getApplicationSession(), DEFAULT_TIMEOUT_MS, false, request.getSession().getId());

        sTimer = tService.createTimer(request.getApplicationSession(),
                DEFAULT_TIMEOUT_MS, DEFAULT_TIMEOUT_MS,
                false, false,
                request.getSession().getId());
        SipSession session = request.getSession();
        logger.info("Servlet Timer was created" + " Session ID: " + session.getId() + " CallID: " + session.getCallId());
    }

    @Override
    protected void doBye(SipServletRequest request) throws ServletException, IOException {
        logger.debug("SimpleProxyServlet: Got BYE request:\n" + request);
        System.out.println("SimpleProxyServlet: Got BYE request:\n" + request);

        SipSession session = request.getSession();
        logger.info("doBye#Stop timmer" + " Session ID: " + session.getId() + " CallID: " + session.getCallId());
        sTimer.cancel();

        super.doBye(request);
    }

    @Override
    protected void doCancel(SipServletRequest req) throws ServletException, IOException {
        logger.debug("SimpleProxyServlet: doCancel");

        SipSession session = req.getSession();
        logger.info("doCancel#Stop timmer" + " Session ID: " + session.getId() + " CallID: " + session.getCallId());
        sTimer.cancel();

        super.doCancel(req);
    }

    @Override
    protected void doErrorResponse(SipServletResponse resp) throws ServletException, IOException {
        logger.debug("SimpleProxyServlet: doErrorResponse");

        logger.debug("doErrorResponse#Stop timmer");
        sTimer.cancel();

        super.doErrorResponse(resp);
    }

    public void timeout(ServletTimer sTimer) {
        //  SipSession sipSession = servletTimer.getApplicationSession().getSipSession((String)servletTimer.getInfo());
        SipSession session = sTimer.getApplicationSession().getSipSession((String) sTimer.getInfo());

        logger.info("Session ID: " + session.getId() + " CallID: " + session.getCallId() + " Timmer was executed " + new Date().toString());
    }

    private TimerService getTimerService() {
        return (TimerService) getServletContext().getAttribute(SipServlet.TIMER_SERVICE);
    }
}
