package ru.ibs.tests;

import io.qameta.allure.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.ibs.data.Product;
import ru.ibs.steps.ApiSteps;
import ru.ibs.stepUtils.ListUtil;

import java.util.List;

import static io.restassured.RestAssured.given;
import static ru.ibs.specifications.Specifications.*;


public class ProductsApiTest extends BaseTest {

    @Step("Создадим продукт с параметрами: {foodName},{foodType},{exotic}")
    private Product createProduct(String foodName, String foodType, boolean exotic) {
        return Product
                .builder()
                .name(foodName)
                .type(foodType)
                .exotic(exotic)
                .build();
    }

    @DisplayName("Проверка получения ответа от страницы \"Товары\"")
    @Test
    public void testConsistence() {
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
    @CsvSource(value = {"string,VEGETABLE,true", "Ананас, FRUIT, true"})
    public void testAddProduct(String foodName, String foodType, boolean exotic) {

        installSpec(requestSpec(), responseSpec());

        Product product = createProduct(foodName, foodType, exotic);

        List<Product> dataListApi1 = ApiSteps.getRowsApi();

        ListUtil.assertNotContains(dataListApi1, product);

        ApiSteps.addByApi(product);

        List<Product> dataListApi2 = ApiSteps.getRowsApi();

        ListUtil.assertContains(dataListApi2, product);

        ApiSteps.resetPage();

        deleteSpec();

        List<Product> dataListDb1 = dataBase.selectAll().stream().toList();

        ListUtil.assertNotContains(dataListDb1, product);

        dataBase.insertProduct(product);

        List<Product> dataListDb2 = dataBase.selectAll().stream().toList();

        ListUtil.assertContains(dataListDb2, product);

        dataBase.deleteProduct(product);
    }
}
