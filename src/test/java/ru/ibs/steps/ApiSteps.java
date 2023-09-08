package ru.ibs.steps;

import io.qameta.allure.Step;
import ru.ibs.data.Product;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ApiSteps {
    @Step("Получаем строки таблицы через Api")
    public static List<Product> getRowsApi() {
        return Arrays.stream(
                given()
                        .when()
                        .get("/api/food")
                        .then()
                        .extract().body().as(Product[].class)).toList();
    }

    @Step("Добавим продукт через Api")
    public static void addByApi(Product product) {
        given()
                .body(product)
                .when()
                .post("/api/food")
                .then()
                .log().all();
    }

    @Step("Приведем страницу в исходное состояние")
    public static void resetPage() {
        given()
                .when()
                .post("/api/data/reset");
    }

}
