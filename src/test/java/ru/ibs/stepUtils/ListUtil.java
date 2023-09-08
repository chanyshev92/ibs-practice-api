package ru.ibs.stepUtils;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import ru.ibs.data.Product;

import java.util.List;

public class ListUtil {
    public static boolean checkListContainsProduct(List<Product> list, Product product) {
        return list
                .stream()
                .anyMatch(s -> (s.getName().equals(product.getName()) &&
                        s.getType().equals(product.getType())
                        && s.getExotic() == product.getExotic()));
    }

    @Step("Проверяем,что список не содержит продукт")
    public static void assertNotContains(List<Product> dataList, Product product) {
        Assertions.assertFalse(ListUtil.checkListContainsProduct(dataList, product),
                "Продукт есть в исходном списке");
    }

    @Step("Проверяем,что список содержит продукт")
    public static void assertContains(List<Product> dataList, Product product) {
        Assertions.assertTrue(ListUtil.checkListContainsProduct(dataList, product),
                "Продукта нет в исходном списке");
    }
}
