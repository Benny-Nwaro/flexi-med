package com.example.flexiMed.auth.authController;


import com.example.flexiMed.security.JwtUtil;
import com.example.flexiMed.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/oauth2")
public class OAuth2AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public OAuth2AuthController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @GetMapping("/token")
    private TokenResponse getOAuth2Token(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {
            throw new IllegalStateException("User is not authenticated via OAuth2");
        }

        Authentication email = oAuth2User.getAttribute("email");

        // Create JWT token
        assert email != null;
        String token = jwtUtil.generateToken(email);

        return new TokenResponse(token);
    }

    record TokenResponse(String token) {
    }
}

