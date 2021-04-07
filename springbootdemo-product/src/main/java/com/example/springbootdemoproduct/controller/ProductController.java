package com.example.springbootdemoproduct.controller;


import com.example.springbootdemoentity.entity.Product;
import com.example.springbootdemoproduct.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author YangPeng
 * @Title: ProductController
 * @ProjectName springbootdemo
 * @Description: TODO
 * @date 2019/3/25-19:26
 */
@RestController
@Slf4j
public class ProductController {
    @RequestMapping(value = "getProduct")
    public String getProduct() {
        log.info("xxxxxxxxx");

        Product product = new Product();
        System.out.println("--------------------------");
        return product.toString();
    }

    @RequestMapping(value = "/home")
    public String home() {
        System.out.println("123111");
        throw new MyException("101", "错误");
    }
}
