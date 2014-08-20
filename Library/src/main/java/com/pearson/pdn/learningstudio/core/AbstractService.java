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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pearson.pdn.learningstudio.core.AbstractService;
import com.pearson.pdn.learningstudio.oauth.*;
import com.pearson.pdn.learningstudio.oauth.request.*;

/**
 * Base functionality of all services
 */
public abstract class AbstractService {
	/**
	 * The format that data is accepted and returned
	 */
	public enum DataFormat {
		JSON, XML
	}
	
	/**
	 * The HTTP method
	 */
	public enum HttpMethod {
		GET, PUT, POST, DELETE
	}
	
	/**
	 * The method of authentication
	 */
	protected enum AuthMethod { 
		OAUTH1_SIGNATURE, OAUTH2_ASSERTION, OAUTH2_PASSWORD 
	}
	
	private final static Logger logger = Logger.getLogger(AbstractService.class);
	
	private final static String API_DOMAIN = "https://api.learningstudio.com";
	private final static String PATH_SYSTEMDATETIME = "/systemDateTime";
	
	protected final static String NO_CONTENT = "";
	
	protected JsonParser jsonParser;
	protected Gson gson;
	
	private OAuthServiceFactory oauthServiceFactory;
	private AuthMethod authMethod;
	private String username;
	private String password;
	private DataFormat dataFormat;
	private OAuthRequest currentOAuthRequest;
		
	/**
	 * Constructs an AbstractService
	 * 
	 * @param oauthServiceFactory	Service provider for OAuth operations
	 */
	protected AbstractService(OAuthServiceFactory oauthServiceFactory) {
		this.oauthServiceFactory = oauthServiceFactory;
		this.jsonParser = new JsonParser();
		this.gson = new GsonBuilder().setPrettyPrinting().create();
		this.dataFormat = DataFormat.JSON;
	}
	
	/**
	 * Provides name of service for identification purposes
	 * 
	 * @return Unique identifier for service
	 */
	protected abstract String getServiceIdentifier();

	/**
	 * Get DataFormat preferred by operations
	 * 
	 * @return	DataFormat
	 */
	public DataFormat getPreferredFormat() {
		return dataFormat;
	}

	/**
	 * Set DataFormat preferred by operations
	 * 
	 * @param dataFormat
	 */
	public void setPreferredFormat(DataFormat dataFormat) {
		this.dataFormat = dataFormat;
	}

	/**
	 * Makes all future request use OAuth1 security
	 */
	public void useOAuth1(){
		this.authMethod = AuthMethod.OAUTH1_SIGNATURE;
		this.username = null;
		this.password = null;
		this.currentOAuthRequest = null;
	}

	/**
	 *  Makes all future request use OAuth2 assertion security
	 *  
	 * @param username
	 */
	public void useOAuth2(String username) {
		this.authMethod = AuthMethod.OAUTH2_ASSERTION;
		this.username = username;
		this.password = null;
		this.currentOAuthRequest = null;
	}

	/**
	 * Makes all future request use OAuth2 password security
	 * 
	 * @param username
	 * @param password
	 */
	public void useOAuth2(String username, String password) {
		this.authMethod = AuthMethod.OAUTH2_PASSWORD;
		this.username = username;
		this.password = password;
		this.currentOAuthRequest = null;
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
	protected Response doMethod(HttpMethod method, String relativeUrl, String body) throws IOException {
		return doMethod(null, method, relativeUrl, body);
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
	protected Response doMethod(Map<String,String> extraHeaders, HttpMethod method, String relativeUrl, String body) throws IOException {

		if(body==null) {
			body = "";
		}
		
		// append .xml extension when XML data format enabled.
		if(dataFormat == DataFormat.XML) {	
			logger.debug("Using XML extension on route");
			
			String queryString = "";
			int queryStringIndex = relativeUrl.indexOf('?');
			if(queryStringIndex!=-1) {
				queryString = relativeUrl.substring(queryStringIndex);
				relativeUrl = relativeUrl.substring(0,queryStringIndex);
			}
			
			String compareUrl = relativeUrl.toLowerCase();
			
			if(!compareUrl.endsWith(".xml")) {
				relativeUrl += ".xml";
			}
			
			if(queryStringIndex!=-1) {
				relativeUrl += queryString;
			}
		}
		
		final String fullUrl = API_DOMAIN + relativeUrl;
		
		if(logger.isDebugEnabled()) {
			logger.debug("REQUEST - Method: " + method.name() + ", URL: " + fullUrl + ", Body: " + body );
		}
		
		URL url = new URL(fullUrl);
		Map<String,String> oauthHeaders = getOAuthHeaders(method,url,body);
		
		if(oauthHeaders==null) {
			throw new RuntimeException("Authentication method not selected. SEE useOAuth# methods");
		}
			
		if(extraHeaders != null) {
			for(String key : extraHeaders.keySet()) {
				if(!oauthHeaders.containsKey(key)) {
					oauthHeaders.put(key,extraHeaders.get(key));
				}
				else {
					throw new RuntimeException("Extra headers can not include OAuth headers");
				}
			}
		}
	
		HttpsURLConnection request = (HttpsURLConnection) url.openConnection();
		try {
			request.setRequestMethod(method.toString());
			
			Set<String> oauthHeaderKeys = oauthHeaders.keySet();
			for(String oauthHeaderKey : oauthHeaderKeys) {
				request.addRequestProperty(oauthHeaderKey,oauthHeaders.get(oauthHeaderKey));
			}
			
			request.addRequestProperty("User-Agent", getServiceIdentifier());
			
			if((method == HttpMethod.POST || method == HttpMethod.PUT) && body.length()>0) {
				if(dataFormat == DataFormat.XML) {
					request.setRequestProperty("Content-Type", "application/xml");
				}
				else {
					request.setRequestProperty("Content-Type", "application/json");
				}
				
				request.setRequestProperty("Content-Length", String.valueOf(body.getBytes("UTF-8").length));
				request.setDoOutput(true);
		
				DataOutputStream out = new DataOutputStream(request.getOutputStream());
				try {
					out.writeBytes(body);
					out.flush();
				}
				finally {
					out.close();
				}
			}
	
			Response response = new Response();
			response.setMethod(method.toString());
			response.setUrl(url.toString());
			response.setStatusCode(request.getResponseCode());
			response.setStatusMessage(request.getResponseMessage());
			response.setHeaders(request.getHeaderFields());

			InputStream inputStream = null;
			if(response.getStatusCode() < ResponseStatus.BAD_REQUEST.code()) {
				inputStream = request.getInputStream();
			}
			else {
				inputStream = request.getErrorStream();
			}
			
			boolean isBinary = false;
			if(inputStream != null) {
				StringBuilder responseBody = new StringBuilder();
				
				String contentType = request.getContentType();
				if(contentType != null) {
					if(!contentType.startsWith("text/") 
						&& !contentType.startsWith("application/xml")
						&& !contentType.startsWith("application/json")) { // assume binary
						isBinary = true;
						inputStream = new Base64InputStream(inputStream,true); // base64 encode
					}
				}
				
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
				try {
					String line = null;
					while((line=bufferedReader.readLine())!=null) {
						responseBody.append(line);
					}
				}
				finally {
					bufferedReader.close();
				}
				
				response.setContentType(contentType);
				
				if(isBinary) {
					String content = responseBody.toString();
					if(content.length()==0) {
						response.setBinaryContent(new byte[0]);
					}
					else {
						response.setBinaryContent(Base64.decodeBase64(responseBody.toString()));
					}
				}
				else {
					response.setContent(responseBody.toString());
				}
			}
			
			if(logger.isDebugEnabled()) {
				if(isBinary) {
					logger.debug("RESPONSE - binary response omitted");
				}
				else { 
					logger.debug("RESPONSE - " + response.toString());
				}
			}
		
			return response;
		}
		finally {
			request.disconnect();
		}
		
	}
	
	/**
	 * Performs time lookup with
	 * /systemDateTime
	 * using OAuth1 or OAuth2
	 * 
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getSystemDateTime() throws IOException {
		return doMethod(HttpMethod.GET, PATH_SYSTEMDATETIME, NO_CONTENT);
	}
	
	/**
	 * Performs time lookup with
	 * /systemDateTime
	 * using OAuth1 or OAuth2
	 * 
	 * @return	The milliseconds since the unix epoch
	 * @throws IOException
	 */
	public long getSystemDateTimeMillis() throws IOException, ParseException {
		Response response = getSystemDateTime();
		if(response.isError()) {
			throw new IOException("Time lookup failed:" + 
									response.getStatusCode() + " - " +
									response.getStatusMessage());
		}
		
		String timeValue = null;
		SimpleDateFormat dateFormat = null;
		if(dataFormat == DataFormat.XML) {
			 timeValue = parseXmlTag(response.getContent(),"systemDateTime","value");
			 dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
		}
		else {
			JsonObject json = jsonParser.parse(response.getContent()).getAsJsonObject();
			json = json.get("systemDateTime").getAsJsonObject();
			
			timeValue= json.get("value").getAsString();
			dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		}
		
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date dateTime = dateFormat.parse(timeValue);
		
		return dateTime.getTime();
	}
	
	/**
	 * Gets the relative path from a URL. 
	 * In example, /me from httpsL//api.learningstuiod.com/me
	 * 
	 * @param url A full url to make relative	
	 * @return	The relative path
	 */
	protected String getRelativePath(String url) {
		String relativeUrl = null;
		
		int index = url.indexOf(API_DOMAIN);
		if(index > -1) {
			index += API_DOMAIN.length();
			relativeUrl = url.substring(index);
		}
		else { // have seen other domains used in the links
			index = url.indexOf(".com");
			if(index > -1) {
				index += 4; // length of .com
				relativeUrl = url.substring(index);
			}
		}
			
		return relativeUrl;
	}
	
	/**
	 * Returns the headers required for the selected authentication of the given request
	 * 
	 * @param method	HTTP method of the request
	 * @param url	URL of the request
	 * @param body	Body of the request
	 * @return	KVP of headers for the request
	 * @throws IOException
	 */
	private Map<String,String> getOAuthHeaders(HttpMethod method, URL url, String body) throws IOException {
		Map<String,String> oauthHeaders = null;
		
		if(authMethod==AuthMethod.OAUTH1_SIGNATURE) {
			if(logger.isDebugEnabled()) {
				logger.debug("Generating OAuth1 headers");
			}
			OAuth1SignatureService oauthService = oauthServiceFactory.build(OAuth1SignatureService.class);
			OAuth1Request oauthRequest = oauthService.generateOAuth1Request(method.name(), url, body);
			oauthHeaders = oauthRequest.getHeaders();
		}
		else {
			// check to see if a previous oauth2 request has been made
			if(currentOAuthRequest!=null) {
				long now = System.currentTimeMillis();
				long expirationTime = ((OAuth2Request)currentOAuthRequest).getExpirationTime();
				
				// check to see if current ones are expired
				if(now>=expirationTime) {
					if(logger.isDebugEnabled()) {
						logger.debug("Previous OAuth2 headers have expired");
					}
					if(authMethod == AuthMethod.OAUTH2_PASSWORD) {
						if(logger.isDebugEnabled()) {
							logger.debug("Refreshing oauth2 token");
						}
						
						OAuth2Request refreshRequest = (OAuth2Request) currentOAuthRequest;
						currentOAuthRequest = null;  // forget the previous oauth2 request
						
						// attempt to use the refresh token
						try {
							OAuth2PasswordService oauthService = oauthServiceFactory.build(OAuth2PasswordService.class);
							currentOAuthRequest = oauthService.refreshOAuth2PasswordRequest(refreshRequest);
						}
						catch(Throwable t) {
							if(logger.isDebugEnabled()) {
								logger.debug("Failed to refresh oauth2 token", t);
							}
						}
					}
					else {
						currentOAuthRequest = null; // forget the previous oauth2 request
					}
				}
			}
			
			// Use the existing headers
			if(currentOAuthRequest!=null) {
				if(logger.isDebugEnabled()) {
					logger.debug("Reusing previous OAuth2 headers");
				}
				oauthHeaders = currentOAuthRequest.getHeaders(); // use the current headers
			}
			else {
				if(logger.isDebugEnabled()) {
					logger.debug("Generating new OAuth2 headers");
				}
				
				OAuth2Request oauthRequest = null;
				if(authMethod==AuthMethod.OAUTH2_ASSERTION) {
					OAuth2AssertionService oauthService = oauthServiceFactory.build(OAuth2AssertionService.class);
					oauthRequest = oauthService.generateOAuth2AssertionRequest(username);
					oauthHeaders = oauthRequest.getHeaders();
				}
				else if(authMethod==AuthMethod.OAUTH2_PASSWORD) {
					OAuth2PasswordService oauthService = oauthServiceFactory.build(OAuth2PasswordService.class);
					oauthRequest = oauthService.generateOAuth2PasswordRequest(username,password);
					oauthHeaders = oauthRequest.getHeaders();
				}
				
				currentOAuthRequest = oauthRequest; // save the new oauth request 
			}
		}
		
		if(oauthHeaders != null) {
			// clone to prevent issues from modifications when attempting reuse
			oauthHeaders = new LinkedHashMap<String,String>(oauthHeaders);
		}
		
		return oauthHeaders;
	}
	
	/**
	 * Release all handles to objects that may prevent garbage collection
	 */
	public void destroy() {
		this.jsonParser=null;
		this.gson=null;
		this.oauthServiceFactory=null;
		this.authMethod=null;
		this.username=null;
		this.password=null;
		this.dataFormat=null;
		this.currentOAuthRequest=null;
	}
	
	/**
	 * Simple xml tag parsing instead of another dependency
	 * 
	 * @param xml	The XML to parse
	 * @param tagNames	The List of tags to parse
	 * @return	Value of the tag
	 */
	private String parseXmlTag(String xml, String... tagNames) {
		for(String tagName :tagNames) {
			xml = xml.substring(0,xml.indexOf("</"+tagName+">"));
			int startTagIndex = xml.indexOf("<"+tagName+">");
			if(startTagIndex != -1) {
				xml = xml.substring(startTagIndex+tagName.length()+2);
			}
			else {
				xml = xml.substring(xml.indexOf("<"+tagName+" "));
				xml = xml.substring(xml.indexOf('>')+1);
			}
		}
		return xml;
	}
	
}
