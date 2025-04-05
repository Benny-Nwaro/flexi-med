package com.example.flexiMed.auth;

import com.example.flexiMed.exceptions.ErrorResponse;
import com.example.flexiMed.security.JwtUtil;
import com.example.flexiMed.enums.Role;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.mapper.UserMapper;
import com.example.flexiMed.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final JwtUtil jwtUtil;


    public CustomOAuth2UserService(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println("OAuth2 User Info: " + oAuth2User.getAttributes()); // Debugging

        // Extract user info
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        UserEntity user;
        try {
            // Try to fetch existing user
            user = UserMapper.toEntity(userService.getUserByEmail(email));
            System.out.println("User found: " + user.getEmail());
        } catch (ErrorResponse.NoSuchUserExistsException e) {
            System.out.println("User not found, creating new user...");

            user = new UserEntity();
            user.setName(name);
            user.setEmail(email);
            user.setRole(Role.USER);
            user.setProfileImageUrl(picture);

            // Set a default password to avoid encoding errors
            user.setPassword("GoogleAuthentication");

            user = userService.saveUser(user);
            System.out.println("New user created: " + user.getEmail());
        }

        // Generate JWT token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
        String jwtToken = jwtUtil.generateToken(authentication);
        System.out.println("Your token  " + jwtToken);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "email"
        );
    }

}

