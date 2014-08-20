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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;

import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader; 
import com.pearson.pdn.learningstudio.oauth.config.OAuth2AssertionConfig;
import com.pearson.pdn.learningstudio.oauth.request.OAuth2Request;

/**
 *	OAuth2 Assertion Service
 */
public class OAuth2AssertionService implements OAuthService {
	private final static String API_DOMAIN = "https://api.learningstudio.com";
	
	private OAuth2AssertionConfig configuration;

	/**
	 * Constructs and OAuth2 assertion service
	 * 
	 * @param configuration	Configuration parameters for OAuth2
	 */
	public OAuth2AssertionService(OAuth2AssertionConfig configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Generates OAuth2 Assertion Request
	 * 
	 * @param username	Username for request
	 * @return	OAuth2 request with assertion
	 * @throws IOException
	 */
	public OAuth2Request generateOAuth2AssertionRequest(String username) throws IOException {
		
		final String grantType = "assertion";
		final String assertionType = "urn:ecollege:names:moauth:1.0:assertion";
		final String url = API_DOMAIN+ "/token";
		
		final String applicationId = configuration.getApplicationId();
		final String applicationName = configuration.getApplicationName();
		final String consumerKey = configuration.getConsumerKey();
		final String clientString = configuration.getClientString();
		final String consumerSecret = configuration.getConsumerSecret();
		
		OAuth2Request oauthRequest = null;
		HttpsURLConnection httpConn = null;
		JsonReader in = null;
		try {
				
			// Create the Assertion String
			String assertion = buildAssertion(applicationName, consumerKey, applicationId, clientString, username, consumerSecret);
	
			// Create the data to send
			StringBuilder data = new StringBuilder();
			data.append("grant_type=").append(URLEncoder.encode(grantType, "UTF-8"));
			data.append("&assertion_type=").append(URLEncoder.encode(assertionType, "UTF-8"));
			data.append("&assertion=").append(URLEncoder.encode(assertion, "UTF-8"));
			
			// Create a byte array of the data to be sent
			byte[] byteArray = data.toString().getBytes("UTF-8");
	
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
			Type mapTypeAccess = new TypeToken<Map<String, String>>() {}.getType();
			Map<String, String> ser = gson.fromJson(in, mapTypeAccess);
			String accessToken = ser.get("access_token");
			
			if(accessToken==null || accessToken.length()==0) {
				throw new IOException("Missing Access Token");
			}
			
			oauthRequest = new OAuth2Request();
			oauthRequest.setAccessToken(accessToken);
			oauthRequest.setExpiresInSeconds(new Integer(ser.get("expires_in")));
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
	
	
	/**
	 * Builds an OAuth 2.0 assertion string
	 * 
	 * @param applicationName
	 *            The name of the application brokering the token request
	 * @param keyMoniker
	 *            The assigned API key moniker
	 * @param applicationID
	 *            The assigned API application id
	 * @param clientString
	 *            The string value that uniquely identifies the campus/school
	 * @param username
	 *            The LearningStudio user's external identifier
	 * @param secret
	 *            The secret key used to sign the assertion string
	 * 
	 * @return A signed, encoded, and fully constructed assertion string
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private String buildAssertion(String applicationName, String keyMoniker, String applicationID,
			String clientString, String username, String secret) throws UnsupportedEncodingException {
		// Get the UTC Date Timestamp
		DateFormat gmtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		TimeZone gmtTime = TimeZone.getTimeZone("GMT");
		gmtFormat.setTimeZone(gmtTime);
		String timestamp = gmtFormat.format(new Date()) + "Z";

		// Setup the Assertion String
		String assertion = applicationName + "|" + keyMoniker + "|" + applicationID + "|" + clientString + "|"
				+ username + "|" + timestamp;

		// Generate the CMAC used for Assertion Security
		String cmac = generateCmac(secret, assertion);

		// Add the CMAC to the Assertion String
		assertion = assertion + "|" + cmac;

		return assertion;
	}
	
	/**
	 * Generates a HEX-encoded CMAC-AES digest
	 * 
	 * @param key
	 *            The secret key used to sign the data
	 * @param msg
	 *            The data to be signed
	 * 
	 * @return A CMAC-AES digest
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private String generateCmac(String key, String msg) throws UnsupportedEncodingException {
		byte[] keyBytes = key.getBytes("UTF-8");
		byte[] data = msg.getBytes("UTF-8");

		CMac macProvider = new CMac(new AESFastEngine());
		macProvider.init(new KeyParameter(keyBytes));
		macProvider.reset();

		macProvider.update(data, 0, data.length);
		byte[] output = new byte[macProvider.getMacSize()];
		macProvider.doFinal(output, 0);

		return Strings.fromUTF8ByteArray(Hex.encode(output));
	}
}
