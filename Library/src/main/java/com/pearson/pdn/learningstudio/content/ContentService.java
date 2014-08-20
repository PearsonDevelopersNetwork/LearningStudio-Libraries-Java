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
package com.pearson.pdn.learningstudio.content;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pearson.pdn.learningstudio.core.AbstractService;
import com.pearson.pdn.learningstudio.core.Response;
import com.pearson.pdn.learningstudio.oauth.OAuthServiceFactory;

public class ContentService extends AbstractService {
	
	// PATH CONSTANTS
	private final static String PATH_COURSES_ITEMS = "/courses/%s/items";
	private final static String PATH_USERS_COURSES_ITEMS = "/users/%s/courses/%s/items";
	private final static String PATH_COURSES_ITEMSHIERARCHY = "/courses/%s/itemHierarchy";
	private final static String PATH_COURSES_ITEMSHIERARCHY__EXPAND_ = "/courses/%s/itemHierarchy?expand=%s";
	private final static String PATH_USERS_COURSES_ITEMSHIERARCHY = "/users/%s/courses/%s/itemHierarchy";
	private final static String PATH_USERS_COURSES_ITEMSHIERARCHY__EXPAND_ = "/users/%s/courses/%s/itemHierarchy?expand=%s";
	private final static String PATH_COURSES_ITEMS_ = "/courses/%s/items/%s";
	private final static String PATH_COURSES_TEXTMULTIMEDIAS = "/courses/%s/textMultimedias";
	private final static String PATH_COURSES_TEXTMULTIMEDIAS__CONTENTPATH_ = "/courses/%s/textMultimedias/%s/%s";
	private final static String PATH_COURSES_TEXTMULTIMEDIAS__CONTENTPATH__USESOURCEDOMAIN = "/courses/%s/textMultimedias/%s/%s?useSourceDomain=true";
	private final static String PATH_COURSES_TEXTMULTIMEDIAS_ = "/courses/%s/textMultimedias/%s";
	private final static String PATH_COURSES_MSOFFICEDOCUMENTS = "/courses/%s/msOfficeDocuments";
	private final static String PATH_COURSES_MSOFFICEDOCUMENTS_ = "/courses/%s/msOfficeDocuments/%s";
	private final static String PATH_COURSES_MSOFFICEDOCUMENTS_ORIGINALDOCUMENT = "/courses/%s/msOfficeDocuments/%s/originalDocument";
	private final static String PATH_COURSES_MSOFFICEDOCUMENTS_CONTENT_ = "/courses/%s/msOfficeDocuments/%s/content/%s";
	private final static String PATH_COURSES_WEBCONTENTUPLOADS = "/courses/%s/webContentUploads";
	private final static String PATH_COURSES_WEBCONTENTUPLOADS_ = "/courses/%s/webContentUploads/%s";
	private final static String PATH_COURSES_WEBCONTENTUPLOADS_ORIGINALDOCUMENT = "/courses/%s/webContentUploads/%s/originalDocument";
	private final static String PATH_COURSES_WEBCONTENTUPLOADS_CONTENT_ = "/courses/%s/webContentUploads/%s/content/%s";
	
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSEHIEARCHY = "/courses/%s/threadeddiscussions/%s/topics/%s/responseHierarchy";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSEHIEARCHY = "/courses/%s/threadeddiscussions/%s/topics/%s/responses/%s/responseHierarchy";
	private final static String PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_USERVIEWRESPONSES_USERVIEWRESPONSES = "/users/%s/courses/%s/threadeddiscussions/%s/topics/%s/userviewresponses/%s/userviewresponses";
	private final static String PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_USERVIEWRESPONSES_USERVIEWRESPONSES__DEPTH = PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_USERVIEWRESPONSES_USERVIEWRESPONSES + "?depth=%s";
	private final static String PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_USERVIEWRESPONSES = "/users/%s/courses/%s/threadeddiscussions/%s/topics/%s/userviewresponses";
	private final static String PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_USERVIEWRESPONSES__DEPTH = PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_USERVIEWRESPONSES + "?depth=%s";
	
	private final static String PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSECOUNTS = "/users/%s/courses/%s/threadeddiscussions/%s/topics/%s/responses/%s/responseCounts";
	private final static String PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSECOUNTS__DEPTH = PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSECOUNTS + "?depth=%s";
	private final static String PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSECOUNTS = "/users/%s/courses/%s/threadeddiscussions/%s/topics/%s/responseCounts";
	private final static String PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSECOUNTS__DEPTH = PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSECOUNTS + "?depth=%s";
	
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSEBRANCH = "/courses/%s/threadeddiscussions/%s/topics/%s/responses/%s/responseBranch";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSEAUTHOR = "/courses/%s/threadeddiscussions/%s/topics/%s/responses/%s/responseAuthor";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSEANDAUTHORCOMPS = "/courses/%s/threadeddiscussions/%s/topics/%s/responses/%s/responseAndAuthorComps";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSEANDAUTHORCOMPS__DEPTH = "/courses/%s/threadeddiscussions/%s/topics/%s/responses/%s/responseAndAuthorComps?depth=%s";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSEANDAUTHORCOMPS = "/courses/%s/threadeddiscussions/%s/topics/%s/responseAndAuthorComps";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSEANDAUTHORCOMPS__DEPTH = "/courses/%s/threadeddiscussions/%s/topics/%s/responseAndAuthorComps?depth=%s";
	
	private final static String PATH_USERS_COURSES_THREADEDDISCUSSIONS__LASTRESPONSE = "/users/%s/courses/%s/threadeddiscussions/lastResponse";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS = "/courses/%s/threadeddiscussions";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS__USESOURCEDOMAIN = "/courses/%s/threadeddiscussions?UseSourceDomain=true";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS = "/courses/%s/threadeddiscussions/%s/topics";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS__USESOURCEDOMAIN = "/courses/%s/threadeddiscussions/%s/topics?UseSourceDomain=true";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_ = "/courses/%s/threadeddiscussions/%s/topics/%s";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_USESOURCEDOMAIN = "/courses/%s/threadeddiscussions/%s/topics/%s?UseSourceDomain=true";
	
	private final static String PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSE_READSTATUS = "/users/%s/courses/%s/threadeddiscussions/%s/topics/%s/responses/%s/readStatus";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSES = "/courses/%s/threadeddiscussions/%s/topics/%s/responses/%s/responses";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_ = "/courses/%s/threadeddiscussions/%s/topics/%s/responses/%s";
	private final static String PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES = "/courses/%s/threadeddiscussions/%s/topics/%s/responses";
	
	/**
	 * Constructs a new ContentService
	 * 
	 * @param oauthServiceFactory Service provider for OAuth operations
	 */
	public ContentService(OAuthServiceFactory oauthServiceFactory) {
		super(oauthServiceFactory);
	}
	
	/**
	 * Provides name of service for identification purposes
	 * 
	 * @return Unique identifier for service
	 */
	protected String getServiceIdentifier() {
		return "LS-Library-Content-Java-V1";
	}
	
	/**
	 * Get items for a course with
	 * Get /courses/{courseId}/items
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getItems(String courseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_ITEMS, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get items for a course with
	 * Get /courses/{courseId}/items
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getUserItems(String userId, String courseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_ITEMS, userId, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get item hierarchy for a course with
	 * Get /courses/{courseId}/itemHierarchy
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getItemHierarchy(String courseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_ITEMSHIERARCHY, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get item hierarchy for a course with
	 * Get /courses/{courseId}/itemHierarchy?expand=item,item.access,item.schedule,item.group
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param expandItems	Comma separated list of items to expand from: item,item.access,item.schedule,item.group
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getItemHierarchy(String courseId, String expandItems) throws IOException {
		if(expandItems==null || expandItems.length()==0) {
			return getItemHierarchy(courseId);
		}
		
		String relativeUrl = String.format(PATH_COURSES_ITEMSHIERARCHY__EXPAND_, courseId,expandItems);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get item hierarchy for a course with
	 * Get /users/{userId}/courses/{courseId}/itemHierarchy
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getUserItemHierarchy(String userId, String courseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_ITEMSHIERARCHY, userId, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get item hierarchy for a course with
	 * Get /users/{userId}/courses/{courseId}/itemHierarchy?expand=item,item.access,item.schedule,item.group
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param expandItems	Comma separated list of items to expand from: item,item.access,item.schedule,item.group
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getUserItemHierarchy(String userId, String courseId, String expandItems) throws IOException {
		if(expandItems==null || expandItems.length()==0) {
			return getUserItemHierarchy(userId,courseId);
		}
		
		String relativeUrl = String.format(PATH_USERS_COURSES_ITEMSHIERARCHY__EXPAND_, userId, courseId,expandItems);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get a specific item for a course with
	 * Get /courses/{courseId}/items/{itemId}
	 * using OAuth1 or OAuth2 as a student, teacher, ta, or admin
	 * 
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getItem(String courseId, String itemId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_ITEMS_, courseId, itemId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get content for a specific item in a course with
	 * getItem(courseId, itemId)
	 * by following the links to the item itself
	 * and next to the contentUrl
	 * using OAuth1 or OAuth2 as a student, teacher, or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param itemId	ID of the item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getItemContent(String courseId, String itemId) throws IOException  {
		Response response = getItem(courseId, itemId);
		if(response.isError()) {
			return response;
		}
		
		// should only be one item here, but it is returned in an array for some reason
		String courseItemsJson = response.getContent();
		JsonObject json = this.jsonParser.parse(courseItemsJson).getAsJsonObject();
		JsonArray items = json.get("items").getAsJsonArray();
		
		// again, only one element expected here...
		Iterator<JsonElement> itemIterator = items.iterator();
		if(itemIterator.hasNext()) {
			JsonObject item = itemIterator.next().getAsJsonObject();
			JsonArray links = item.get("links").getAsJsonArray();
			
			for(Iterator<JsonElement> linkIter = links.iterator(); linkIter.hasNext();) {
				JsonObject link = linkIter.next().getAsJsonObject();
				
				JsonElement title = link.get("title");
				// rel on link varies, so identify self by missing title
				if(title==null) {
					String relativeUrl = this.getRelativePath(link.get("href").getAsString());
					response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
					if(response.isError()) {
						return response;
					}
					
					json = this.jsonParser.parse(response.getContent()).getAsJsonObject();
					String itemType = json.entrySet().iterator().next().getKey();
					json = json.get(itemType).getAsJsonArray().get(0).getAsJsonObject(); // single element wrapped in an array
					String contentUrl = json.get("contentUrl").getAsString();
					relativeUrl = this.getRelativePath(contentUrl);
					
					return doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
				}
			}
		}
		
		// should never get here
		throw new RuntimeException("No item content path found");
	}
	
	/**
	 * Get links details from a specific item for a course with
	 * Get /courses/{courseId}/items/{itemId}
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * Example JSON structure: (Multimedia item)
	 * 
	 * { 
	 *   "details": { 
	 *     "access": {...}, 
	 *     "schedule": {...}, 
	 *     "self": {...},
	 *     "selfType": "textMultimedias"
	 *   }
	 * }
	 * 
	 * @param courseId	ID of the course
	 * @param itemId	ID of the item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getItemLinkDetails(String courseId, String itemId) throws IOException {
		Response response = getItem(courseId, itemId);
		if(response.isError()) {
			return response;
		}
		
		// should only be one item here, but it is returned in an array for some reason
		String courseItemsJson = response.getContent();
		JsonObject json = this.jsonParser.parse(courseItemsJson).getAsJsonObject();
		JsonArray items = json.get("items").getAsJsonArray();
		JsonObject detail = new JsonObject();
		
		// again, only one element expected here...
		Iterator<JsonElement> itemIterator = items.iterator();
		if(itemIterator.hasNext()) {
			JsonObject item = itemIterator.next().getAsJsonObject();
			JsonArray links = item.get("links").getAsJsonArray();
			
			for(Iterator<JsonElement> linkIter = links.iterator(); linkIter.hasNext();) {
				JsonObject link = linkIter.next().getAsJsonObject();
				String relativeUrl = this.getRelativePath(link.get("href").getAsString());
				
				response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
				if(response.isError()) {
					return response;
				}
				
				JsonElement linkElement = this.jsonParser.parse(response.getContent());
				
				JsonElement title = link.get("title");
				// rel on link varies, so identify self by missing title
				if(title==null) {
					Map.Entry<String,JsonElement> self = linkElement.getAsJsonObject().entrySet().iterator().next();

					linkElement = self.getValue();
					// content items like to return a single resource in an array sometimes
					if(linkElement.isJsonArray()) {
						linkElement = linkElement.getAsJsonArray().get(0);
					}
					
					detail.add("self", linkElement);
					detail.addProperty("selfType", self.getKey());
				}
				else {
					// remove the first layer wrapper. it's just repetitive
					linkElement = linkElement.getAsJsonObject().get(title.getAsString());
					detail.add(title.getAsString(), linkElement);
				}
			}
			
			JsonObject detailWrapper = new JsonObject();
			detailWrapper.add("details",detail);
			
			response.setContent(this.gson.toJson(detailWrapper));
		}
		else { 
			// this should never happen, but it should be detected if it does
			throw new RuntimeException("Unexpected condition in library: No items");
		}


		return response;
	}
	
	/**
	 * Get text multimedias by course with
	 * GET /courses/{courseId}/textMultimedias
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getTextMultimedias(String courseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_TEXTMULTIMEDIAS, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get specific text multimedia content by course with
	 * GET /courses/{courseId}/textMultimedias
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param textMediaId	ID of the text media
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getTextMultimedia(String courseId, String textMediaId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_TEXTMULTIMEDIAS_, courseId, textMediaId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get specific text multimedia content by course with UseSourceDomain parameter with
	 * GET /courses/{courseId}/textMultimedias/{contentPath}
	 * GET /courses/{courseId}/textMultimedias/{contentPath}?UseSourceDomain=true
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param textMediaId	ID of the text multimedia
	 * @param contentPath	Path of content
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getTextMultimediasContent(String courseId, String textMediaId, String contentPath) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_TEXTMULTIMEDIAS__CONTENTPATH_, courseId, textMediaId, contentPath);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	
	/**
	 * Get specific text multimedia content by course with UseSourceDomain parameter with
	 * GET /courses/{courseId}/textMultimedias/{contentPath}
	 * GET /courses/{courseId}/textMultimedias/{contentPath}?UseSourceDomain=true
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param textMediaId	ID of the text media
	 * @param contentPath	Path of content
	 * @param useSourceDomain	Indicator of whether to include domain in urls
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getTextMultimediasContent(String courseId, String textMediaId, String contentPath, boolean useSourceDomain) throws IOException {
		String path = useSourceDomain ? PATH_COURSES_TEXTMULTIMEDIAS__CONTENTPATH_ : PATH_COURSES_TEXTMULTIMEDIAS__CONTENTPATH__USESOURCEDOMAIN;
		String relativeUrl = String.format(path, courseId, textMediaId, contentPath);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get specific text multimedia content by course parameter with
	 * getTextMultimedia(courseId, textMediaId)'s contentUrl 
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param textMediaId	ID of the text media
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getTextMultimediasContent(String courseId, String textMediaId) throws IOException {
		return getTextMultimediasContent(courseId,textMediaId,false);
	}
	
	/**
	 * Get specific text multimedia content by course with UseSourceDomain parameter with
	 * getTextMultimedia(courseId, textMediaId)'s contentUrl 
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param textMediaId	ID of the text media
	 * @param useSourceDomain	Indicator of whether to include domain in urls
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getTextMultimediasContent(String courseId, String textMediaId, boolean useSourceDomain) throws IOException {

		Response response = getTextMultimedia(courseId, textMediaId);
		if(response.isError()) {
			return response;
		}
		
		JsonObject json = this.jsonParser.parse(response.getContent()).getAsJsonObject();
		json = json.get("textMultimedias").getAsJsonArray().get(0).getAsJsonObject(); // single element wrapped in an array
		String contentUrl = json.get("contentUrl").getAsString();
		
		if(useSourceDomain) {
			contentUrl += "?useSourceDomain=true";
		}
		
		String relativeUrl = this.getRelativePath(contentUrl);
		response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		
		return response;
	}
	
	/**
	 * Get all MS Office documents in a course with
	 * GET /courses/{courseId}/msOfficeDocuments
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getMsOfficeDocuments(String courseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_MSOFFICEDOCUMENTS, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get a specific MS Office document in a course with
	 * GET /courses/{courseId}/msOfficeDocuments/{msOfficeDocumentId}
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param msOfficeDocumentId	ID of the ms office document
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getMsOfficeDocument(String courseId, String msOfficeDocumentId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_MSOFFICEDOCUMENTS_, courseId, msOfficeDocumentId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get content for a specific MS Office Document in a course with
	 * GET /courses/{courseId}/msOfficeDocuments/{msOfficeDocumentId}
	 * GET /courses/{courseId}/msOfficeDocuments/{msOfficeDocumentId}/content/{contentPath}
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param msOfficeDocumentId	ID of the ms office document
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getMsOfficeDocumentContent(String courseId, String msOfficeDocumentId) throws IOException {
		Response response = getMsOfficeDocument(courseId, msOfficeDocumentId);
		if(response.isError()) {
			return response;
		}
		
		JsonObject json = this.jsonParser.parse(response.getContent()).getAsJsonObject();
		json = json.get("msOfficeDocuments").getAsJsonArray().get(0).getAsJsonObject(); // single element wrapped in an array
		String contentUrl = json.get("contentUrl").getAsString();
		String relativeUrl = this.getRelativePath(contentUrl);
		response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		
		return response;
	}
	
	/**
	 * Get content for a specific MS Office Document in a course with
	 * GET /courses/{courseId}/msOfficeDocuments/{msOfficeDocumentId}
	 * GET /courses/{courseId}/msOfficeDocuments/{msOfficeDocumentId}/content/{contentPath}
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param msOfficeDocumentId	ID of the ms office document
	 * @param contentPath	Path of the content
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getMsOfficeDocumentContent(String courseId, String msOfficeDocumentId, String contentPath) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_MSOFFICEDOCUMENTS_CONTENT_, courseId, msOfficeDocumentId, contentPath);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get the original of a specific MS Office document in a course with
	 * GET /courses/{courseId}/msOfficeDocuments/{msOfficeDocumentId}/originalDocument
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param msOfficeDocumentId	ID of the ms office document
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getMsOfficeDocumentOriginal(String courseId, String msOfficeDocumentId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_MSOFFICEDOCUMENTS_ORIGINALDOCUMENT, courseId, msOfficeDocumentId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get all web content uploads in a course with
	 * GET /courses/{courseId}/webContentUploads
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getWebContentUploads(String courseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_WEBCONTENTUPLOADS, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get a specific MS Office document in a course with
	 * GET /courses/{courseId}/webContentUploads/{webContentUploadId}
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param webContentUploadId	ID of the ms office document
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getWebContentUpload(String courseId, String webContentUploadId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_WEBCONTENTUPLOADS_, courseId, webContentUploadId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get a specific MS Office document in a course with
	 * GET /courses/{courseId}/webContentUploads/{webContentUploadId}
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param webContentUploadId	ID of the ms office document
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getWebContentUploadOriginal(String courseId, String webContentUploadId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_WEBCONTENTUPLOADS_ORIGINALDOCUMENT, courseId, webContentUploadId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get content for a specific Web Content Upload in a course with
	 * GET /courses/{courseId}/webContentUpload/{webContentUploadId}
	 * GET /courses/{courseId}/webContentUpload/{webContentUploadId}/content/{contentPath}
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param webContentUploadId	ID of the web content upload
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getWebContentUploadContent(String courseId, String webContentUploadId) throws IOException {
		Response response = getWebContentUpload(courseId, webContentUploadId);
		if(response.isError()) {
			return response;
		}
		
		JsonObject json = this.jsonParser.parse(response.getContent()).getAsJsonObject();
		json = json.get("webContentUploads").getAsJsonArray().get(0).getAsJsonObject(); // single element wrapped in an array
		String contentUrl = json.get("contentUrl").getAsString();
		String relativeUrl = this.getRelativePath(contentUrl);
		response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		
		return response;
	}
	
	/**
	 * Get content for a specific Web Content Upload in a course with
	 * GET /courses/{courseId}/webContentUpload/{webContentUploadId}
	 * GET /courses/{courseId}/webContentUpload/{webContentUploadId}/content/{contentPath}
	 * using OAuth2 as a student, teacher or teaching assistant
	 * 
	 * @param courseId	ID of the course
	 * @param webContentUploadId	ID of the web content upload
	 * @param contentPath	Path of the content
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getWebContentUploadContent(String courseId, String webContentUploadId, String contentPath) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_WEBCONTENTUPLOADS_CONTENT_, courseId, webContentUploadId, contentPath);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get hierarchy of a discussion thread response with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}/responseHierarchy
	 * using OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response	
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionResponseHierarchy(String courseId, String threadId, String topicId, String responseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSEHIEARCHY, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get all user's view statuses of a discussion thread response with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/userviewresponses/{responseId}/userviewresponses
	 * using OAuth2 as a student
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response	
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionUserViewResponses(String userId, String courseId, String threadId, String topicId, String responseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_USERVIEWRESPONSES_USERVIEWRESPONSES, userId, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get all user's view statuses of a discussion thread response with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/userviewresponses/{responseId}/userviewresponses?depth={depth}
	 * using OAuth2 as a student
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response	
	 * @param depth	Number of levels to traverse
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionUserViewResponses(String userId, String courseId, String threadId, String topicId, String responseId, int depth) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_USERVIEWRESPONSES_USERVIEWRESPONSES__DEPTH, userId, courseId, threadId, topicId, responseId, depth);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get all user's view statuses of a discussion thread topic with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/userviewresponses
	 * using OAuth2 as a student
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionTopicUserViewResponses(String userId, String courseId, String threadId, String topicId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_USERVIEWRESPONSES, userId, courseId, threadId, topicId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get all user's view statuses of a discussion thread topic with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/userviewresponses?depth={depth}
	 * using OAuth2 as a student
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param depth	Number of levels to traverse
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionTopicUserViewResponses(String userId, String courseId, String threadId, String topicId, int depth) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_USERVIEWRESPONSES__DEPTH, userId, courseId, threadId, topicId, depth);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get hierarchy of a discussion thread topic with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responseHierarchy
	 * using OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic	
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionTopicHierarchy(String courseId, String threadId, String topicId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSEHIEARCHY, courseId, threadId, topicId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get count of responses for a specific response with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}/responseCounts
	 * using OAuth1 or OAuth2 as a student
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response	
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionResponseCount(String userId, String courseId, String threadId, String topicId, String responseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSECOUNTS, userId, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get count of responses for a specific response with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}/responseCounts?depth={depth}
	 * using OAuth1 or OAuth2 as a student
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response	
	 * @param depth	Number of levels to traverse
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionResponseCount(String userId, String courseId, String threadId, String topicId, String responseId, int depth) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSECOUNTS__DEPTH, userId, courseId, threadId, topicId, responseId, depth);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get count of responses for a specific topic with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responseCounts
	 * using OAuth1 or OAuth2 as a student
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionTopicResponseCount(String userId, String courseId, String threadId, String topicId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSECOUNTS, userId, courseId, threadId, topicId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get count of responses for a specific topic with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responseCounts?depth={depth}
	 * using OAuth1 or OAuth2 as a student
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param depth	Number of levels to traverse
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionTopicResponseCount(String userId, String courseId, String threadId, String topicId, int depth) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSECOUNTS__DEPTH, userId, courseId, threadId, topicId, depth);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get branch hierarchy to a discussion thread response with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}/responseBranch
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response	
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionResponseBranch(String courseId, String threadId, String topicId, String responseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSEBRANCH, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get author of a discussion thread response with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}/responseAuthor
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response	
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionResponseAuthor(String courseId, String threadId, String topicId, String responseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSEAUTHOR, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get response and author composite of a discussion thread response with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}/responseAndAuthorComps
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response	
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionResponseAndAuthorComposite(String courseId, String threadId, String topicId, String responseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSEANDAUTHORCOMPS, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	
	/**
	 * Get response and author composite for a discussion thread response at a specified depth with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}/responseAndAuthorComps?depth={depth}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response	
	 * @param depth		Max depth to traverse
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionResponseAndAuthorComposite(String courseId, String threadId, String topicId, String responseId, int depth) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSEANDAUTHORCOMPS__DEPTH, courseId, threadId, topicId, responseId, depth);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get response and author composite for a discussion thread topic with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responseAndAuthorComps/{responseId}/responseAndAuthorComps
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionTopicResponseAndAuthorComposite(String courseId, String threadId, String topicId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSEANDAUTHORCOMPS, courseId, threadId, topicId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	
	/**
	 * Get response and author composite of a discussion thread topic at a specified depth with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responseAndAuthorComps/{responseId}/responseAndAuthorComps?depth={depth}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param depth		Max depth to traverse
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionTopicResponseAndAuthorComposite(String courseId, String threadId, String topicId, int depth) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSEANDAUTHORCOMPS__DEPTH, courseId, threadId, topicId, depth);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get a user's last threaded discussion response in a course with
	 * GET /users/{userId}/courses/{courseId}/threadeddiscussions/lastResponse
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getLastThreadedDiscussionResponse(String userId, String courseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_THREADEDDISCUSSIONS__LASTRESPONSE, userId, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get threaded dicussions for a course with
	 * GET /courses/{courseId}/threadeddiscussions
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussions(String courseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get threaded dicussions for a course with
	 * GET /courses/{courseId}/threadeddiscussions?UseSourceDomain={useSourceDomain}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param useSourceDomain	Indicator of whether to use the source domain in links
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussions(String courseId, boolean useSourceDomain) throws IOException {
		if(!useSourceDomain) {
			return getThreadedDiscussions(courseId);
		}
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS__USESOURCEDOMAIN, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get threaded dicussion topics for a course with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionTopics(String courseId, String threadId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS, courseId, threadId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get threaded dicussion topics for a course with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics?UseSourceDomain={useSourceDomain}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param useSourceDomain	Indicator of whether to use the source domain in links
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionTopics(String courseId, String threadId, boolean useSourceDomain) throws IOException {
		if(!useSourceDomain) {
			return getThreadedDiscussionTopics(courseId, threadId);
		}
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS__USESOURCEDOMAIN, courseId, threadId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get threaded dicussion topics for a course with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionTopic(String courseId, String threadId, String topicId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_, courseId, threadId, topicId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get threaded dicussion topics for a course with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}?UseSourceDomain={useSourceDomain}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param useSourceDomain	Indicator of whether to use the source domain in links
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionTopic(String courseId, String threadId, String topicId, boolean useSourceDomain) throws IOException {
		if(!useSourceDomain) {
			return getThreadedDiscussionTopics(courseId, threadId);
		}
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_USESOURCEDOMAIN, courseId, threadId, topicId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get read status of a user's discussion thread response with
	 * GET /users/{userId}/courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}/readStatus
	 * using OAuth1 or OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionResponseReadStatus(String userId, String courseId, String threadId, String topicId, String responseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSE_READSTATUS, userId, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get read status of a user's discussion thread response with
	 * PUT /users/{userId}/courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}/readStatus
	 * using OAuth1 or OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response
	 * @param readStatus	Read status Message
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response updateThreadedDiscussionResponseReadStatus(String userId, String courseId, String threadId, String topicId, String responseId, String readStatus) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSE_READSTATUS, userId, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.PUT,relativeUrl, readStatus);
		return response;
	}
	
	/**
	 * Get responses to a specific discussion thread response with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}/responses
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionResponses(String courseId, String threadId, String topicId, String responseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSES, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Create a response to a specific discussion thread response with
	 * POST /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}/responses
	 * using OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response
	 * @param responseMessage	Response message to create
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response createThreadedDiscussionResponse(String courseId, String threadId, String topicId, String responseId, String responseMessage) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_RESPONSES, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.POST,relativeUrl,responseMessage);
		return response;
	}
	
	/**
	 * Create a response to a specific discussion thread topic with
	 * POST /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses
	 * using OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseMessage	Response message to create
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response createThreadedDiscussionResponse(String courseId, String threadId, String topicId, String responseMessage) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES, courseId, threadId, topicId);
		Response response = doMethod(HttpMethod.POST,relativeUrl,responseMessage);
		return response;
	}
	
	/**
	 * Update a response to a specific discussion thread response with
	 * PUT /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}
	 * using OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseID	ID of the response
	 * @param responseMessage	Response message to create
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
/*	public Response updateThreadedDiscussionResponse(String courseId, String threadId, String topicId, String responseId, String responseMessage) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.PUT,relativeUrl,responseMessage);
		return response;
	}
*/
	
	/**
	 * Get a specific discussion thread response with
	 * GET /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}
	 * using OAuth2 as a student, teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getThreadedDiscussionResponse(String courseId, String threadId, String topicId, String responseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Delete a specific discussion thread response with
	 * DELETE /courses/{courseId}/threadeddiscussions/{threadId}/topics/{topicId}/responses/{responseId}
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or admin
	 * 
	 * @param courseId	ID of the course
	 * @param threadId	ID of the thread
	 * @param topicId	ID of the topic
	 * @param responseId	ID of the response
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response deleteThreadedDiscussionResponse(String courseId, String threadId, String topicId, String responseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_THREADEDDISCUSSIONS_TOPICS_RESPONSES_, courseId, threadId, topicId, responseId);
		Response response = doMethod(HttpMethod.DELETE,relativeUrl,NO_CONTENT);
		return response;
	}
	
}
