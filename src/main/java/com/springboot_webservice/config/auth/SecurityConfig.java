package com.springboot_webservice.config.auth;

import com.springboot_webservice.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity //1. Spring Security 설정들을 활성화 시켜줌.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() //2. h2-console 화면을 사용하기 위해서, 해당 옵션들 disable
                .and()
                    .authorizeRequests() //3. authorizeRequests. URL별 권한 관리를 설정하는 옵션의 시작점. .authorizeRequests 가 있어야지만, antMatchers 옵션 설정이 가능하다.
                    .antMatchers("/","/css/**","/images/**","/js/**","/h2-console/**").permitAll()
                //3. antMatchers. 권한 관리 대상을 지정하는 옵션. URL,HTTP 메소드별로 관리가 가능함. "/"등 지정된 URL들을 permitAll 옵션을 통해서 전체 열람 권한
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name()) //4
                // api/v1/**들은 USER권한 가진 사람들만 가능하도록
                    .anyRequest().authenticated()
                //5. anyRequest. 설정된 값들 이외 나머지 url. 여기선 authenticated()을 추가해 나머지 URL들은 모두 인증된 사용자들에게만 허용하게 함.
                //즉. 로그인한 사용자들을 말함.
                .and()
                    .logout()
                        .logoutSuccessUrl("/") //로그아웃 성공시 /주소로 이동
                .and()
                    .oauth2Login() //로그인 기능에 대한 여러 설정의 진입점
                        .userInfoEndpoint() //로그인 설정 이후, 사용자 정보를 가져올 때의 설정들을 담당.
                            .userService(customOAuth2UserService); //소셜 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
        //리소스 서버(소셜 서비스)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있다.
    }
}
