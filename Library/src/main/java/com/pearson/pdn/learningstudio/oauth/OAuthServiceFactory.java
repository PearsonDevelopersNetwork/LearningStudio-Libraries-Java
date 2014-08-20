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
package com.pearson.pdn.learningstudio.oauth;

import com.pearson.pdn.learningstudio.oauth.config.OAuth1SignatureConfig;
import com.pearson.pdn.learningstudio.oauth.config.OAuth2AssertionConfig;
import com.pearson.pdn.learningstudio.oauth.config.OAuth2PasswordConfig;
import com.pearson.pdn.learningstudio.oauth.config.OAuthConfig;

/**
 * Factory for building various OAuth services
 */
public class OAuthServiceFactory {
	private OAuthConfig configuration;
	
	private OAuth1SignatureService oauth1SignatureService;
	private OAuth2AssertionService oauth2AssertionService;
	private OAuth2PasswordService oauth2PasswordService;
	
	/**
	 * Constructs a OAuth service factory
	 * 
	 * @param config	Configuration parameters shared between services
	 */
	public OAuthServiceFactory(OAuthConfig config) {
		this.configuration = config;
	}
	
	/**
	 * Create the requested service type
	 * 
	 * @param serviceClass	Class of the service to create
	 * @return	Service of the request type
	 */
	public <T extends OAuthService> T build(Class<T> serviceClass) {
		
		if(serviceClass == OAuth1SignatureService.class) {		
			if(oauth1SignatureService==null) {
				OAuth1SignatureConfig config = new OAuth1SignatureConfig();
				config.setApplicationId(configuration.getApplicationId());
				config.setConsumerKey(configuration.getConsumerKey());
				config.setConsumerSecret(configuration.getConsumerSecret());
				
				oauth1SignatureService = new OAuth1SignatureService(config);
			}
			return (T) oauth1SignatureService;
		}
		else if(serviceClass == OAuth2AssertionService.class) {	
			if(oauth2AssertionService==null) {
				OAuth2AssertionConfig config = new OAuth2AssertionConfig();
				config.setApplicationId(configuration.getApplicationId());
				config.setApplicationName(configuration.getApplicationName());
				config.setClientString(configuration.getClientString());
				config.setConsumerKey(configuration.getConsumerKey());
				config.setConsumerSecret(configuration.getConsumerSecret());
				
				oauth2AssertionService = new OAuth2AssertionService(config);
			}
			return (T) oauth2AssertionService;
		}
		else if(serviceClass == OAuth2PasswordService.class) {	
			if(oauth2PasswordService==null) {
				OAuth2PasswordConfig config = new OAuth2PasswordConfig();
				config.setApplicationId(configuration.getApplicationId());
				config.setClientString(configuration.getClientString());
				
				oauth2PasswordService = new OAuth2PasswordService(config);
			}
			return (T) oauth2PasswordService;
		}
		
		throw new RuntimeException("Not implemented: " + serviceClass);
	}
	
}
