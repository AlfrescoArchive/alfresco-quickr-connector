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

import javax.ws.rs.core.Response;

import org.alfresco.repo.lotus.rs.error.QuickrError;
import org.alfresco.repo.node.integrity.IntegrityException;
import org.alfresco.repo.security.permissions.AccessDeniedException;
import org.alfresco.service.cmr.model.FileExistsException;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.InvalidNodeRefException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.abdera.Abdera;
import org.apache.abdera.protocol.error.Error;

public class AtomExceptionInterceptor implements MethodInterceptor
{
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        Response result = Response.ok().build();
        try
        {
            result = (Response) invocation.proceed();
        }
        catch (FileNotFoundException e)
        {
            result = createFaultResponse(QuickrError.ITEM_NOT_FOUND, e.getMessage());
        }
        catch (InvalidNodeRefException e)
        {
            result = createFaultResponse(QuickrError.ITEM_NOT_FOUND, e.getMessage());
        }
        catch (FileExistsException e)
        {

            result = createFaultResponse(QuickrError.ITEM_EXISTS, e.getMessage());
        }
        catch (IntegrityException e)
        {
            result = createFaultResponse(QuickrError.COSNTRAIN_TVIOLATION, e.getMessage());
        }
        catch (AccessDeniedException e)
        {
            result = createFaultResponse(QuickrError.ACCESS_DENIED, e.getMessage());
        }        
        catch (Exception e)
        {
            result = createFaultResponse(QuickrError.UNKNOWN, e.getMessage());
        }
        return result;
    }

    private Response createFaultResponse(QuickrError error, String message)
    {
        return Response.status(error.getResponseStatus()).entity(Error.create(new Abdera(), error.getErrorCode(), message)).build();
    }

}
