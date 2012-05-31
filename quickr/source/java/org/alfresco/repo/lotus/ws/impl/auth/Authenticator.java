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
package org.alfresco.repo.lotus.ws.impl.auth;

import org.apache.cxf.message.Message;

/**
 * Web authentication fundamental API
 * 
 * @author PavelYur
 *
 */
public interface Authenticator
{

    /**
     * Authenticate user based on information retrieved from client
     * 
     * @param message the incoming message
     * 
     * @return <code>true</code> if user successfully authenticated, otherwise <code>false</code>
     */
    public boolean authenticate(Message message);
    
    /**
     * Indicate whether authenticator is active or not
     * 
     * @return <code>true</code> if authenticator is active, otherwise <code>false</code>
     */
    public boolean isActive();
}
