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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.pearson.pdn.learningstudio.oauth.config.OAuth2PasswordConfig;
import com.pearson.pdn.learningstudio.oauth.request.OAuth2Request;

/**
 * OAuth2 Password Service
 */
public class OAuth2PasswordService implements OAuthService {
	private final static String API_DOMAIN = "https://api.learningstudio.com";
	private OAuth2PasswordConfig configuration;

	/**
	 * Contructs and OAuth2 Password Service
	 * 
	 * @param configuration	Configuration parameters for OAuth2
	 */
	public OAuth2PasswordService(OAuth2PasswordConfig configuration) {
		this.configuration = configuration;
	}

	/**
	 * Generates OAuth2 password request
	 * 
	 * @param username	Username for request
	 * @param password	Password for request
	 * @return	OAuth2 request with password
	 * @throws IOException
	 */
	public OAuth2Request generateOAuth2PasswordRequest(String username, String password) throws IOException {	
		
		final String grantType = "password";
		final String applicationId = configuration.getApplicationId();
		final String clientString = configuration.getClientString();
		
		// Create the data to send
		StringBuilder data = new StringBuilder();
		data.append("grant_type=").append(URLEncoder.encode(grantType, "UTF-8"));
		data.append("&client_id=").append(URLEncoder.encode(applicationId, "UTF-8"));
		data.append("&username=").append(URLEncoder.encode(clientString + "\\" + username, "UTF-8"));
		data.append("&password=").append(URLEncoder.encode(password, "UTF-8"));

		// Create a byte array of the data to be sent
		byte[] byteArray = data.toString().getBytes("UTF-8");
		return doRequest(byteArray);
	}
	
	/**
	 * Refreshes OAuth2 password request
	 * 
	 * @param lastOAuthRequest	OAuth2 password request to refresh
	 * @return	New OAuth2 request with password
	 * @throws IOException
	 */
	public OAuth2Request refreshOAuth2PasswordRequest(OAuth2Request lastOAuthRequest) throws IOException {	
		
		final String grantType = "refresh_token";
		final String applicationId = configuration.getApplicationId();
			
		// Create the data to send
		StringBuilder data = new StringBuilder();
		data.append("grant_type=").append(URLEncoder.encode(grantType, "UTF-8"));
		data.append("&client_id=").append(URLEncoder.encode(applicationId, "UTF-8"));
		data.append("&refresh_token=").append(URLEncoder.encode(lastOAuthRequest.getRefreshToken(), "UTF-8"));
		
		byte[] byteArray = data.toString().getBytes("UTF-8");
		return doRequest(byteArray);
	}
	
	private OAuth2Request doRequest(byte[] byteArray) throws IOException {	
		final String url = API_DOMAIN + "/token";
		
		OAuth2Request oauthRequest = null;		
		HttpsURLConnection httpConn = null;
		JsonReader in = null;
		try {			
			// Setup the Request
			URL request = new URL(url);
			httpConn = (HttpsURLConnection) request.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.addRequestProperty("User-Agent","LS-Library-OAuth-Java-V1");
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConn.setRequestProperty("Content-Length", "" + byteArray.length);
			httpConn.setDoOutput(true);

			// Write data
			OutputStream postStream = httpConn.getOutputStream();
			postStream.write(byteArray, 0, byteArray.length);
			postStream.close();

			long creationTime = System.currentTimeMillis();
			
			// Send Request & Get Response
			in = new JsonReader(new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8")));

			// Parse the Json response and retrieve the Access Token
			Gson gson = new Gson();
			Type mapType = new TypeToken<Map<String, String>>() {}.getType();
			Map<String, String> ser = gson.fromJson(in, mapType);
			String accessToken = ser.get("access_token");
			
			if(accessToken==null || accessToken.length()==0) {
				throw new IOException("Missing Access Token");
			}
			
			oauthRequest = new OAuth2Request();
			oauthRequest.setAccessToken(accessToken);
			oauthRequest.setExpiresInSeconds(new Integer(ser.get("expires_in")));
			oauthRequest.setRefreshToken(ser.get("refresh_token"));
			oauthRequest.setCreationTime(creationTime);
			
			Map<String,String> headers = new TreeMap<String,String>();
			headers.put("X-Authorization", "Access_Token access_token="+accessToken);
			oauthRequest.setHeaders(headers);
			
		} finally {
			// Be sure to close out any resources or connections
			if (in != null)
				in.close();
			if (httpConn != null)
				httpConn.disconnect();
		}
			
		return oauthRequest;
	}
}
