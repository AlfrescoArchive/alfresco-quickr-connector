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

package org.alfresco.repo.lotus.rs.impl.providers;

import org.apache.abdera.util.Constants;
import org.dom4j.QName;

/**
 * @author EugeneZh
 */
public interface AtomConstants
{
    // namespaces

    String ATOM_NS_PREFIX = "atom";

    // elements

    QName SERVICE = QName.get(Constants.LN_SERVICE, Constants.APP_PREFIX, Constants.APP_NS);

    QName WORKSPACE = QName.get(Constants.LN_WORKSPACE, Constants.APP_PREFIX, Constants.APP_NS);

    QName COLLECTION = QName.get(Constants.LN_COLLECTION, Constants.APP_PREFIX, Constants.APP_NS);

    QName ACCEPT = QName.get(Constants.LN_ACCEPT, Constants.APP_PREFIX, Constants.APP_NS);

    // attributes

    QName TITLE_ATTRIBUTE = QName.get(Constants.LN_TITLE);

    QName HREF_ATTRIBUTE = QName.get(Constants.LN_HREF);

    // freemarker

    String TEMPLATE_ENCODING = "utf-8";
}
