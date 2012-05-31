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

import org.alfresco.repo.lotus.ws.ClbError;
import org.alfresco.repo.lotus.ws.ClbErrorType;
import org.alfresco.repo.lotus.ws.ClbResponse;
import org.alfresco.repo.node.integrity.IntegrityException;
import org.alfresco.repo.security.permissions.AccessDeniedException;
import org.alfresco.service.cmr.model.FileExistsException;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.InvalidNodeRefException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class SOAPExceptionInterceptor implements MethodInterceptor
{

    public Object invoke(MethodInvocation invocation) throws Throwable
    {

        ClbResponse result = (ClbResponse) invocation.getMethod().getReturnType().getDeclaredConstructor().newInstance();
        try
        {
            result = (ClbResponse) invocation.proceed();
        }
        catch (FileNotFoundException e)
        {
            result.setError(createError(ClbErrorType.ITEM_NOT_FOUND, e.getMessage()));
        }
        catch (InvalidNodeRefException e)
        {
            result.setError(createError(ClbErrorType.ITEM_NOT_FOUND, e.getMessage()));
        }
        catch (FileExistsException e)
        {
            result.setError(createError(ClbErrorType.ITEM_EXISTS, e.getMessage()));
        }
        catch (IntegrityException e)
        {
            result.setError(createError(ClbErrorType.CONSTRAINT_VIOLATION, e.getMessage()));
        }
        catch (AccessDeniedException e)
        {
            result.setError(createError(ClbErrorType.ACCESS_DENIED, e.getMessage()));
        }
        catch (Exception e)
        {
            result.setError(createError(ClbErrorType.GENERAL_INFORMATION, e.getMessage()));
        }
        return result;
    }

    private ClbError createError(ClbErrorType errorType, String message)
    {
        ClbError error = new ClbError();
        error.setMessage(message);
        error.setType(errorType);

        return error;
    }
}
