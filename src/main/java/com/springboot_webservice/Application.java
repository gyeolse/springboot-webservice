package com.springboot_webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //스프링 부트의 자동 설정, 스프링 Bean 읽기, 생성 모두 자동으로 설정. 항상 프로젝트의 상단에 위치해야 함.
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); //내장 was를 실행하는 과정.
    }
}
