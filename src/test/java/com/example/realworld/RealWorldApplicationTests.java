package com.example.realworld;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RealWorldApplicationTests {

    @Test
    void contextLoads() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RealWorldApplication.class);
        int beanDefinitionCount = context.getBeanDefinitionCount();
        assertThat(beanDefinitionCount).isNotZero();
    }

}
