package sampleapitests;

import io.restassured.RestAssured;
import utilities.BaseClass;

import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import static io.restassured.RestAssured.*;
import files.payload;

public class AddPlaceAPIPost {

	public static void main(String[] args) throws IOException {
		// validate Add place API

		// given - all input details
		// when - submit the api
		// then - validate the response
		System.out.println("==========Post Method=============");

		RestAssured.baseURI = "https://rahulshettyacademy.com";
//		String response_withStringpayload = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
//				.body(payload.AddPlace()).when().post("maps/api/place/add/json").then().assertThat().log().all()
//				.statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)").extract()
//				.response().asString();

		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(new String (Files.readAllBytes(Paths
						.get("E:\\ws-practice\\RestAssuredAutomationPractice\\src\\test\\java\\files\\AddPlace.json"))))
				.when().post("maps/api/place/add/json").then().assertThat().log().all().statusCode(200)
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)").extract().response()
				.asString();

		System.out.println("Respone body: " + response);
		// Add place, update place with new address,verify the updated place

		// Takes response as string and provides json response
		String place_id = BaseClass.rawToJson(response).getString("place_id");
		System.out.println("Place id is:" + place_id);

		// Verify the address using get method:
		System.out.println("==========Get Method=============");

		String get_response = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", place_id)
				.when().get("maps/api/place/get/json").then().assertThat().log().all().statusCode(200)
				.header("Server", "Apache/2.4.52 (Ubuntu)").extract().asString();
		String get_address = BaseClass.rawToJson(get_response).getString("address");
		Assert.assertEquals(get_address,"26 Sandness");
		
		

		// Now run the PUT API and update the address:
		System.out.println("==========Put Method=============");

		String put_address = "Nedre Banegate 6 Stavanger";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(payload.updatePlace(place_id, put_address)).when().put("maps/api/place/update/json").then()
				.assertThat().log().all().statusCode(200).header("Server", "Apache/2.4.52 (Ubuntu)")
				.body("msg", equalTo("Address successfully updated")).extract().asString();

		// Now run the get request and check the updated address:
		System.out.println("==========Get Method=============");

		 get_response = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", place_id)
				.when().get("maps/api/place/get/json").then().assertThat().log().all().statusCode(200)
				.header("Server", "Apache/2.4.52 (Ubuntu)").extract().asString();
		 
		 get_address = BaseClass.rawToJson(get_response).getString("address");
		Assert.assertEquals(put_address, get_address);

	}

}
