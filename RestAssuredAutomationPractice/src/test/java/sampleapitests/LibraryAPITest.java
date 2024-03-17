package sampleapitests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import utilities.BaseClass;
import static org.hamcrest.Matchers.*;
import org.testng.Assert;

import static io.restassured.RestAssured.*;

public class LibraryAPITest {

	@Test(priority = 1, dataProvider = "Books")
	public void addBookTest(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";

		String addbook_response = given().header("Content-Type", "application/json").body(payload.AddBook(isbn, aisle))
				.when().post("Library/Addbook.php").then().assertThat().statusCode(200).body("Msg",equalTo("successfully added")).extract().response().asString();

		JsonPath js = BaseClass.rawToJson(addbook_response);
		String id = js.getString("ID");
		System.out.println(id);

	}

	@Test(priority = 2,dataProvider = "Books")
	public void deleteBooks(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";

		String id = isbn+aisle;
		String deletebook_response = given().header("Content-Type", "application/json").body(payload.deleteBook(id))
				.when().post("Library/DeleteBook.php").then().assertThat().statusCode(200).extract().response().asString();
		JsonPath js = BaseClass.rawToJson(deletebook_response);
		String message = js.getString("msg");
		Assert.assertEquals(message,"book is successfully deleted");
	}

	@DataProvider(name = "Books")
	public Object[][] getBooksList() {
		Object[][] arr = new Object[4][2];

		arr[0][0] = "Book1_";
		arr[0][1] = "0001";

		arr[1][0] = "Book2_";
		arr[1][1] = "0002";

		arr[2][0] = "Book3_";
		arr[2][1] = "0003";

		arr[3][0] = "Book4_";
		arr[3][1] = "0004";

		return arr;

	}
}
