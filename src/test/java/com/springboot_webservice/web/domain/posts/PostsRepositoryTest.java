package com.springboot_webservice.web.domain.posts;
import com.springboot_webservice.domain.posts.Posts;
import com.springboot_webservice.domain.posts.PostsRepository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

//import static org.junit.Assert.assertThat; 얘 쓰면 error 난다. 진짜 이거 떄문에 한시간 쓴듯;; 보고 잘 따라쓰자 ;
import static org.assertj.core.api.Assertions.assertThat;

//#1. 별다른 설정을 하지 안혹, @SpringBootTest를 사용할 경우, H2 데이터베이스를 자동으로 실행해준다.
//#2. 실행되는 쿼리를 보기 위해서 -> src/main/resources 의 application.properties 파일에서 spring.jpa.show_sql=true 설정
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After //1. Junit에서 단위 테스트가 끝날 때마다 수행되는 메소드를 지정함. 테스트간 데이터침범을 막귀 위해 사용함.
    public void cleanUp(){
        postsRepository.deleteAll();
    }

    @Test
    public void board_create_load(){ //게시글 저장 불러오기
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder() //2. posts에 insert/update 쿼리를 실행하는 것. id 값이 있다면 update, 없다면 insert 쿼리가 실행이 된다.
                .title(title)
                .content(content)
                .author("gyeolse@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll(); //테이블 list에 있는 모든 데이터들을 조회해오는 method.

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }
}
