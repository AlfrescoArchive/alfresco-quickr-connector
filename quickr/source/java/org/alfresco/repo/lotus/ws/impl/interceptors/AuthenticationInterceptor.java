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

package org.alfresco.repo.lotus.ws.impl.interceptors;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.alfresco.repo.lotus.ws.impl.auth.Authenticator;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.ws.addressing.EndpointReferenceType;

/**
 * @author Eugene Zheleznyakov
 */
public class AuthenticationInterceptor extends SoapHeaderInterceptor
{
    public final static String HEADER_WWW_AUTHENTICATE = "WWW-Authenticate";

    private Authenticator authenticator;

    /**
     * Sets authenticator
     * 
     * @param authenticator the authenticator to set
     */   
    public void setAuthenticator(Authenticator authenticator)
    {
        this.authenticator = authenticator;
    }

    @Override
    public void handleMessage(final Message message) throws Fault
    {
        if (!authenticator.authenticate(message))
        {
            sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
        }
    }

    private void sendErrorResponse(Message message, int responseCode)
    {
        Message outMessage = getOutMessage(message);
        outMessage.put(Message.RESPONSE_CODE, responseCode);

        @SuppressWarnings("unchecked")
        Map<String, List<String>> responseHeaders = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
        if (responseHeaders != null)
        {
            responseHeaders.put(HEADER_WWW_AUTHENTICATE, Arrays.asList(new String[] { "Basic realm=\"Alfresco Server\"" }));
        }
        message.getInterceptorChain().abort();
        try
        {
            getConduit(message).prepare(outMessage);
            close(outMessage);
        }
        catch (IOException e)
        {
            // TODO
        }
    }

    private Message getOutMessage(Message inMessage)
    {
        Exchange exchange = inMessage.getExchange();
        Message outMessage = exchange.getOutMessage();
        if (outMessage == null)
        {
            Endpoint endpoint = exchange.get(Endpoint.class);
            outMessage = endpoint.getBinding().createMessage();
            exchange.setOutMessage(outMessage);
        }
        outMessage.putAll(inMessage);
        return outMessage;
    }

    private Conduit getConduit(Message inMessage) throws IOException
    {
        Exchange exchange = inMessage.getExchange();
        EndpointReferenceType target = exchange.get(EndpointReferenceType.class);
        Conduit conduit = exchange.getDestination().getBackChannel(inMessage, null, target);
        exchange.setConduit(conduit);
        return conduit;
    }

    private void close(Message outMessage) throws IOException
    {
        OutputStream os = outMessage.getContent(OutputStream.class);
        os.flush();
        os.close();
    }
}
