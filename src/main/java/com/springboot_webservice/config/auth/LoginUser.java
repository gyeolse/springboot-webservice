package com.springboot_webservice.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //어노테이션이 생성될 수 있는 위치를 지정함.
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser { //이 파일을 어노테이션 클래스로 지정함. LoginUser라는 이름을 가진 어노테이션이 지정되었음.
}
