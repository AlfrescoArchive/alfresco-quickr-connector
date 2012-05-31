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

package org.alfresco.repo.lotus.ws.impl;

import java.util.List;

//import org.alfresco.i18n.I18NUtil;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.lotus.ws.ContentService;
import org.alfresco.repo.lotus.ws.LoginException_Exception;
import org.alfresco.repo.lotus.ws.Property;
import org.alfresco.repo.lotus.ws.ServerInfo;
import org.alfresco.repo.lotus.ws.ServerInfoResponse;
import org.alfresco.repo.lotus.ws.ServiceException_Exception;
import org.alfresco.repo.lotus.ws.UserInfo;
import org.alfresco.repo.lotus.ws.UserResponse;
import org.alfresco.repo.lotus.ws.UsersResponse;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.PersonService;
import org.springframework.extensions.surf.util.I18NUtil;

/**
 * @author PavelYur
 */
public class AlfrescoContentServiceImpl implements ContentService
{
    private static final String ATOM_URI = "/dm/atom";

    private String serverVersion;
    
    private String supportedVersions;

    private AuthenticationService authenticationService;

    private PersonService personService;

    private NodeService nodeService;

    public void setNodeService(NodeService nodeService)
    {
        this.nodeService = nodeService;
    }

    public void setPersonService(PersonService personService)
    {
        this.personService = personService;
    }

    public void setServerVersion(String serverVersion)
    {
        this.serverVersion = serverVersion;
    }

    public void setSupportedVersions(String supportedVersions)
    {
        this.supportedVersions = supportedVersions;
    }

    public void setAuthenticationService(AuthenticationService authenticationService)
    {
        this.authenticationService = authenticationService;
    }

    /**
     * Retrieves the ServerInfo object
     * 
     * @return ServerInfoResponse
     * @throws LoginException_Exception, ServiceException_Exception
     */
    public ServerInfoResponse getServerInfo() throws LoginException_Exception, ServiceException_Exception
    {
        ServerInfoResponse result = new ServerInfoResponse();

        ServerInfo serverInfo = new ServerInfo();

        // set server locale
        serverInfo.setLocale(I18NUtil.getLocale().toString());

        // set server security on/off
        serverInfo.setSecurityEnabled(true);

        // set server version
        serverInfo.setVersion(serverVersion);

        // adding server properties
        Property appUrlProperty = new Property();
        appUrlProperty.setKey("app.url");
        appUrlProperty.setValue(ATOM_URI);
        serverInfo.getServerProperties().add(appUrlProperty);
        
        Property supportedVersionsProperty = new Property();
        supportedVersionsProperty.setKey("supported.versions");
        supportedVersionsProperty.setValue(supportedVersions);
        serverInfo.getServerProperties().add(supportedVersionsProperty);

        result.setServerInfo(serverInfo);

        return result;
    }

    /**
     * The getUser operation retrieves the requested UserInfo object and returns it in the UserResponse.
     * 
     * @return UserResponse
     * @throws LoginException_Exception, ServiceException_Exception
     */
    public UserResponse getUser() throws LoginException_Exception, ServiceException_Exception
    {
        UserResponse userResponse = new UserResponse();
        UserInfo userInfo = new UserInfo();
        String user = authenticationService.getCurrentUserName();
        NodeRef personNodeRef = personService.getPerson(user);
        String userFirstName = (String) nodeService.getProperty(personNodeRef, ContentModel.PROP_FIRSTNAME);
        String userLastName = (String) nodeService.getProperty(personNodeRef, ContentModel.PROP_LASTNAME);

        userInfo.setCommonName(userFirstName + " " + userLastName);
        userInfo.setDn("uid=" + user + ",o=Default Organization");
        userInfo.setEmailAddress((String) nodeService.getProperty(personNodeRef, ContentModel.PROP_EMAIL));
        userResponse.setUser(userInfo);

        return userResponse;
    }

    public UsersResponse getUsers(List<String> ids) throws LoginException_Exception, ServiceException_Exception
    {
        // TODO Auto-generated method stub
        return null;
    }
}
