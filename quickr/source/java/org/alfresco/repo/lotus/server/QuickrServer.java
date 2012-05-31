/*
 * Copyright 2010-2012 Alfresco Software Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file is part of an unsupported extension to Alfresco.
 *
 * [BRIEF DESCRIPTION OF FILE CONTENTS]
 */

package org.alfresco.repo.lotus.server;

import javax.servlet.http.HttpServlet;

import org.alfresco.error.AlfrescoRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.context.ApplicationEvent;
import org.springframework.extensions.surf.util.AbstractLifecycleBean;

/**
 * Embedded HTTP server that process all Quickr requests.
 * 
 * @author EugeneZh
 */
public class QuickrServer extends AbstractLifecycleBean
{
    private static Log logger = LogFactory.getLog(QuickrServer.class);

    private Server server;
    private Connector connector;
    private HttpServlet servlet;

    /**
     * Set the HTTP connector
     * 
     * @param connector HTTP Connector
     */
    public void setConnector(Connector connector)
    {
        this.connector = connector;
    }

    /**
     * Set the main QuickrServlet. All the requests will be processed by it.
     * 
     * @param servlet HTTP Servlet
     */
    public void setServlet(HttpServlet servlet)
    {
        this.servlet = servlet;
    }

    /**
     * Method checks that all mandatory fiedls are set.
     * 
     * @throws RuntimeException Exception is thrown if at least one mandatory field isn't set.
     */
    private void check()
    {
        if (servlet == null)
        {
            throw new AlfrescoRuntimeException("Error start QuickrServer, cause: Property 'servlet' not set");
        }

        if (connector == null)
        {
            throw new AlfrescoRuntimeException("Error start QuickrServer, cause: Property 'connector' not set");
        }
    }

    /**
     * Method starts the server.
     * 
     * @see org.alfresco.util.AbstractLifecycleBean#onBootstrap(org.springframework.context.ApplicationEvent)
     */
    @Override
    protected void onBootstrap(ApplicationEvent event)
    {
        check();

        server = new Server();
        server.setStopAtShutdown(true);
        server.setConnectors(new Connector[] { connector });

        Context context = new Context(server, "/", Context.SESSIONS);
        context.addServlet(new ServletHolder(servlet), "/dm/*");

        try
        {
            server.start();

            if (logger.isInfoEnabled())
                logger.info("Quickr server started successfully on port: " + this.connector.getLocalPort());
        }
        catch (Exception e)
        {
            throw new AlfrescoRuntimeException("Error start QuickrServer, cause: ", e);
        }
    }

    /**
     * Method stops the server.
     * 
     * @see org.alfresco.util.AbstractLifecycleBean#onBootstrap(org.springframework.context.ApplicationEvent)
     */
    @Override
    protected void onShutdown(ApplicationEvent event)
    {
        try
        {
            server.stop();
        }
        catch (Exception e)
        {
            throw new AlfrescoRuntimeException("Error stop QuickrServer, cause: ", e);
        }
    }
}
