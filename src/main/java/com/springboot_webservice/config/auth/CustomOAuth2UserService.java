package com.springboot_webservice.config.auth;

import com.springboot_webservice.config.auth.dto.OAuthAttributes;
import com.springboot_webservice.config.auth.dto.SessionUser;
import com.springboot_webservice.domain.user.User;
import com.springboot_webservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //1. registrationId. 현재 로그인 진행 중인 서비스를 구분하는 코드. 지금은 구글만 사용하는 불필요한 값이지만, 네이버 연동시에 네이버 로그인인지, 구글 로그인인지 구분하기 위해서 사용
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //1
        //2. userNameAttributeName. OAuth2 로그인 진행시 키가 되는 필드값을 이야기함. PK와 같은 의미
        // 기본적을 구글은 코드를 지원하지만, 네이버 카카오 등은 기본 지원 X.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); //2
        //3. OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes()); //3. 추가할 것

        User user = saveOrUpdate(attributes);

        //세션에 사용자 정보를 저장하기 위한 Dto 클래스. User 클래스를 쓰지않고 새로 만들어서 쓰는 이유가 따로 있음!
        httpSession.setAttribute("user",new SessionUser(user)); //4. sessionUser 클래스 생성해야함.

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                                                                attributes.getAttributes(),
                                                                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity->entity.update(attributes.getName(), attributes.getPicture())).orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
