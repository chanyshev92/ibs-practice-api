package ru.ibs.specifications;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

/**
 * Класс со спецификациями запросов и ответов
 */
public class Specifications {
    static Cookies cookies = getDetailedCookies();

    /**
     * Для id-cессии
     * @return Куки)
     */
    private static Cookies getDetailedCookies() {
        return RestAssured.given()
                .when()
                .get("/api/food")
                .getDetailedCookies();
    }

    /**
     * Спецификация запроса. Установка URL страницы. Установка типа JSON
     * @return Объект спецификации
     */
    public  static RequestSpecification requestSpec(){
        return new RequestSpecBuilder()
                .setBaseUri("http://localhost:8080")
                .setContentType("application/json")
                .addCookies(cookies)
                .build();
    }
    /**
     * Спецификация "Успешного" ответа. Установка статус кода 200.
     * @return Объект спецификации
     */
    public static ResponseSpecification responseSpec(){
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    /**
     * Спецификация "Провального" ответа. Установка статус кода 400.
     * @return Объект спецификации
     */
    public static ResponseSpecification responseBadSpec(){
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .build();
    }
    /**
     * Установка спецификации запроса
     * @param requestSpec Входная спецификация запроса
     */
    public static void installSpec(RequestSpecification requestSpec){
        RestAssured.requestSpecification = requestSpec;
    }

    /**
     * Установка спецификации ответа
     * @param responseSpec Входная спецификация ответа
     */
    public static void installSpec(ResponseSpecification responseSpec){
        RestAssured.responseSpecification = responseSpec;
    }

    /**
     * Установка спецификации запроса и ответа
     * @param requestSpec Входная спецификация запроса
     * @param responseSpec Входная спецификация ответа
     */
    public static void installSpec(RequestSpecification requestSpec, ResponseSpecification responseSpec){
        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
    }

    /**
     * Обнуление спецификации
     */
    public static void deleteSpec(){
        RestAssured.requestSpecification = null;
        RestAssured.responseSpecification = null;
    }

}
