/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

// http://sip.downv.com/Linux-software-download/sip-protocol-traffic-generator

package com.dayler.sip.servlet.example;

import javax.servlet.sip.SipServlet;
import javax.servlet.ServletContext;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.SipServletResponse;

/**
 *
 * @author asalazar
 */
@javax.servlet.sip.annotation.SipServlet
public class SimpleProxyServlet extends SipServlet {

    private static final long serialVersionUID = 3978425801979081269L;

    //Reference to context - The ctx Map is used as a central storage for this app
    ServletContext context = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = config.getServletContext();

        System.out.println("Initializing the servlet: SimpleProxyServlet:\n");
    }

    /*
     * Demonstrates extension with a new "REPUBLISH" method
     */
    @Override
    protected void doRequest(SipServletRequest request) throws ServletException, IOException {
        System.out.println("SimpleProxyServlet: Get request:\n" + request);
        super.doRequest(request);
    }

    @Override
    protected void doResponse(SipServletResponse response) throws ServletException, IOException {
        System.out.println("SimpleProxyServlet: Got response:\n" + response);
        super.doResponse(response);
    }

    @Override
    protected void doInvite(SipServletRequest request) throws ServletException, IOException {
        if (request.isInitial()) {
            Proxy proxy = request.getProxy();
            proxy.setRecordRoute(true);
            proxy.setSupervised(true);
            proxy.proxyTo(request.getRequestURI());

            System.out.println("URI: " + request.getRequestURI());
        }

        System.out.println("SimpleProxyServlet: Got request:\n" + request);
    }

    @Override
    protected void doBye(SipServletRequest request) throws ServletException, IOException {
        System.out.println("SimpleProxyServlet: Got BYE request:\n" + request);
        super.doBye(request);
    }
}
