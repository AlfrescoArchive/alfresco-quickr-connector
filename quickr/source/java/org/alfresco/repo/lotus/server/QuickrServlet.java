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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.bus.spring.BusApplicationContext;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author EugeneZh
 */
public class QuickrServlet extends CXFServlet
{
    private static final long serialVersionUID = 1L;

    private boolean inRefresh;

    private ServletContext sc;

    @Override
    public void onApplicationEvent(ApplicationEvent event)
    {
        if (!inRefresh && event instanceof ContextRefreshedEvent)
        {
            // need to re-do the bus/controller stuff
            try
            {
                inRefresh = true;
                updateContext(((ContextRefreshedEvent) event).getApplicationContext());
            }
            finally
            {
                inRefresh = false;
            }
        }
    }

    private void updateContext(ApplicationContext ctx)
    {
        // This constructor works whether there is a context or not
        // If the ctx is null, we just start up the default bus
        if (ctx == null)
        {
            bus = new SpringBusFactory().createBus();
            ctx = bus.getExtension(BusApplicationContext.class);
        }
        else
        {
            bus = new SpringBusFactory(ctx).createBus();
        }

        replaceDestinationFactory();
        ServletConfig conf = new QuickrServletConfig();
        sc = conf.getServletContext();
        // Set up the ServletController
        controller = createServletController(conf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        if (request.getMethod().equals("GET") && (request.getPathInfo().endsWith("/services/LibraryService") || request.getPathInfo().endsWith("/services/ContentService")))
        {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        super.doGet(request, response);
    }

    @Override
    public ServletContext getServletContext()
    {
        return sc;
    }
}
