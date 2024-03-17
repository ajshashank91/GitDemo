package sampleapitests;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.payload;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import utilities.BaseClass;

public class JIRATest {
	String sessionID;
	String project;
	String description;
	String summary;

	@Test(priority = 1)
	public void testJIRAcreateIssue() {
		project = "RSA";
		description = "Creating a new bug from REST API's";
		summary = "Front end bug";

		RestAssured.baseURI = "http://localhost:8080";

		SessionFilter session = new SessionFilter();

		given().header("Content-Type", "application/json").body(payload.createSession("ajshashank91", "Jira@1234"))
				.log().all().filter(session).when().post("rest/auth/1/session").then().log().all().assertThat()
				.statusCode(200).extract().response().asString();

//		JsonPath js = BaseClass.rawToJson(response_sessionAPI);
//		String cookie_name = js.getString("session.name");
//		String cookie_value = js.getString("session.value");

//		System.out.println("Cookie name: "+cookie_name);
//		System.out.println("Cookie value: "+cookie_value);

		String response_post = given().log().all().header("Content-Type", "application/json")
				.body(payload.createIssue(project, summary, description)).filter(session).when()
				.post("rest/api/2/issue").then().log().all().assertThat().statusCode(201).extract().response()
				.asString();

		JsonPath js = BaseClass.rawToJson(response_post);
		String id = js.getString("id");

		// Add comments to above issue
		String comment = "commenting from code Rest API's";
		String response_addcomment = given().header("Content-Type", "application/json").pathParam("key", id)
				.body(payload.addJIRAComment(comment)).log().all().filter(session)
				.post("rest/api/2/issue/{key}/comment").then().log().all().assertThat().statusCode(201).extract()
				.response().asString();

		JsonPath js2 = BaseClass.rawToJson(response_addcomment);
		String cid = js2.getString("id");

		// update the above comments
		comment = "updated comments from code Rest API's";
		given().header("Content-Type", "application/json").pathParam("issueid", id).pathParam("commentid", cid)
				.body(payload.addJIRAComment(comment)).log().all().filter(session)
				.put("rest/api/2/issue/{issueid}/comment/{commentid}").then().log().all().assertThat().statusCode(200)
				.extract().response().asString();

		// Add attachments:
		String path = "E:\\ws-practice\\RestAssuredAutomationPractice\\src\\test\\java\\files\\Jira.txt";
		given().header("X-Atlassian-Token", "no-check").header("Content-Type", "multipart/form-data").filter(session)
				.pathParam("issueId", id).multiPart("file", new File(path)).log().all()
				.post("/rest/api/2/issue/{issueId}/attachments").then().log().all().statusCode(200);
		
		//Get issue:
		String get_response = given().queryParam("fields","comment").filter(session).pathParam("issueId", id).log().all().when().
		get("/rest/api/2/issue/{issueId}").then().log().all().extract().response().asString();
		
		JsonPath js3 = BaseClass.rawToJson(get_response);
		int count = js3.get("fields.comment.comments.size()");
		String commentid = null;
		String actualcomment = null;
		for(int i=0;i<count;i++) {
			commentid = js3.get("fields.comment.comments["+i+"].id");
			if(cid.equals(commentid)) {
				actualcomment = js3.get("fields.comment.comments["+i+"].body");
			}
		}
		Assert.assertEquals(actualcomment, comment);
	}

	@Test(priority = 2)
	public void testJIRAcreateIssue2() {
		System.out.println("Test2-Added for leaning git-Git demo");
	}
	
	@Test(priority = 3)
	public void testJIRAcreateIssue3() {
		System.out.println("Test3-Added for leaning git-Git demo");
	}
	
	@Test(priority = 4)
	public void testJIRAcreateIssue4() {
		System.out.println("Test4-Added for leaning git-Git demo");
	}
	
	@Test(priority = 5)
	public void testJIRAcreateIssue5() {
		System.out.println("Test5-Added for leaning git-Git demo");
	}
	
	@Test(priority = 6)
	public void testJIRAcreateIssue6() {
		System.out.println("Develop branch:Test6-Added for leaning git-Git demo");
	}
	
	@Test(priority = 7)
	public void testJIRAcreateIssue7() {
		System.out.println("Develop branch:Test7-Added for leaning git-Git demo");
	}
}

//http://localhost:8080/secure/SetupMode!default.jspa
//server id :BXFH-JYA5-GU2J-VHOS
//Email: ajshashank91@gmail.com , username:ajshashank91 password ; Jira@1234
//Project: RSA
//JIRA API'S : https://docs.atlassian.com/software/jira/docs/api/REST/8.5.15/