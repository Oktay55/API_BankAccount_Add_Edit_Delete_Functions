import Pojo.Campus;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BankAccountFunctionalityPojo {

    private RequestSpecification reqSpec;
    private Campus user = new Campus();
    private HashMap<String, String> credentials;

    private String bankAccountId;

    private Cookies cookies;


    @BeforeClass
    public void setup() {


        RestAssured.baseURI = "https://demo.mersys.io/";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);

        credentials = new HashMap<>();
        credentials.put("username", "richfield.edu");
        credentials.put("password", "Richfield2020!");
        credentials.put("rememberMe", "true");

        cookies = given()
                .spec(reqSpec)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .log().body()
                .statusCode(200)
                .body("scope", equalTo("openid"))
                .extract().detailedCookies();

    }

    @Test(priority = 1)
    public void addUserBankAccountTest() {

        user.setName("fdgdfgdfg ihhiih");
        user.setIban("QT75 91");
        user.setIntegrationCode("843");
        user.setCurrency("USD");
        user.setActive("true");
        user.setSchoolId("5fe07e4fb064ca29931236a5");

        user.setId(given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(user)
                .when()
                .post("/school-service/api/bank-accounts")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(user.getName()))
                .body("iban", equalTo(user.getIban()))
                .body("integrationCode", equalTo(user.getIntegrationCode()))
                .body("currency", equalTo(user.getCurrency()))
                .body("schoolId", equalTo(user.getSchoolId()))
                .extract().jsonPath().getString("id"));


    }

    @Test(priority = 2)
    public void addUserBankAnkAccountNegativeTest() {

        user.setName("fdgdfgdfg ihhiih");
        user.setIban("QT75 91");
        user.setIntegrationCode("843");
        user.setCurrency("USD");
        user.setActive("true");
        user.setSchoolId("5fe07e4fb064ca29931236a5");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(user)
                .when()
                .post("/school-service/api/bank-accounts")
                .then()
                .log().body()
                .statusCode(400);


    }

    @Test(priority = 3)
    public void editUserBankAccountTest() {

        HashMap<String, String> updateReqBody = new HashMap<>();
        updateReqBody.put("id", user.getId());
        updateReqBody.put("name", "cvcx gff");
        updateReqBody.put("iban", "ZC50 43");
        updateReqBody.put("integrationCode", "017");
        updateReqBody.put("currency", "USD");
        updateReqBody.put("active", "true");
        updateReqBody.put("schoolId", "5fe07e4fb064ca29931236a5");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updateReqBody)
                .when()
                .put("/school-service/api/bank-accounts")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(updateReqBody.get("name")));
    }
    @Test(priority = 4)
    public void deleteUserBankAccountTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/bank-accounts/" + user.getId())
                .then()
                .log().body()
                .statusCode(200);

    }
    @Test(priority = 5)
    public void deleteUserBankAccountNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/bank-accounts/" + user.getId())
                .then()
                .log().body()
                .statusCode(400);
    }

}
