package com.github.hgaol.uiharu.helper;

import com.github.hgaol.uiharu.demo.Customer;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseHelperTest {

    private Customer customer = new Customer("hgao", "hello", "1234567890", "hgao@cmss.com");
    private Customer customer2 = new Customer("hahaha", "hello", "1234567890", "hahaha@cmss.com");

    @Test
    public void capitalizeToPosix() {
        System.out.println(DatabaseHelper.capitalizeToPosix("HelloWorld", "_"));
        System.out.println(DatabaseHelper.capitalizeToPosix("Hello123World", "_"));
    }

    @Test
    public void save() {
    }

    @Test
    public void getColoums() {
        System.out.println(DatabaseHelper.getColoums(customer));
    }

    @Test
    public void getColoumValue() {
    }

    @Test
    public void insert() {
        DatabaseHelper.insert(customer);
    }

    @Test
    public void update() {
        customer2.setId(3);
        DatabaseHelper.update(customer2);
    }

//    @Test
//    public void queryEntity() {
//        System.out.println(DatabaseHelper.queryEntity(Customer.class, "select * from customer where id = ?", "1"));
//    }

}