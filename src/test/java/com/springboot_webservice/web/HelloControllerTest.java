package com.springboot_webservice.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void returned_hello() throws Exception{
        String hello = "hello";

        mvc.perform(get("/hello")) //요청할 GET 요청.
                .andExpect(status().isOk()) //200,404, 500 등의 상태 점검. OK는 200을 뜻한다.
                .andExpect(content().string(hello)); //기대되는 값. 여기서는 hello가 기대된다.
    }

    @Test
    public void returned_helloDto() throws Exception {
        String name = "hello";
        int amount =1000;

        mvc.perform(
                get("/hello/dto")
                                    .param("name",name)
                                    .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(name)))
                .andExpect(jsonPath("$.amount",is(amount)));
    }
}
