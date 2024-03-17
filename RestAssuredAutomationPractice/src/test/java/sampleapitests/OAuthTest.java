package sampleapitests;
import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import utilities.BaseClass;

public class OAuthTest {
	@Test
	public void oAuthTest() {
		RestAssured.baseURI="https://rahulshettyacademy.com/";
		String response = given().formParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com").
		formParam("client_secret","erZOWM9g3UtwNRj340YYaK_W").formParam("grant_type", "client_credentials").
		formParam("scope","trust").log().all().when().post("oauthapi/oauth2/resourceOwner/token").then().
		extract().response().asString();
		
		JsonPath js = BaseClass.rawToJson(response);
		String access_token = js.getString("access_token");
		System.out.println("Access token: "+access_token);
		
		//Now run the Get request:
		 String response_get =given().queryParam("access_token", access_token).log().all().when().get("oauthapi/getCourseDetails").then().
		 extract().response().asString();
		 
		 System.out.println("Response of get method: "+response_get);
	}
}
