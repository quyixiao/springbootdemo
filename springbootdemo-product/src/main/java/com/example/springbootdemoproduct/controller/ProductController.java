package com.example.springbootdemoproduct.controller;


import com.example.springbootdemoentity.entity.Product;
import com.example.springbootdemoproduct.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class ProductController {
    @RequestMapping(value = "getProduct")
    public String getProduct() {
        Product product = new Product();
        log.info("生产者生产数据：" + product.toString());
        return product.toString();
    }

}
