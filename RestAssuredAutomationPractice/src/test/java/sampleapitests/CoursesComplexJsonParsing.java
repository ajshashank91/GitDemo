package sampleapitests;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.payload;
import io.restassured.path.json.JsonPath;

public class CoursesComplexJsonParsing {
	 @Test
	    public void testingComplexJSONtest() {
		 
		JsonPath js = new JsonPath(payload.getCourses());

		// 1. Print No of courses returned by API
		int coursecount = js.getInt("courses.size()");
		System.out.println("Number of courses:" + coursecount);

		// 2.Print Purchase Amount
		int purchaseamount = js.getInt("dashboard.purchaseAmount");
		System.out.println("Total amount: " + purchaseamount);

		// 3. Print Title of the first course
		String first_coursetitle = js.getString("courses[0].title");
		System.out.println("First course title: " + first_coursetitle);

		// 4. Print All course titles and their respective Prices
		int sum = 0;
		for (int i = 0; i < coursecount; i++) {
			System.out.print(js.getString("courses[" + i + "].title"));
			System.out.print(", ");
			System.out.print(js.getInt("courses[" + i + "].price"));
			System.out.println();

			// 5. Print no of copies sold by RPA Course
			if (js.getString("courses[" + i + "].title").equals("RPA")) {
				System.out.println("Number of copies sold by RPA course: " + "" + js.getInt("courses[" + i + "].copies"));
			}

			// 6. Verify if Sum of all Course prices matches with Purchase Amount
			int quantity = js.getInt("courses[" + i + "].copies");
			int price = js.getInt("courses[" + i + "].price");
			sum = sum + (quantity*price);
		}
		
		System.out.println("Calculated purchase amount for all courses: "+sum);
		Assert.assertEquals(sum, purchaseamount);
	}

}
