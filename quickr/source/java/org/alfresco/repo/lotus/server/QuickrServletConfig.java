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

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * @author EugeneZh
 */
public class QuickrServletConfig implements ServletConfig
{
    public String getInitParameter(String arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration<?> getInitParameterNames()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ServletContext getServletContext()
    {
        return new QuickrServletContext();
    }
    
    public String getServletName()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
