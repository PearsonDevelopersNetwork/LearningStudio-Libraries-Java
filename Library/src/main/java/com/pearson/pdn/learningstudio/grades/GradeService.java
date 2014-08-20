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
package com.pearson.pdn.learningstudio.grades;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pearson.pdn.learningstudio.core.AbstractService;
import com.pearson.pdn.learningstudio.core.Response;
import com.pearson.pdn.learningstudio.core.ResponseStatus;
import com.pearson.pdn.learningstudio.oauth.OAuthServiceFactory;

public class GradeService extends AbstractService {
	
	// no variable between gradebook and next part, hence the double underscore to indicate the difference
	private final static String PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES = "/courses/%s/gradebook/customCategories";
	private final static String PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_ = "/courses/%s/gradebook/customCategories/%s";
	private final static String PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_CUSTOMITEMS = "/courses/%s/gradebook/customCategories/%s/customItems";
	private final static String PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_CUSTOMITEMS_ = "/courses/%s/gradebook/customCategories/%s/customItems/%s";
	private final static String PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_CUSTOMITEMS_GRADEBOOKITEM = "/courses/%s/gradebook/customCategories/%s/customItems/%s/gradebookItem";
	private final static String PATH_COURSES_GRADEBOOKITEMS = "/courses/%s/gradebookItems";
	private final static String PATH_COURSES_GRADEBOOKITEMS_ = "/courses/%s/gradebookItems/%s";
	private final static String PATH_COURSES_GRADEBOOK__GRADEBOOKITEMS_ = "/courses/%s/gradebook/gradebookItems/%s";
	private final static String PATH_COURSES_GRADEBOOKITEMS_GRADES = "/courses/%s/gradebookItems/%s/grades";
	private final static String PATH_COURSES_GRADEBOOKITEMS_GRADES_ = "/courses/%s/gradebookItems/%s/grades/%s";
	private final static String PATH_USERS_COURSES_GRADEBOOKITEMS_GRADE = "/users/%s/courses/%s/gradebookItems/%s/grade";
	private final static String PATH_USERS_COURSES_USERGRADEBOOKITEMS = "/users/%s/courses/%s/userGradebookItems";
	private final static String PATH_USERS_COURSES_USERGRADEBOOKITEMS__EXPANDGRADE = PATH_USERS_COURSES_USERGRADEBOOKITEMS+"?expand=grade";
	private final static String PATH_USERS_COURSES_USERGRADEBOOKITEMS__USESOURCEDOMAIN = PATH_USERS_COURSES_USERGRADEBOOKITEMS+"?UseSourceDomain=true";
	private final static String PATH_USERS_COURSES_USERGRADEBOOKITEMS__USESOURCEDOMAIN_EXPANDGRADE = PATH_USERS_COURSES_USERGRADEBOOKITEMS+"?UseSourceDomain=true&expand=grade";
	private final static String PATH_USERS_COURSES_GRADEBOOK__GRADEBOOKITEMS_GRADE = "/users/%s/courses/%s/gradebook/gradebookItems/%s/grade";
	private final static String PATH_USERS_COURSES_GRADEBOOK__GRADEBOOKITEMS_GRADE__USESOURCEDOMAIN = "/users/%s/courses/%s/gradebook/gradebookItems/%s/grade?UseSourceDomain=true";
	private final static String PATH_USERS_COURSES_COURSEGRADETODATE = "/users/%s/courses/%s/coursegradetodate";
	private final static String PATH_COURSES_GRADEBOOK__ROSTERCOURSEGRADESTODATE__STUDENTIDS_ = "/courses/%s/gradebook/rostercoursegradestodate?Student.ID=%s";
	private final static String PATH_COURSES_GRADEBOOK__ROSTERCOURSEGRADESTODATE__OFFSET_LIMIT_ = "/courses/%s/gradebook/rostercoursegradestodate?offset=%s&limit=%s";
	private final static String PATH_COURSES_GRADEBOOK__ROSTERCOURSEGRADESTODATE__STUDENTIDS_OFFSET_LIMIT_ = "/courses/%s/gradebook/rostercoursegradestodate?Student.ID=%s&offset=%s&limit=%s";
	private final static String PATH_USERS_COURSES_GRADEBOOK__USERGRADEBOOKITEMS = "/users/%s/courses/%s/gradebook/userGradebookItems";
	private final static String PATH_USERS_COURSES_GRADEBOOK__USERGRADEBOOKITEMS_ = "/users/%s/courses/%s/gradebook/userGradebookItems/%s";
	private final static String PATH_USERS_COURSES_GRADEBOOK__USERGRADEBOOKITEMS_EXPANDGRADE = PATH_USERS_COURSES_GRADEBOOK__USERGRADEBOOKITEMS_+"?expand=grade";
	private final static String PATH_USERS_COURSES_GRADEBOOK__USERGRADEBOOKITEMSTOTAL = "/users/%s/courses/%s/gradebook/userGradebookItemsTotals";
	//private final static String PATH_COURSES_ITEMS_GRADEBOOKITEM = "/courses/%s/items/%s/gradebookItem";

	/**
	 * Constructs a new GradeService
	 * 
	 * @param oauthServiceFactory Service provider for OAuth operations
	 */
	public GradeService(OAuthServiceFactory oauthServiceFactory) {
		super(oauthServiceFactory);
	}
	
	/**
	 * Provides name of service for identification purposes
	 * 
	 * @return Unique identifier for service
	 */
	protected String getServiceIdentifier() {
		return "LS-Library-Grade-Java-V1";
	}
	
	/**
	 * Create custom category and item with
	 * POST /courses/{courseId}/gradebook/customCategories
	 * POST /courses/{courseId}/gradebook/customCategories/{customCategoryId}/customItems
	 * using OAuth1 or OAuth2 as a teacher, teaching assistance or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param customCategory	Custom category to create
	 * @param customItem	Custom item to create
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response createCustomGradebookCategoryAndItem(String courseId, String customCategory, String customItem) throws IOException {
		JsonObject customCategoryObject = jsonParser.parse(customCategory).getAsJsonObject();
		
		 // required for item, so the customCategory should already be that way.
		//customCategoryObject.get("customCategories").getAsJsonObject().addProperty("isAssignable", true);
		
		Response response = createCustomGradebookCategory(courseId, gson.toJson(customCategoryObject));
		if(response.isError()) {
			return response;
		}
		
		customCategoryObject = jsonParser.parse(response.getContent()).getAsJsonObject();
		customCategoryObject = customCategoryObject.get("customCategory").getAsJsonObject();
		String customCategoryId = customCategoryObject.get("guid").getAsString();
		
		response = createCustomGradebookItem(courseId, customCategoryId, customItem);
		if(response.isError()) {
			return response;
		}
		
		JsonObject customItemObject = jsonParser.parse(response.getContent()).getAsJsonObject();
		customItemObject = customItemObject.get("customItem").getAsJsonObject();
		
		JsonObject wrapper = new JsonObject();
		wrapper.add("customCategory", customCategoryObject);
		wrapper.add("customItem", customItemObject);
		response.setContent(gson.toJson(wrapper));
		
		return response;
	}
	
	/**
	 * Create custom gradebook category for a course with
	 * POST /courses/{courseId}/gradebook/customCategories
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param customCategory	Custom category to create
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response createCustomGradebookCategory(String courseId, String customCategory) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES, courseId);
		Response response = doMethod(HttpMethod.POST,relativeUrl,customCategory);
		return response;
	}
	
	/**
	 * Create custom gradebook category for a course with
	 * PUT /courses/{courseId}/gradebook/customCategories/{customCategoryId}
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param customCategoryId	ID of the custom category
	 * @param customCategory	Custom category to create
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response updateCustomGradebookCategory(String courseId, String customCategoryId, String customCategory) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_, courseId, customCategoryId);
		Response response = doMethod(HttpMethod.PUT,relativeUrl,customCategory);
		return response;
	}
	
	/**
	 * Delete custom gradebook category for a course with
	 * DELETE /courses/{courseId}/gradebook/customCategories/{customCategoryId}
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param customCategoryId	ID of the custom category
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response deleteCustomGradebookCategory(String courseId, String customCategoryId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_, courseId, customCategoryId);
		Response response = doMethod(HttpMethod.DELETE,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get custom gradebook category for a course with
	 * GET /courses/{courseId}/gradebook/customCategories/{customCategoryId}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param customCategoryId	ID of the custom category
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCustomGradebookCategory(String courseId, String customCategoryId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_, courseId, customCategoryId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Create custom gradebook item in a custom category for a course with
	 * POST /courses/{courseId}/gradebook/customCategories/{customCategoryId}/customItems
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param customCategoryId	ID of the custom category
	 * @param customItem	Custom item to create
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response createCustomGradebookItem(String courseId, String customCategoryId, String customItem) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_CUSTOMITEMS, courseId, customCategoryId);
		Response response = doMethod(HttpMethod.POST,relativeUrl,customItem);
		return response;
	}
	
	/* 404 - PUT route does not exists
	public Response updateCustomGradebookItem(String courseId, String customCategoryId, String customItemId, String customItem) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_CUSTOMITEMS_, courseId, customCategoryId, customItemId);
		Response response = doMethod(HttpMethod.PUT,relativeUrl,customItem);
		return response;
	}
	*/
	
	/**
	 * Delete custom gradebook item in a custom category for a course with
	 * DELETE /courses/{courseId}/gradebook/customCategories/{customCategoryId}/customItems/{customItemId}
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param customCategoryId	ID of the custom category
	 * @param customItemId	ID of the custom item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response deleteCustomGradebookItem(String courseId, String customCategoryId, String customItemId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_CUSTOMITEMS_, courseId, customCategoryId, customItemId);
		Response response = doMethod(HttpMethod.DELETE,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get custom item in a custom gradebook category for a course with
	 * GET /courses/{courseId}/gradebook/customCategories/{customCategoryId}/customItems/{customItemId}
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param customCategoryId	ID of the custom category
	 * @param customItemId	ID of the custom item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getGradebookCustomItem(String courseId, String customCategoryId, String customItemId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_CUSTOMITEMS_, courseId, customCategoryId, customItemId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get custom gradebook item in a custom category for a course with
	 * GET /courses/{courseId}/gradebook/customCategories/{customCategoryId}/customItems/{customItemId}
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param customCategoryId	ID of the custom category
	 * @param customItemId	ID of the custom item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCustomGradebookItem(String courseId, String customCategoryId, String customItemId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_CUSTOMITEMS_GRADEBOOKITEM, courseId, customCategoryId, customItemId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get gradebook items for a course with
	 * GET /courses/{courseId}/gradebookItems
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getGradebookItems(String courseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOKITEMS, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get specific gradebook item for a course with
	 * GET /courses/{courseId}/gradebookItems/{gradebookItemId}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getGradebookItem(String courseId, String gradebookItemId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOKITEMS_, courseId, gradebookItemId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Create specific gradebook item for a course with
	 * POST /courses/{courseId}/gradebook/gradebookItems/{gradebookItemId}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @param gradebookItem		Details of gradebook item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
/*	public Response createGradebookItem(String courseId, String gradebookItemId, String gradebookItem) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__GRADEBOOKITEMS_, courseId, gradebookItemId);
		Map<String, String> extraHeaders = new HashMap<String,String>();
		extraHeaders.put("X-METHOD-OVERRIDE", "PUT");
		Response response = doMethod(extraHeaders, HttpMethod.POST,relativeUrl,gradebookItem);
		return response;
	}
*/ 	// not working...
	
	/**
	 * Update specific gradebook item for a course with
	 * PUT /courses/{courseId}/gradebook/gradebookItems/{gradebookItemId}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @param gradebookItem		Details of gradebook item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response updateGradebookItem(String courseId, String gradebookItemId, String gradebookItem) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__GRADEBOOKITEMS_, courseId, gradebookItemId);
		Response response = doMethod(HttpMethod.PUT,relativeUrl,gradebookItem);
		return response;
	}
	
	/**
	 * Get grades for specific gradebook item in a course with
	 * GET /courses/{courseId}/gradebookItems/{gradebookItemId}/grades
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getGradebookItemGrades(String courseId, String gradebookItemId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOKITEMS_GRADES, courseId, gradebookItemId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get grades for specific gradebook item in a course using parameters with
	 * GET /courses/{courseId}/gradebookItems/{gradebookItemId}/grades?gradedStudents={gradedStudentIds}&useSourceDomains=true&expand=user
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @param gradedStudentIds	ID of students (semicolon separated)
	 * @param useSourceDomain	Indicator of whether to include domains in urls
	 * @param expandUser		Indicator of whether to expand user info 
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getGradebookItemGrades(String courseId, String gradebookItemId, 
											String gradedStudentIds, boolean useSourceDomain, boolean expandUser) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOKITEMS_GRADES, courseId, gradebookItemId);
		if(gradedStudentIds!=null || useSourceDomain || expandUser) {
			relativeUrl += "?";
			boolean firstParameter = true;
			if(gradedStudentIds!=null) {
				relativeUrl += "gradedStudents="+gradedStudentIds;
				firstParameter=false;
			}
			if(useSourceDomain) {
				if(!firstParameter) {
					relativeUrl += "&";
				}
				relativeUrl += "UseSourceDomain=true";
				firstParameter=false;
			}
			if(expandUser) {
				if(!firstParameter) {
					relativeUrl += "&";
				}
				relativeUrl += "expand=user";
				firstParameter=false;				
			}
		}
		
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get specific grade for an item in a course with
	 * GET /courses/{courseId}/gradebookItems/{gradebookItemId}/grades/{gradeId}
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getGradebookItemGrade(String courseId, String gradebookItemId, String gradeId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOKITEMS_GRADES_, courseId, gradebookItemId, gradeId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get specific grade for an item in a course using parameters with
	 * GET /courses/{courseId}/gradebookItems/{gradebookItemId}/grades/{gradeId}?gradedStudents={gradedStudentIds}&useSourceDomains=true&expand=user
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @param gradeId	ID of the grade within the gradebook
	 * @param gradedStudentIds	ID of students (semicolon separated)
	 * @param useSourceDomain	Indicator of whether to include domains in urls
	 * @param expandUser		Indicator of whether to expand user info 
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getGradebookItemGrade(String courseId, String gradebookItemId, String gradeId,  
											String gradedStudentIds, boolean useSourceDomain, boolean expandUser) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOKITEMS_GRADES_, courseId, gradebookItemId, gradeId);
		if(gradedStudentIds!=null || useSourceDomain || expandUser) {
			relativeUrl += "?";
			boolean firstParameter = true;
			if(gradedStudentIds!=null) {
				relativeUrl += "gradedStudents="+gradedStudentIds;
				firstParameter=false;
			}
			if(useSourceDomain) {
				if(!firstParameter) {
					relativeUrl += "&";
				}
				relativeUrl += "UseSourceDomain=true";
				firstParameter=false;
			}
			if(expandUser) {
				if(!firstParameter) {
					relativeUrl += "&";
				}
				relativeUrl += "expand=user";
				firstParameter=false;				
			}
		}
		
		
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Create user's grade for an item in a course with
	 * POST /users/{userId}/courses/{courseId}/gradebookItems/{gradebookItemId}/grade
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @param grade	Grade content to be created
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response createGradebookItemGrade(String userId, String courseId, String gradebookItemId, String grade) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_GRADEBOOKITEMS_GRADE, userId, courseId, gradebookItemId);
		Response response = doMethod(HttpMethod.POST,relativeUrl,grade);
		return response;
	}
	
	/**
	 * Update user's grade for an item in a course with
	 * PUT /users/{userId}/courses/{courseId}/gradebookItems/{gradebookItemId}/grade
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @param grade	Grade content to be updated
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response updateGradebookItemGrade(String userId, String courseId, String gradebookItemId, String grade) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_GRADEBOOKITEMS_GRADE, userId, courseId, gradebookItemId);
		Response response = doMethod(HttpMethod.PUT,relativeUrl,grade);
		return response;
	}
	
	/**
	 * Delete user's grade for an item in a course with
	 * DELETE /users/{userId}/courses/{courseId}/gradebookItems/{gradebookItemId}/grade
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response deleteGradebookItemGrade(String userId, String courseId, String gradebookItemId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_GRADEBOOKITEMS_GRADE, userId, courseId, gradebookItemId);
		Response response = doMethod(HttpMethod.DELETE, relativeUrl, NO_CONTENT);
		return response;
	}

	/**
	 * Get specific gradebook item for a course with
	 * GET /courses/{courseId}/gradebookItems/{gradebookItemId}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param itemId	ID of the gradebook item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	/*
	public Response getGradebookItemByItem(String courseId, String itemId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_ITEMS_GRADEBOOKITEM, courseId, itemId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	*/
	
	/**
	 * Get gradebook items for a user in a course with
	 * GET /users/{userId}/courses/{courseId}/userGradebookItems
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getUserGradebookItems(String userId, String courseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_USERGRADEBOOKITEMS, userId, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get gradebook items for a user in a course with
	 * GET /users/{userId}/courses/{courseId}/userGradebookItems
	 * with optional useSourceDomain and expand parameters
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param useSourceDomain	Flag for using source domain parameter
	 * @param expandGrade	Flag for using expand grade parameter
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getUserGradebookItems(String userId, String courseId, boolean useSourceDomain, boolean expandGrade) throws IOException {
		String path = PATH_USERS_COURSES_USERGRADEBOOKITEMS;
		if(useSourceDomain || expandGrade) {
			if(useSourceDomain && expandGrade) {
				path = PATH_USERS_COURSES_USERGRADEBOOKITEMS__USESOURCEDOMAIN_EXPANDGRADE;
			}
			else if(useSourceDomain) {
				path = PATH_USERS_COURSES_USERGRADEBOOKITEMS__USESOURCEDOMAIN;
			}
			else  {
				path = PATH_USERS_COURSES_USERGRADEBOOKITEMS__EXPANDGRADE;
			}
		}
		
		String relativeUrl = String.format(path, userId, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Create a user's grade for an item in a course with
	 * POST /users/{userId}/courses/{courseId}/gradebook/gradebookItems/{gradebookItemId}/grade
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @param grade		Grade on the exam
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response createGrade(String userId, String courseId, String gradebookItemId, String grade) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_GRADEBOOK__GRADEBOOKITEMS_GRADE, userId, courseId, gradebookItemId);
		Response response = doMethod(HttpMethod.POST,relativeUrl,grade);
		return response;
	}
	
	/**
	 * Update a user's grade for an item in a course with
	 * PUT /users/{userId}/courses/{courseId}/gradebook/gradebookItems/{gradebookItemId}/grade
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @param grade		Grade on the exam
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response updateGrade(String userId, String courseId, String gradebookItemId, String grade) throws IOException  {
		String relativeUrl = String.format(PATH_USERS_COURSES_GRADEBOOK__GRADEBOOKITEMS_GRADE, userId, courseId, gradebookItemId);
		Response response = doMethod(HttpMethod.PUT,relativeUrl,grade);
		return response;
	}
	
	/**
	 * Delete a user's grade for an item in a course with
	 * DELETE /users/{userId}/courses/{courseId}/gradebook/gradebookItems/{gradebookItemId}/grade
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response deleteGrade(String userId, String courseId, String gradebookItemId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_GRADEBOOK__GRADEBOOKITEMS_GRADE, userId, courseId, gradebookItemId);
		Response response = doMethod(HttpMethod.DELETE,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get a user's grade for an item in a course with
	 * GET /users/{userId}/courses/{courseId}/gradebook/gradebookItems/{gradebookItemId}/grade
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getGrade(String userId, String courseId, String gradebookItemId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_GRADEBOOK__GRADEBOOKITEMS_GRADE, userId, courseId, gradebookItemId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get a user's grade for an item in a course with override for useSourceDomain with
	 * GET /users/{userId}/courses/{courseId}/gradebook/gradebookItems/{gradebookItemId}/grade
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param gradebookItemId	ID of the gradebook item
	 * @param useSourceDomain	Indicator of whether to include domain in urls
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getGrade(String userId, String courseId, String gradebookItemId, boolean useSourceDomain) throws IOException {
		String path = useSourceDomain ? PATH_USERS_COURSES_GRADEBOOK__GRADEBOOKITEMS_GRADE__USESOURCEDOMAIN : PATH_USERS_COURSES_GRADEBOOK__GRADEBOOKITEMS_GRADE;
		String relativeUrl = String.format(path, userId, courseId, gradebookItemId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get a user's grades for a course with
	 * GET /users/{userId}/courses/{courseId}/gradebook/userGradebookItems
	 * and getGrade(String userId, String courseId, String gradebookItemId, false)
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getGrades(String userId, String courseId) throws IOException {
		return getGrades(userId, courseId,false);
	}
	
	/**
	 * Get a user's grades for a course with
	 * GET /users/{userId}/courses/{courseId}/gradebook/userGradebookItems
	 * and getGrade(String userId, String courseId, String gradebookItemId, boolean useSourceDomain)
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param useSourceDomain	Indicator of whether to include domain in urls
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getGrades(String userId, String courseId, boolean useSourceDomain) throws IOException {
		Response response = getCourseGradebookUserItems(userId, courseId);
		if(response.isError()) {
			return response;
		}
		
		JsonArray grades = new JsonArray();
		
		JsonElement json = this.jsonParser.parse(response.getContent());
		JsonArray items = json.getAsJsonObject().get("userGradebookItems").getAsJsonArray();
		for(Iterator<JsonElement> itemIter = items.iterator(); itemIter.hasNext();) {
			JsonObject item = itemIter.next().getAsJsonObject();
			String gradebookItemId = item.get("gradebookItem").getAsJsonObject().get("id").getAsString();
			Response gradeResponse = getGrade(userId,courseId,gradebookItemId,useSourceDomain);
			if(gradeResponse.isError()) {
				// grades for all items might not exist. That's ok
				if(gradeResponse.getStatusCode()==ResponseStatus.NOT_FOUND.code()) {
					continue;
				}
				return gradeResponse;
			}
			JsonObject grade = this.jsonParser.parse(gradeResponse.getContent()).getAsJsonObject();
			grade = grade.get("grade").getAsJsonObject();
			grades.add(grade);
		}
		
		JsonObject gradesWrapper = new JsonObject();
		gradesWrapper.add("grades",grades);

		response.setContent(this.gson.toJson(gradesWrapper));
		return response;
	}
	
	/**
	 * Get a user's current grades for a course with
	 * GET /users/{userId}/courses/{courseId}/coursegradetodate
	 * using OAuth1 or Auth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCurrentGrade(String userId, String courseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_COURSEGRADETODATE, userId, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get current grades for all students in a course with
	 * GET /courses/{courseId}/gradebook/rostercoursegradestodate?offset={offset}&limit={limit}
	 * using OAuth1 or Auth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of course
	 * @param offset	Offset position
	 * @param limit		Limitation on count of records
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCurrentGrades(String courseId, int offset, int limit) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__ROSTERCOURSEGRADESTODATE__OFFSET_LIMIT_, courseId, String.valueOf(offset), String.valueOf(limit));
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get current grades for specific students in a course with
	 * GET /courses/{courseId}/gradebook/rostercoursegradestodate?Student.ID={studentIds}&offset={offset}&limit={limit}
	 * using OAuth1 or Auth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of course
	 * @param studentIds Comma-separated list of students to filter 
	 * @param offset	Offset position
	 * @param limit		Limitation on count of records
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCurrentGrades(String courseId, String studentIds, int offset, int limit) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__ROSTERCOURSEGRADESTODATE__STUDENTIDS_OFFSET_LIMIT_, courseId, studentIds, String.valueOf(offset), String.valueOf(limit));
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get current grades for specific students in a course with
	 * GET /courses/{courseId}/gradebook/rostercoursegradestodate?Student.ID={studentIds}
	 * using OAuth1 or Auth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of course
	 * @param studentIds Comma-separated list of students to filter 
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCurrentGrades(String courseId, String studentIds) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__ROSTERCOURSEGRADESTODATE__STUDENTIDS_, courseId, studentIds);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get user gradebook items in a course gradebook with
	 * GET /users/{userId}/courses/{courseId}/gradebook/userGradebookItems
	 * using OAuth1 or Auth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCourseGradebookUserItems(String userId, String courseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_GRADEBOOK__USERGRADEBOOKITEMS, userId, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get user gradebook item in a course gradebook by user gradebook item id with
	 * GET /users/{userId}/courses/{courseId}/gradebook/userGradebookItems/{userGradebookItemId}
	 * using OAuth1 or Auth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param userGradebookItemId concatenation of {userId}-{gradebookItemGuid}
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCourseGradebookUserItem(String userId, String courseId, String userGradebookItemId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_GRADEBOOK__USERGRADEBOOKITEMS_, userId, courseId, userGradebookItemId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get user gradebook item in a course gradebook by user gradebook item id with
	 * GET /users/{userId}/courses/{courseId}/gradebook/userGradebookItems/{userGradebookItem}
	 * or GET /users/{userId}/courses/{courseId}/gradebook/userGradebookItems/{userGradebookItem}?expandGrade=true
	 * using OAuth1 or Auth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param userGradebookItemId concatenation of {userId}-{gradebookItemGuid}
	 * @param expandGrade	Flag of whether to expand grade data
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCourseGradebookUserItem(String userId, String courseId, String userGradebookItemId, boolean expandGrade) throws IOException {
		String path = expandGrade ? PATH_USERS_COURSES_GRADEBOOK__USERGRADEBOOKITEMS_EXPANDGRADE : PATH_USERS_COURSES_GRADEBOOK__USERGRADEBOOKITEMS_;
		String relativeUrl = String.format(path, userId, courseId, userGradebookItemId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get summary of points available to a student in a course with
	 * GET /users/{userId}/courses/{courseId}/gradebook/userGradebookItemsTotals
	 * using OAuth1 or Auth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getTotalPointsAvailable(String userId, String courseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_GRADEBOOK__USERGRADEBOOKITEMSTOTAL, userId, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	
	/**
	 * Get custom categories in a course's gradebook with
	 * GET /courses/{courseId}/gradebook/customCategories
	 * using OAuth1 or Auth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCustomGradebookCategories(String courseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Get custom items in a custom category of a course's gradebook with
	 * GET /courses/{courseId}/gradebook/customCategories/{customCategoryId}/customItems
	 * using OAuth1 or Auth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @param customCategoryId	ID of a custom category
	 * @return	Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCustomGradebookItems(String courseId, String customCategoryId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_GRADEBOOK__CUSTOMCATEGORIES_CUSTOMITEMS, courseId, customCategoryId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
}
