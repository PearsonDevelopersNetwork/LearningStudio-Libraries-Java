/*
 * LearningStudio RESTful API Libraries 
 * These libraries make it easier to use the LearningStudio Course APIs.
 * Full Documentation is provided with the library. 
 * 
 * Need Help or Have Questions? 
 * Please use the PDN Developer Community at https://community.pdn.pearson.com
 *
 * @category   LearningStudio Course APIs
 * @author     Wes Williams <wes.williams@pearson.com>
 * @author     Pearson Developer Services Team <apisupport@pearson.com>
 * @copyright  2014 Pearson Education Inc.
 * @license    http://www.apache.org/licenses/LICENSE-2.0  Apache 2.0
 * @version    1.0
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
 */
package com.pearson.pdn.learningstudio.oauth.request;

import java.util.Map;

/**
 *	OAuth1 Request
 */
public class OAuth1Request implements OAuthRequest {
	
	private Map<String,String> headers;
	private String signature;
	
	/**
	 * Get the header values for an OAuth request
	 * 
	 * @return	KVP header values
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	/**
	 * Set the header values for an OAuth request
	 * 
	 * @param headers	KVP header values
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * Get the OAuth1 signature
	 * 
	 * @return	Value of OAuth1 Signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * Sets the OAuth1 signature
	 * 
	 * @param signature	Value of OAuth1 Signature
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

}
