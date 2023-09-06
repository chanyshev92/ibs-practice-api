package ru.ibs.dataBase;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.ibs.data.Product;

import javax.sql.DataSource;
import java.util.List;

/**
 * Класс-инициализатор БД c использованием Singleton
 */
public class DataBaseInitializer {
    //language=SQL
    static final String SQL_SELECT_ALL = "select FOOD_NAME as name, FOOD_TYPE as type, FOOD_EXOTIC as exotic from FOOD order by food_id";

    //language=SQL
    static final String SQL_INSERT = "insert into FOOD(FOOD_NAME,FOOD_TYPE,FOOD_EXOTIC) values (?,?,?)";

    //language=SQL
    static final String SQL_DELETE_BY_DESCRIPTION = "delete from FOOD where FOOD_ID = (select max(FOOD_ID) from FOOD where FOOD_NAME=? and FOOD_TYPE=? and FOOD_EXOTIC =?)";


    private JdbcTemplate jdbcTemplate;

    DataBaseInitializer(DataSource dataSource){
        this.jdbcTemplate=new JdbcTemplate(dataSource);
    }

    public List<Product> selectAll(){
        return jdbcTemplate.query(SQL_SELECT_ALL, BeanPropertyRowMapper.newInstance(Product.class));
    }

    public void insertProduct(Product product){
        Object[] inserts ={product.getName(),product.getType(),product.getExotic()};
        jdbcTemplate.update(SQL_INSERT,inserts);
        //product.setFoodId(jdbcTemplate.queryForObject("select max(FOOD_ID) from FOOD where FOOD_NAME=? and FOOD_TYPE=? and FOOD_EXOTIC =? ",Integer.TYPE,inserts));
    }
    public void deleteProduct(Product product){
        Object[] inserts ={product.getName(),product.getType(),product.getExotic()};
        jdbcTemplate.update(SQL_DELETE_BY_DESCRIPTION,inserts);
    }

}
