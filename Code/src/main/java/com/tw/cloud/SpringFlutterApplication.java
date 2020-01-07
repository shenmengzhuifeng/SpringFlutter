package com.tw.cloud;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringFlutterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringFlutterApplication.class, args);
    }

}
