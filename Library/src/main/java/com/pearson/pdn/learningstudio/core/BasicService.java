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
package com.pearson.pdn.learningstudio.core;

import java.io.IOException;
import java.util.Map;

import com.pearson.pdn.learningstudio.oauth.OAuthServiceFactory;

/**
 * A basic service exposing the core functionality of AbstractService without wrapping methods
 */
public class BasicService extends AbstractService {
	
	public BasicService(OAuthServiceFactory oauthServiceFactory) {
		super(oauthServiceFactory);
	}
	
	/**
	 * Provides name of library for identification purposes
	 * 
	 * @return Unique identifier for library
	 */
	protected String getServiceIdentifier() {
		return "LS-Library-Core-Java-V1";
	}
	
	/**
	 * Performs HTTP operations using the selected authentication method
	 * 
	 * @param method	The HTTP Method to user
	 * @param relativeUrl	The URL after .com (/me)
	 * @param body	The body of the message
	 * @return Output in the preferred data format
	 * @throws IOException
	 */
	public Response doMethod(HttpMethod method, String relativeUrl, String body) throws IOException {
		return super.doMethod(method, relativeUrl, body);
	}
	
	/**
	 * Performs HTTP operations using the selected authentication method
	 * 
	 * @param extraHeaders	Extra headers to include in the request
	 * @param method	The HTTP Method to user
	 * @param relativeUrl	The URL after .com (/me)
	 * @param body	The body of the message
	 * @return Output in the preferred data format
	 * @throws IOException
	 */
	public Response doMethod(Map<String,String> extraHeaders, HttpMethod method, String relativeUrl, String body) throws IOException {
		return super.doMethod(extraHeaders, method, relativeUrl, body);
	}
}
