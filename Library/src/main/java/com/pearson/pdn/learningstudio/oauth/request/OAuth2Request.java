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
 *	OAuth2 request
 */
public class OAuth2Request implements OAuthRequest {
	
	private Map<String,String> headers;
	private String accessToken;
	private String refreshToken;
	private Integer expiresInSeconds;
	private Long creationTime;
	
	/**
	 * Get the header values for an OAuth request
	 * 
	 * @return	KVP header values
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	/**
	 * Set the headers for the OAuth2 Request
	 * 
	 * @param headers	KVP header values
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	/**
	 * Get access token for request
	 * 
	 * @return	Access token value
	 */
	public String getAccessToken() {
		return accessToken;
	}
	
	/**
	 * Set access token for request
	 * 
	 * @param accessToken	Access token value
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * Get refresh token for request
	 * 
	 * @return	Refresh token for request
	 */
	public String getRefreshToken() {
		return refreshToken;
	}
	
	/**
	 * Set refresh token for request
	 * 
	 * @param refreshToken	Refresh token for request
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * Get seconds until expiration
	 * 
	 * @return	Seconds until expiration
	 */
	public Integer getExpiresInSeconds() {
		return expiresInSeconds;
	}

	/**
	 * Sets seconds until expiration
	 * 
	 * @param expiresInSeconds	Seconds until expiration
	 */
	public void setExpiresInSeconds(Integer expiresInSeconds) {
		this.expiresInSeconds = expiresInSeconds;
	}
	
	/**
	 * Get Creation time of request
	 * 
	 * @return	Milliseconds since epoch
	 */
	public Long getCreationTime() {
		return creationTime;
	}

	/**
	 * Set creation time of request
	 * 
	 * @param creationTime	Milliseconds since epoch
	 */
	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}
	
	/**
	 * Get expiration time of request
	 * 
	 * @return	Milliseconds since epoch
	 */
	public Long getExpirationTime() {
		if(creationTime==null || expiresInSeconds==null) {
			return null;
		}
		
		return creationTime + (expiresInSeconds*1000);
	}
	
}
