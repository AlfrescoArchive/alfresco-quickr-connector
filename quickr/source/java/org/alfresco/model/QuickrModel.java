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
package org.alfresco.model;

import org.alfresco.service.namespace.QName;

public interface QuickrModel
{
    static final String QUICKR_MODEL_1_0_URI = "http://www.alfresco.org/model/quickr/1.0";
    static final String PROP_SHHET_MODEL_1_0_URI = "http://www.alfresco.org/model/quickr/psheet/1.0";
    static final String DRAFT_APPROVE__MODEL_1_0_URI = "http://www.alfresco.org/model/quickr/draft/approve/1.0";

    static final QName ASPECT_QUICKR_DOC_TYPE = QName.createQName(QUICKR_MODEL_1_0_URI, "docType");
    static final QName ASPECT_QUICKR_PROP_SHEET = QName.createQName(QUICKR_MODEL_1_0_URI, "propSheet");
    static final QName ASPECT_QUICKR_DRAFT_APPROVAL_TYPE = QName.createQName(QUICKR_MODEL_1_0_URI, "draftApprovals");
    static final QName ASPECT_QUICKR_VERSION_PROP = QName.createQName(QUICKR_MODEL_1_0_URI, "versioning");

    static final QName ASPECT_QUICKR_INITIAL_DRAFT = QName.createQName("http://www.alfresco.org/model/quickr/1.0", "draft");

}
