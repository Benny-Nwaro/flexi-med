package com.example.flexiMed.auth.authService;

import com.example.flexiMed.security.JwtUtil;
import com.example.flexiMed.dto.UserDTO;
import com.example.flexiMed.model.UserEntity;
import com.example.flexiMed.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User googleUser = (OAuth2User) authentication.getPrincipal();
        String email = googleUser.getAttribute("email");

        UserDTO userDTO = userService.getUserByEmail(email);
        UserEntity user = userService.loadUserByUsername(email); // Get UserEntity

        // Create an Authentication object from UserEntity
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, // principal (UserEntity, which implements UserDetails)
                null, // credentials (not needed for JWT)
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())) // roles
        );

        String jwtToken = jwtUtil.generateToken(auth); // Pass the Authentication object

        String redirectUrl = UriComponentsBuilder.fromUriString("https://flexi-med-front-itcp.vercel.app/")
                .queryParam("token", jwtToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}