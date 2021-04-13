package com.example.springbootdemoconsumer.controller;


import com.alibaba.ttl.threadpool.TtlExecutors;
import com.example.springbootdemoconsumer.service.ProductService;
import com.example.springbootdemoconsumer.service.TestService;
import com.example.springbootdemoentity.entity.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author YangPeng
 * @Title: ConsumerController
 * @ProjectName springbootdemo
 * @Description: TODO
 * @date 2019/3/27-11:32
 */
@RestController
@Slf4j
public class ConsumerController {
    @Autowired
    private ProductService productService;

    @Autowired
    private TestService testService;


    //使用@Value注解可以直接获取配置文件中的属性并注入到spring容器中；
    @Autowired
    @Value("${name}")
    private String name;


    private static ExecutorService pool = Executors.newFixedThreadPool(1);


    @RequestMapping(value = "getConsumer")
    public String getConsumer() {
        String str = productService.getProduct();
        log.info("消费者从生产者中获取到的数据 ：" + str);
        testService.testLog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = productService.getProduct();
                log.info("子线程执行 " + str);
            }
        }).start();

        for (int i = 0; i < 10; i++) {
            startThread(i);
        }

        Consumer consumer = new Consumer();
        log.info(consumer.toString());
        log.info(consumer.getAdd());
        log.info(consumer.getAge() + "");
        consumer.setAge(12333);
        log.info(consumer.getAge() + "");
        log.info(name);
        return str;
    }

    public static void startThread(final int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("i = " + i + "   使用 主线程的线程编号 测试 ");
                TtlExecutors.getTtlExecutorService(pool).submit(new MycallableA(i));
            }
        }).start();
    }

    static class MycallableA implements Runnable {
        private int i;

        public MycallableA(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            log.info("i = " + i + "  MycallableA-> 子线程中拿到线程编号 ");
        }
    }
}
