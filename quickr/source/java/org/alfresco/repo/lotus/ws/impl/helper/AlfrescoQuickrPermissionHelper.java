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

package org.alfresco.repo.lotus.ws.impl.helper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.security.AccessStatus;
import org.alfresco.service.cmr.security.PermissionService;

/**
 * @author Dmitry Vaserin
 */
public class AlfrescoQuickrPermissionHelper
{
    private final static Map<String, String> quickrToAlfPerms;

    static
    {
        quickrToAlfPerms = new HashMap<String, String>();
        quickrToAlfPerms.put("AddChild", PermissionService.ADD_CHILDREN);
        quickrToAlfPerms.put("Delete", PermissionService.DELETE);
        quickrToAlfPerms.put("Edit", PermissionService.WRITE);
        quickrToAlfPerms.put("View", PermissionService.READ);
        quickrToAlfPerms.put("GrantAccess", PermissionService.CHANGE_PERMISSIONS);
        // quickrToAlfPerms.put("Delegate",PermissionService.);
        quickrToAlfPerms.put("LockOverride", PermissionService.CANCEL_CHECK_OUT);
        quickrToAlfPerms.put("AddFolder", PermissionService.ADD_CHILDREN);
        quickrToAlfPerms.put("EditFolder", PermissionService.WRITE);
    }

    private PermissionService permissionService;

    public void setPermissionService(PermissionService permissionService)
    {
        this.permissionService = permissionService;
    }

    /**
     * Get all the Quickr permissions that are granted to the current authentication for the given node
     * 
     * @param nodeRef - the reference to the node
     * @return the set of allowed permissions
     */
    public List<String> getPermissions(NodeRef nodeRef)
    {
        List<String> allowedPerms = new LinkedList<String>();
        for (String quickrPerm : quickrToAlfPerms.keySet())
        {
            if (permissionService.hasPermission(nodeRef, quickrToAlfPerms.get(quickrPerm)).equals(AccessStatus.ALLOWED))
            {
                allowedPerms.add(quickrPerm);
            }
        }

        return allowedPerms;
    }
}
