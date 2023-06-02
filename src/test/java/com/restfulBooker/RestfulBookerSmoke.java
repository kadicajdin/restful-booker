package com.restfulBooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RestfulBookerSmoke {

    private static final String BASE_URL = "https://restful-booker.herokuapp.com";
    private static final String AUTH_ENDPOINT = "/auth";
    private static final String BOOKING_ENDPOINT = "/booking";

    private String token;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test(priority = 1)
    public void UserRegistration() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "admin");
        requestParams.put("password", "password123");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestParams.toString())
                .post(AUTH_ENDPOINT);

        String token = response.jsonPath().getString("token");
        int statusCode = response.getStatusCode();

        Assert.assertEquals(statusCode, 200, "User registration failed. Status code: " + statusCode);
        //Assert.assertNotNull(token, "User registration failed. Token is null.");
        
        System.out.println("User registered successfully. Token: " + token);
        System.out.println("Response Code: " + statusCode);
        System.out.println("Response Body:");
        response.prettyPrint();
    }

    @Test(priority = 2)
    public void CreateBooking() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("firstname", "Ajdin");
        requestParams.put("lastname", "Kadic");
        requestParams.put("totalprice", 150);
        requestParams.put("depositpaid", true);

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2023-06-01");
        bookingDates.put("checkout", "2023-06-05");
        requestParams.put("bookingdates", bookingDates);
        requestParams.put("additionalneeds", "Kids Bed");

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestParams.toString())
                .post(BOOKING_ENDPOINT);

        String bookingId = response.jsonPath().getString("bookingid");
        int statusCode = response.getStatusCode();

        Assert.assertEquals(statusCode, 200, "Booking creation failed. Status code: " + statusCode);
        //Assert.assertNotNull(bookingId, "Booking creation failed. Booking ID is null.");
        
        System.out.println("Booking created successfully. ID: " + bookingId);
        System.out.println("Response Code: " + statusCode);
        System.out.println("Response Body:");
        response.prettyPrint();
    }
}
