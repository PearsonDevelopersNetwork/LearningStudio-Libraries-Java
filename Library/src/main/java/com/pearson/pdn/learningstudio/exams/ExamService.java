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
package com.pearson.pdn.learningstudio.exams;

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

/**
 * Service for interacting with the exams module of LearningStudio
 *
 */
public class ExamService extends AbstractService {
		
	// RELATION CONSTANTS
	private final static String RELS_USER_COURSE_EXAM = "https://api.learningstudio.com/rels/user/course/exam";
	
	// PATH CONSTANTS
	private final static String PATH_USERS_COURSES_ITEMS = "/users/%s/courses/%s/items";
	private final static String PATH_USERS_COURSES_EXAMS = "/users/%s/courses/%s/exams";
	private final static String PATH_USERS_COURSES_EXAMS_ = "/users/%s/courses/%s/exams/%s";
	private final static String PATH_USERS_COURSES_EXAMS_ATTEMPTS = "/users/%s/courses/%s/exams/%s/attempts";
	private final static String PATH_USERS_COURSES_EXAMS_ATTEMPTS_ = "/users/%s/courses/%s/exams/%s/attempts/%s";
	private final static String PATH_USERS_COURSES_EXAMS_ATTEMPTS_SUMMARY = "/users/%s/courses/%s/exams/%s/attempts/%s/summary";
	private final static String PATH_USERS_COURSES_EXAMDETAILS = "/users/%s/courses/%s/examDetails";
	private final static String PATH_USERS_COURSES_EXAMDETAILS_ = "/users/%s/courses/%s/examDetails/%s";
	private final static String PATH_COURSES_EXAMSCHEDULES = "/courses/%s/examSchedules";
	
	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS = "/users/%s/courses/%s/exams/%s/sections";
	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_QUESTIONS = "/users/%s/courses/%s/exams/%s/sections/%s/questions";
	
	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_TRUEFALSE_ = "/users/%s/courses/%s/exams/%s/sections/%s/trueFalseQuestions/%s";
	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_TRUEFALSE_CHOICES = "/users/%s/courses/%s/exams/%s/sections/%s/trueFalseQuestions/%s/choices";
	
	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_MULTIPLECHOICE_ = "/users/%s/courses/%s/exams/%s/sections/%s/multipleChoiceQuestions/%s";
	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_MULTIPLECHOICE_CHOICES = "/users/%s/courses/%s/exams/%s/sections/%s/multipleChoiceQuestions/%s/choices";

	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_MANYMULTIPLECHOICE_ = "/users/%s/courses/%s/exams/%s/sections/%s/manyMultipleChoiceQuestions/%s";
	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_MANYMULTIPLECHOICE_CHOICES = "/users/%s/courses/%s/exams/%s/sections/%s/manyMultipleChoiceQuestions/%s/choices";

	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_MATCHING_ = "/users/%s/courses/%s/exams/%s/sections/%s/matchingQuestions/%s";
	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_MATCHING_PREMISES = "/users/%s/courses/%s/exams/%s/sections/%s/matchingQuestions/%s/premises";
	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_MATCHING_CHOICES = "/users/%s/courses/%s/exams/%s/sections/%s/matchingQuestions/%s/choices";

	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_SHORT_ = "/users/%s/courses/%s/exams/%s/sections/%s/shortQuestions/%s";
	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_ESSAY_ = "/users/%s/courses/%s/exams/%s/sections/%s/essayQuestions/%s";
	private final static String PATH_USERS_COURSES_EXAMS_SECTIONS_FILLINTHEBLANK_ = "/users/%s/courses/%s/exams/%s/sections/%s/fillintheblankQuestions/%s";

	private final static String PATH_USERS_COURSES_EXAMS_ATTEMPTS_ANSWERS = "/users/%s/courses/%s/exams/%s/attempts/%s/answers";
    private final static String PATH_USERS_COURSES_EXAMS_ATTEMPTS_ANSWERS_ = "/users/%s/courses/%s/exams/%s/attempts/%s/answers/%s";

    // EXAM Constants
    private final static String PEARSON_EXAM_TOKEN = "Pearson-Exam-Token";
    private final static String PEARSON_EXAM_PASSWORD = "Pearson-Exam-Password";
    
	private enum QuestionType {
	    TRUE_FALSE("trueFalse"),
	    MULTIPLE_CHOICE("multipleChoice"),
	    MANY_MULTIPLE_CHOICE("manyMultipleChoice"),
	    MATCHING("matching"),
	    SHORT_ANSWER("short"),
	    ESSAY("essay"),
	    FILL_IN_THE_BLANK("fillInTheBlank");
	    
	    private String value;
	    
	    private QuestionType(String value) {
	    	this.value = value;
	    }
	    
	    public String value() {
	    	return this.value;
	    }
	    
	    public static QuestionType matchesValue(String value) {
	    	for(QuestionType qt : QuestionType.values()) {
	    		if(qt.value.equalsIgnoreCase(value)){
	    			return qt;
	    		}
	    	}
	    	
	    	return null;
	    }
	}
	
	/**
	 * Constructs a new ExamService
	 * 
	 * @param oauthServiceFactory Service provider for OAuth operations
	 */
	public ExamService(OAuthServiceFactory oauthServiceFactory) {
		super(oauthServiceFactory);
	}
	
	/**
	 * Provides name of service for identification purposes
	 * 
	 * @return Unique identifier for service
	 */
	protected String getServiceIdentifier() {
		return "LS-Library-Exam-Java-V1";
	}
	
	/**
	 * Retrieve all of a user's exams for a course with
	 * GET /users/{userId}/courses/{courses}/items
	 * using OAuth1 or OAuth2 as a student or teacher
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of course
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getAllExamItems(String userId, String courseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_ITEMS,userId,courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		if(response.isError()) {
			return response;
		}
		
		String courseItemsJson = response.getContent();
		JsonObject json = this.jsonParser.parse(courseItemsJson).getAsJsonObject();
		JsonArray items = json.get("items").getAsJsonArray();
		JsonArray exams = new JsonArray();
		
		for(Iterator<JsonElement> itemIter = items.iterator(); itemIter.hasNext();) {
			JsonObject item = itemIter.next().getAsJsonObject();
			JsonArray links = item.get("links").getAsJsonArray();
			
			for(Iterator<JsonElement> linkIter = links.iterator(); linkIter.hasNext();) {
				JsonObject link = linkIter.next().getAsJsonObject();
				if(RELS_USER_COURSE_EXAM.equals(link.get("rel").getAsString())) {
					exams.add(item);
					break;
				}
			}
		}
		
		JsonObject examItems = new JsonObject();
		examItems.add("items",exams);

		response.setContent(this.gson.toJson(examItems));
		return response;
	}
	
	/**
	 * Retrieve all of a user's existing exams for a course with 
	 * GET /users/{userId}/courses/{courseId}/exams
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getExistingExams(String userId, String courseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS,userId,courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Retrieve details for all exams for a course with 
	 * GET /users/{userId}/courses/{courseId}/exams
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getExamDetails(String userId, String courseId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMDETAILS,userId,courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Retrieve details for all exams for a course with 
	 * GET /users/{userId}/courses/{courseId}/exams
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getExamDetails(String userId, String courseId, String examId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMDETAILS_,userId,courseId,examId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Retrieve exam schedules for a course with 
	 * GET /courses/{courseId}/examschedules
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getExamSchedules(String courseId) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_EXAMSCHEDULES, courseId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Retrieve exam schedules for a course with 
	 * POST /courses/{courseId}/examschedules
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
/*	public Response createExamSchedules(String courseId, String examSchedules) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_EXAMSCHEDULES, courseId);
		Response response = doMethod(HttpMethod.POST,relativeUrl,examSchedules);
		return response;
	}
*/
	
	/**
	 * Retrieve exam schedules for a course with 
	 * POST /courses/{courseId}/examschedules
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param courseId	ID of the course
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
/*	public Response updateExamSchedules(String courseId, String examSchedules) throws IOException {
		String relativeUrl = String.format(PATH_COURSES_EXAMSCHEDULES, courseId);
		Response response = doMethod(HttpMethod.PUT,relativeUrl,examSchedules);
		return response;
	}
*/
	
	/**
	 * Retrieve a user's exam for a course with
	 * GET /users/{userId}/courses/{courseId}/exams/{examId}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course	
	 * @param examId	ID of the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getExistingExam(String userId, String courseId, String examId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_,userId,courseId,examId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}

	/**
	 * Creates an exam for a user in a course with 
	 * POST /users/userId/courses/{courseId}/exams/{examId}
	 * using OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response createUserExam(String userId, String courseId, String examId) throws IOException {
		Response response = getExistingExam(userId,courseId,examId);
		if(response.getStatusCode() == ResponseStatus.NOT_FOUND.code()) {
			String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_,userId,courseId,examId);
			response = doMethod(HttpMethod.POST,relativeUrl,NO_CONTENT);
		}
		return response;
	}
	
	/**
	 * Updates an exam for a user in a course with 
	 * PUT /users/userId/courses/{courseId}/exams/{examId}
	 * using OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
/*	public Response updateUserExam(String userId, String courseId, String examId) throws IOException {
		Response response = getExistingExam(userId,courseId,examId);
		if(response.getStatusCode() == ResponseStatus.OK.code()) {
			String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_,userId,courseId,examId);
			**FIX** response = doMethod(HttpMethod.PUT,relativeUrl,"{\"exam\":{\"id\":53967560,\"title\":\"test\",\"description\":\"\" }");
		}
		return response;
	}
*/
	
	/**
	 * Delete a users's exam in a course with 
	 * DELETE /users/{userId}/courses/{courseId}/exams/{examId}
	 * using OAuth1 or OAuth2 as a teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course	
	 * @param examId	ID of the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response deleteUserExam(String userId, String courseId, String examId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_,userId,courseId,examId);
		Response response = doMethod(HttpMethod.DELETE,relativeUrl,NO_CONTENT);
		return response;
	}

	/**
	 * Create an exam attempt for a user in a course with 
	 * POST /users/{userId}/courses/{courseId}/exams/{examId}/attempts
	 * using OAuth1 or OAuth2 as student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response createExamAttempt(String userId, String courseId, String examId) throws IOException {
		
		return createExamAttempt(userId, courseId, examId, null);
	}
	
	/**
	 * Create an exam attempt for a user in a course with 
	 * POST /users/{userId}/courses/{courseId}/exams/{examId}/attempts
	 * using OAuth1 or OAuth2
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @param examPassword	Optional password from instructor
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response createExamAttempt(String userId, String courseId, String examId,String examPassword) throws IOException {
		
		Map<String,String> examHeaders = null;
		if(examPassword != null) {
			examHeaders = new HashMap<String,String>();
			examHeaders.put(PEARSON_EXAM_PASSWORD, examPassword);
		}
		
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_ATTEMPTS,userId,courseId,examId);
		Response response = doMethod(examHeaders, HttpMethod.POST,relativeUrl,NO_CONTENT);
		return response;
	}

	/**
	 * Retrieve a users's attempt of an exam in a course with 
	 * GET /users/{userId}/courses/{courseId}/exams/{examId}/attempts
	 * using OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getExamAttempts(String userId, String courseId, String examId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_ATTEMPTS,userId,courseId,examId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}
	
	/**
	 * Retrieve a user's attempt of an exam in a course with 
	 * GET /users/{userId}/courses/{courseId}/exams/{examId}/attempts/{attemptId}
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @param attemptId	ID of the exam attempt
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getExamAttempt(String userId, String courseId, String examId, String attemptId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_ATTEMPTS_,userId,courseId,examId,attemptId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}

	/**
	 *  Retrieves and filters a user's current attempt of an exam in a course with 
	 *  GET /users/{userId}/courses/{courseId}/exams/{examId}/attempts
	 *  using OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getCurrentExamAttempt(String userId, String courseId, String examId) throws IOException {
		Response response = getExamAttempts(userId,courseId,examId);
		if(response.isError()) {
			return response;
		}
		String attemptsJson = response.getContent();
		JsonElement json = jsonParser.parse(attemptsJson);
		JsonArray attempts = json.getAsJsonObject().get("attempts").getAsJsonArray();
		JsonObject currentAttempt = null;
		
		for(Iterator<JsonElement> attemptsIterator = attempts.iterator();attemptsIterator.hasNext();) {
			JsonObject attempt = attemptsIterator.next().getAsJsonObject();
			if(!attempt.get("isCompleted").getAsBoolean()) {
				currentAttempt = attempt;
				break;
			}
		}
		
		if(currentAttempt != null) {
			JsonObject attempt = new JsonObject();
			attempt.add("attempt",currentAttempt);
			
			response.setContent(gson.toJson(attempt));
		}
		else {
			response.setStatusCode(ResponseStatus.NOT_FOUND.code());
			response.setContent(null);
		}
		
		return response;
	}

	/**
	 * Retrieve a summary of a user's attempt of an exam in a course with 
	 * GET /users/{userId}/courses/{courseId}/exams/{examId}/attempts/{attempdId}/summary
	 * using OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param attemptId	ID of the attempt
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getExamAttemptSummary(String userId, String courseId, String examId, String attemptId) throws IOException {
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_ATTEMPTS_SUMMARY,userId,courseId,examId,attemptId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}

	/**
	 * Retrieve a user's current attempt or create new attempt of an exam in a course with
	 * getCurrentExamAttempt and createExamAttempt
	 * using OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response startExamAttempt(String userId, String courseId, String examId) throws IOException {
		return startExamAttempt(userId, courseId, examId, null);
	}
	
	/**
	 * Retrieve a user's current attempt or create new attempt of an exam in a course with
	 * getCurrentExamAttempt and createExamAttempt
	 * using OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @param examPassword	Optional password from instructor
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response startExamAttempt(String userId, String courseId, String examId, String examPassword) throws IOException {
		Response response = getCurrentExamAttempt(userId,courseId,examId);
		if(response.getStatusCode() == ResponseStatus.NOT_FOUND.code()) {
			response = createExamAttempt(userId,courseId,examId,examPassword);
		}
		return response;
	}
	
	/**
	 * Retrieve sections of an user's exam in a course with 
	 * GET /users/{userId}/courses/{courseId}/exams/{examId}/sections
	 * using OAuth1 or OAuth2 as a student, teacher, teaching assistant or administrator
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getExamSections(String userId, String courseId, String examId) throws IOException {
		 
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS,userId,courseId,examId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		return response;
	}

	/**
	 * Retrieve details of questions for a section of a user's exam in a course with 
	 * GET /users/{userId}/courses/{courseId}/exams/{examId}/sections/{sectionId}/questions
	 * and getExamSectionQuestion
	 * using OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @param sectionId	ID of the section on the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getExamSectionQuestions(String userId, String courseId, String examId, String sectionId) throws IOException {
		
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_QUESTIONS,userId,courseId,examId,sectionId);
		Response response = doMethod(HttpMethod.GET,relativeUrl,NO_CONTENT);
		if(response.isError()) {
			return response;
		}
		
		String sectionQuestionJson = response.getContent();
		JsonObject json = this.jsonParser.parse(sectionQuestionJson).getAsJsonObject();
		JsonArray questions = json.get("questions").getAsJsonArray();
		JsonArray sectionQuestions = new JsonArray();
		
		for(Iterator<JsonElement> questionIter = questions.iterator(); questionIter.hasNext();) {
			JsonObject question = questionIter.next().getAsJsonObject();
			String questionType = question.get("type").getAsString();
			String questionId = question.get("id").getAsString();
			Response questionResponse = getExamSectionQuestion(userId,courseId,examId,sectionId,questionType,questionId);
			if(questionResponse.isError()) {
				return questionResponse;
			}
			
			JsonObject sectionQuestion = this.jsonParser.parse(questionResponse.getContent()).getAsJsonObject();
			sectionQuestion.addProperty("id", questionId);
			sectionQuestion.addProperty("type", questionType);
			sectionQuestion.addProperty("pointsPossible", question.get("pointsPossible").getAsString());
			sectionQuestions.add(sectionQuestion);
		}
		
		JsonObject sectionQuestionsWrapper = new JsonObject();
		sectionQuestionsWrapper.add("questions",sectionQuestions);

		response.setContent(this.gson.toJson(sectionQuestionsWrapper));
		return response;
	}

	/**
	 * Retrieve details of a question for a section of a user's exam in a course with
	 * GET /users/{userId}/courses/{courseId}/exams/{examId}/sections/{sectionId}/{questionType}Questions/{questionId}
	 * and GET /users/{userId}/courses/{courseId}/exams/{examId}/sections/{sectionId}/{questionType}Questions/{questionId}/choices
	 * and GET /users/{userId}/courses/{courseId}/exams/{examId}/sections/{sectionId}/{questionType}Questions/{questionId}/premises
	 * using OAuth2 as a student
	 *   
	 * @param userId	ID of the user
	 * @param examId	ID of the exam
	 * @param sectionId	ID of the section
	 * @param questionType	Type of question
	 * @param questionId	ID of the question
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getExamSectionQuestion(String userId, String courseId, String examId, String sectionId, String questionType, String questionId) throws IOException {
		
		QuestionType type = QuestionType.matchesValue(questionType);
		if(type == null) {
			throw new RuntimeException("Invalid Question Type");
		}
		
		Response response = getCurrentExamAttempt(userId,courseId,examId);
		if(response.isError()) {
			return response;
		}
		Map<String, String> extraHeaders = getExamHeaders(response);
		
		String questionRelativeUrl = null;
		String choicesRelativeUrl = null;
		String premisesRelativeUrl = null;
		
		switch(type) {
			case TRUE_FALSE:
				questionRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_TRUEFALSE_,userId, courseId, examId, sectionId, questionId);
				choicesRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_TRUEFALSE_CHOICES,userId, courseId, examId, sectionId, questionId);
				break;
			case MULTIPLE_CHOICE:
				questionRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_MULTIPLECHOICE_,userId, courseId, examId, sectionId, questionId);
				choicesRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_MULTIPLECHOICE_CHOICES,userId, courseId, examId, sectionId, questionId);
				break;
			case MANY_MULTIPLE_CHOICE:
				questionRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_MANYMULTIPLECHOICE_,userId, courseId, examId, sectionId, questionId);
				choicesRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_MANYMULTIPLECHOICE_CHOICES,userId, courseId, examId, sectionId, questionId);
				break;
			case MATCHING:
				questionRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_MATCHING_,userId, courseId, examId, sectionId, questionId);
				premisesRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_MATCHING_PREMISES,userId, courseId, examId, sectionId, questionId);
				choicesRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_MATCHING_CHOICES,userId, courseId, examId, sectionId, questionId);
				break;
			case SHORT_ANSWER:
				questionRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_SHORT_,userId, courseId, examId, sectionId, questionId);
				break;
			case ESSAY:
				questionRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_ESSAY_,userId, courseId, examId, sectionId, questionId);
				break;
			case FILL_IN_THE_BLANK:
				questionRelativeUrl = String.format(PATH_USERS_COURSES_EXAMS_SECTIONS_FILLINTHEBLANK_,userId, courseId, examId, sectionId, questionId);
				break;		
		}
		
		
		JsonObject details = new JsonObject();
		
		response = doMethod(extraHeaders, HttpMethod.GET,questionRelativeUrl,NO_CONTENT);
		if(response.isError()) {
			return response;
		}
		
		JsonObject question = this.jsonParser.parse(response.getContent()).getAsJsonObject();
		details.add("question", question.get(type.value()+"Question").getAsJsonObject());
		
		if(choicesRelativeUrl!=null) {
			response = doMethod(extraHeaders,HttpMethod.GET,choicesRelativeUrl,NO_CONTENT);
			if(response.isError()) {
				return response;
			}
			
			JsonObject choices = this.jsonParser.parse(response.getContent()).getAsJsonObject();
			details.add("choices", choices.get("choices").getAsJsonArray());
		}
		
		if(premisesRelativeUrl!=null) {
			response = doMethod(extraHeaders, HttpMethod.GET,premisesRelativeUrl,NO_CONTENT);
			if(response.isError()) {
				return response;
			}
			
			JsonObject choices = this.jsonParser.parse(response.getContent()).getAsJsonObject();
			details.add("premises", choices.get("premises").getAsJsonArray());
		}

		response.setContent(this.gson.toJson(details));
		return response;
	}

	/**
	 * Updates a user's answer for a question on a specific attempt of an exam in a course with
	 * GET /users/{userId}/courses/{courseId}/exams/{examId}/attempts/{attemptId}/answers/{answerId}
	 * POST /users/{userId}/courses/{courseId}/exams/{examId}/attempts/{attemptId}/answers
	 * PUT /users/{userId}/courses/{courseId}/exams/{examId}/attempts/{attemptId}/answers/{answerId}
	 * using OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam	
	 * @param attemptId	ID of the attempt on the exam
	 * @param questionId	ID of the question on the exam
	 * @param answer	Answer to the question on the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response answerQuestion(String userId, String courseId, String examId, String attemptId, String questionId, String answer) throws IOException {
		Response response = getExamAttempt(userId,courseId,examId, attemptId);
		if(response.isError()) {
			return response;
		}
		Map<String, String > extraHeaders = getExamHeaders(response);
		
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_ATTEMPTS_ANSWERS_, userId, courseId, examId, attemptId, questionId);
		response = doMethod(extraHeaders, HttpMethod.GET, relativeUrl, NO_CONTENT);
		if(response.isError()) {
			if(response.getStatusCode() == ResponseStatus.NOT_FOUND.code()) {
				relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_ATTEMPTS_ANSWERS, userId, courseId, examId, attemptId);
				response = doMethod(extraHeaders, HttpMethod.POST,relativeUrl, answer);
			}
		}
		else {
			response = doMethod(extraHeaders, HttpMethod.PUT,relativeUrl, answer);
		}
		
		return response;
	}
	
	/**
	 * Retrieve a user's answer for a question on a specific attempt of an exam in a course with
	 * GET /users/{userId}/courses/{courseId}/exams/{examId}/attempts/{attemptId}/answers/{answerId}
	 * using OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam	
	 * @param attemptId	ID of the attempt on the exam
	 * @param questionId	ID of the question on the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response getQuestionAnswer(String userId, String courseId, String examId, String attemptId, String questionId) throws IOException {
		Response response = getExamAttempt(userId,courseId,examId,attemptId);
		if(response.isError()) {
			return response;
		}
		Map<String, String > extraHeaders = getExamHeaders(response);
		
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_ATTEMPTS_ANSWERS_, userId, courseId, examId, attemptId, questionId);
		response = doMethod(extraHeaders, HttpMethod.GET, relativeUrl, NO_CONTENT);
		
		return response;
	}

	/**
	 * Delete a user's answer for a question on a specific attempt of an exam in a course with
	 * DELETE /users/{userId}/courses/{courseId}/exams/{examId}/attempts/{attemptId}/answers/{answerId}
	 * using OAuth2 as a student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam	
	 * @param attemptId	ID of the attempt on the exam
	 * @param questionId	ID of the question on the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response deleteQuestionAnswer(String userId, String courseId, String examId, String attemptId, String questionId) throws IOException {
		Response response = getExamAttempt(userId,courseId,examId,attemptId);
		if(response.isError()) {
			return response;
		}
		Map<String, String > extraHeaders = getExamHeaders(response);
		
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_ATTEMPTS_ANSWERS_, userId, courseId, examId, attemptId, questionId);
		response = doMethod(extraHeaders, HttpMethod.DELETE, relativeUrl, NO_CONTENT);
		
		return response;
	}
	
	/**
	 * Updates a user's attempt of an exam in a course to complete with 
	 * PUT /users/{userId}/courses/{examId}/exams/{examId}/attempts/{attemptId}
	 * using OAuth2 as student
	 * 
	 * @param userId	ID of the user
	 * @param courseId	ID of the course
	 * @param examId	ID of the exam
	 * @param attemptId	ID of the attempt on the exam
	 * @return Response object with details of status and content
	 * @throws IOException
	 */
	public Response completeExamAttempt(String userId, String courseId, String examId, String attemptId) throws IOException {
		Response response = getExamAttempt(userId,courseId,examId, attemptId);
		if(response.isError()) {
			return response;
		}
		Map<String, String > extraHeaders = getExamHeaders(response);
		
		String relativeUrl = String.format(PATH_USERS_COURSES_EXAMS_ATTEMPTS_,userId,courseId,examId,attemptId);
		String json = "{ \"attempts\" : { \"isCompleted\" : true } }";
		response = doMethod(extraHeaders, HttpMethod.PUT,relativeUrl, json);
		return response;
	}
	
	private Map<String, String> getExamHeaders(Response response) throws IOException {		
		JsonObject attempt = this.jsonParser.parse(response.getContent()).getAsJsonObject();
		attempt = attempt.get("attempt").getAsJsonObject();
		Map<String,String> extraHeaders = new HashMap<String,String>();
		extraHeaders.put(PEARSON_EXAM_TOKEN, attempt.get("pearsonExamToken").getAsString());
		return extraHeaders;
	}
	
}
