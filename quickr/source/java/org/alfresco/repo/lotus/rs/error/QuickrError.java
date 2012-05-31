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
package org.alfresco.repo.lotus.rs.error;

/**
 * @author EugeneZh
 */
public enum QuickrError
{
    ITEM_NOT_FOUND(404, 1, "ItemNotFound"),

    UNKNOWN(500, 2, "Unknown"),
    
    COSNTRAIN_TVIOLATION(500, 3, "ConstraintViolation"),

    ITEM_EXISTS(409, 4, "ItemExists"),
    
    ACCESS_DENIED(403, 5, "AccessDenied");

    private int responseStatus;

    private int errorCode;

    private String errorId;

    QuickrError(int responseStatus, int errorCode, String errorId)
    {
        this.responseStatus = responseStatus;
        this.errorCode = errorCode;
        this.errorId = errorId;
    }

    public int getResponseStatus()
    {
        return responseStatus;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getErrorId()
    {
        return errorId;
    }

    public static String getErrorIdByErrorCode(int errorCode)
    {
        QuickrError errors[] = values();
        for (QuickrError quickrError : errors)
        {
            if (quickrError.errorCode == errorCode)
            {
                return quickrError.errorId;
            }
        }
        return "";
    }
}
