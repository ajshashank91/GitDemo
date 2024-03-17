package sampleapitests;
import static io.restassured.RestAssured.given;

import java.util.List;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import pojo.API;
import pojo.GetCourses;
import utilities.BaseClass;

public class OAuthSDTest {
	@Test
	public void oAuthTestSerandDS() {
		RestAssured.baseURI="https://rahulshettyacademy.com/";
		String response = given().formParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com").
		formParam("client_secret","erZOWM9g3UtwNRj340YYaK_W").formParam("grant_type", "client_credentials").
		formParam("scope","trust").log().all().when().post("oauthapi/oauth2/resourceOwner/token").then().
		extract().response().asString();
		
		JsonPath js = BaseClass.rawToJson(response);
		String access_token = js.getString("access_token");
		System.out.println("Access token: "+access_token);
		
		//Now run the Get request:
		 GetCourses gc =given().queryParam("access_token", access_token).log().all().when().log().all().get("oauthapi/getCourseDetails")
		.as(GetCourses.class);
		 
		 System.out.println(gc.getInstructor());
		 System.out.println(gc.getLinkedIn());
		 
		 System.out.println(gc.getCourses().getMobile().get(0).getCourseTitle());
		 System.out.println(gc.getCourses().getMobile().get(0).getPrice());
		 
		 List<API> apicourses = gc.getCourses().getApi();
		 for(int i=0;i<apicourses.size();i++) {
			 if(apicourses.get(i).getCourseTitle().equals("SoapUI Webservices testing")) {
				 System.out.println("Price of SoapUI Webservices testing: "+apicourses.get(i).getPrice());
			 }
		 }
}
}
