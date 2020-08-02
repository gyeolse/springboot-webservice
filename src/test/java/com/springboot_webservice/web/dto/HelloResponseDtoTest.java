package com.springboot_webservice.web.dto;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat; //assertj 라이브러리에 들어간 것을 써야한다.

public class HelloResponseDtoTest {

    @Test
    public void isRombokOperating(){
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto dto = new HelloResponseDto(name,amount);

        //then
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }
}
