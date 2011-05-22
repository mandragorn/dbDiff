/**
 * Copyright 2011 Vecna Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
*/

package com.vecna.dbDiff.business.dbCompare.impl;


/**
 * @author dlopuch@vecna.com
 */
public class RdbCompareError {
  private RdbCompareErrorType m_errorType;
  private String m_message;
  
  public RdbCompareError(RdbCompareErrorType errorType, String message) {
    m_errorType = errorType;
    m_message = message;
  }
  
  /**
   * Set the errorType.
   * @param errorType The errorType to set
   */
  public void setErrorType(RdbCompareErrorType errorType) {
    this.m_errorType = errorType;
  }
  /**
   * Get the errorType.
   * @return Returns the errorType
   */
  public RdbCompareErrorType getErrorType() {
    return m_errorType;
  }
  /**
   * Set the message.
   * @param message The message to set
   */
  public void setMessage(String message) {
    this.m_message = message;
  }
  /**
   * Get the message.
   * @return Returns the message
   */
  public String getMessage() {
    return m_message;
  }
}