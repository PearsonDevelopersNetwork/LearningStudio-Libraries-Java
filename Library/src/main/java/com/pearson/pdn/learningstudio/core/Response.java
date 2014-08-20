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

import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

/**
 * The response object returned by all Service objects
 */
public class Response {
	private String method;
	private String url;
	private String content;
	private String contentType;
	private int statusCode;
	private String statusMessage;
	private byte[] binaryContent;
	private Map<String,List<String>> headers;

	/**
	 * Get the HTTP method used by the last performed operation
	 * 
	 * @return	Name of the HTTP method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Sets the HTTP method used by the last performed operation
	 * 
	 * @param method	Name of the HTTP Method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Get the URL used by the last performed operation
	 * 
	 * @return	URL String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the URL used by the last performed operation
	 * 
	 * @param url	URL String
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Get the content collected by the operation
	 * 
	 * @return	String of content
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Set the content collected by the operation
	 * 
	 * @param content	String of content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Get the type of content collected by the operation
	 * 
	 * @return Content-Type String
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * Sets the type of content collected by the operation
	 * 
	 * @param contentType Content-Type string
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * Get the HTTP status code returned by the last operation
	 * 
	 * @return HTTP Status Code
	 */
	public int getStatusCode() {
		return statusCode;
	}
	
	/**
	 * Set HTTP status code of last operation
	 * 
	 * @param statusCode	HTTP Status Code
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Get HTTP response status message associated with last operation
	 * 
	 * @return	HTTP Status Message
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * Set HTTP response status message 
	 * 
	 * @param statusMessage	HTTP Status Message
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
	/**
	 * Indicates whether the content is binary. If so, it has been base64 encoded.
	 * 
	 * @return	Boolean indicator of binary content
	 */
	public boolean isBinaryContent() {
		return binaryContent!=null;
	}

	/**
	 * Sets the binary content
	 * 
	 * @param binaryContent	Boolean indicator of binaryContent
	 */
	public void setBinaryContent(byte[] binaryContent) {
		this.binaryContent = binaryContent;
	}
	
	/**
	 * Gets the binary content
	 * 
	 * @return	Binary content
	 */
	public byte[] getBinaryContent() {
		return binaryContent;
	}
	
	/**
	 * Gets the response headers from the request
	 * 
	 * @return	KVP of headers
	 */
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	/**
	 * Sets the response headers from the request
	 * 
	 * @param headers	KVP of response headers
	 */
	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}
	
	/**
	 * Indicates if an error occurred during last operation
	 * 
	 * False indicates the operation completed successfully. True indicates the operation was aborted.
	 * 
	 * @return	Boolean indicator of error condition
	 */
	public boolean isError() {
		return statusCode < 200 || statusCode >= 300; // only if outside 200 range
	}
	
	/**
	 * Implements the toString method for use in debugging
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Method: ").append(method).append(", ");
		sb.append("URL: ").append(url).append(", ");
		sb.append("Code: ").append(statusCode).append(", ");
		sb.append("Message: ").append(statusMessage).append(", ");
		
		sb.append("Headers: ");
		String delimiter = "";
		for(String headerField : headers.keySet()) {
			sb.append(delimiter).append(headerField).append("=");
			delimiter = "";
			for(String headerValue : headers.get(headerField)) {
				sb.append(delimiter).append(headerValue);
				delimiter = ", ";
			}
			delimiter = "; ";
		}
		sb.append(", ");
		
		sb.append("Content-Type: ").append(contentType).append(", ");
		sb.append("Content: ").append(content);
		
		return sb.toString();
	}
	
}
