package ru.ibs.utils;

import ru.ibs.data.Product;

import java.util.List;

public class ListUtil {
    public static boolean checkListContainsProduct(List<Product> list, Product product){
        return list
                .stream()
                .anyMatch(s -> (s.getName().equals(product.getName()) &&
                        s.getType().equals(product.getType())
                        && s.getExotic() == product.getExotic()));
    }
}
