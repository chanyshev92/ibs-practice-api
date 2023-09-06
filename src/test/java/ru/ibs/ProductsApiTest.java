package ru.ibs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.ibs.data.Product;
import ru.ibs.dataBase.BaseTestDb;
import ru.ibs.utils.ListUtil;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static ru.ibs.specifications.Specifications.*;


public class ProductsApiTest extends BaseTestDb {
    @DisplayName("Проверка получения ответа от страницы \"Товары\"")
    @Test
    public void testConsistence(){
        installSpec(requestSpec());
        given()
                .when()
                .get("/api/food")
                .then()
                .log().all()
                .extract().body().as(Product[].class);
        deleteSpec();
    }
    @DisplayName("Проверка добавления продукта с валидными данными через Api и БД")
    @ParameterizedTest
    @CsvSource(value={"string,VEGETABLE,true","Ананас, FRUIT, true"})
    public void testAddProduct(String foodName, String foodType, boolean exotic){
        //Установим спецификации запросов и ответов
        installSpec(requestSpec(),responseSpec());
        //Создадим продукт
        Product product= Product.builder()
                .name(foodName)
                .type(foodType)
                .exotic(exotic)
                .build();
        //Проверка Api
        //Получим изначальный список
        List<Product> dataListApi1 = Arrays.stream(
                given()
                .when()
                .get("/api/food")
                .then()
                .extract().body().as(Product[].class)).toList();

        //Проверим,что исходная страница не содержит созданный продукт
        Assertions.assertFalse(ListUtil.checkListContainsProduct(dataListApi1,product),
                "Продукт есть в исходном списке, проверь тестовые данные");

        //Добавим продукт
        given()
                .body(product)
                .when()
                .post("/api/food")
                .then()
                .log().all();

        // Получим список после изменения
        List<Product> dataListApi2 = Arrays.stream(given()
                .when()
                .get("/api/food")
                .then()
                .extract().body().as(Product[].class)).toList();
        //Проверим что продукт есть в новом списке
        Assertions.assertTrue(ListUtil.checkListContainsProduct(dataListApi2,product),
                "Продукта нет в новом списке");

        //Приведем страницу в исходное состояние
        given()
                .when()
                .post("/api/data/reset");

        //Удалим спецификацию
        deleteSpec();

        //Проверка БД
        //Получим исходный список
        List<Product> dataListDb1=dataBaseInitializer.selectAll().stream().toList();

        //Проверим, что исходный список не содержит добавляемого продукта
        Assertions.assertFalse(ListUtil.checkListContainsProduct(dataListDb1,product),
                "Добавляемый продукт есть в БД, проверь тестовые данные");

        //Добавим через Dd
        dataBaseInitializer.insertProduct(product);

        //Получим новый список
        List<Product> dataListDb2 = dataBaseInitializer.selectAll().stream().toList();

        //Проверим, что полученный список содержит созданный продукт
        Assertions.assertTrue(ListUtil.checkListContainsProduct(dataListDb2,product),
                "Продукт не добавился в БД");

        //Удалим созданный продукт из базы
        dataBaseInitializer.deleteProduct(product);
    }
}
