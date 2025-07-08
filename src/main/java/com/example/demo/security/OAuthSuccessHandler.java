package com.example.demo.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    public static final String REDIRECT_URI_PARAM="redirect_url";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("auth succeded");

        String token=tokenProvider.create(authentication);

        Optional<Cookie> oCookie= Arrays.stream(request.getCookies()).filter(cookie->cookie.getName().equals(REDIRECT_URI_PARAM)).findFirst();
        Optional<String> redirectUri=oCookie.map(Cookie::getValue);

        log.info("token {}", token);
//        response.setContentType("text/plain;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(redirectUri.orElseGet(()->REDIRECT_URI_PARAM)+"/sociallogin?token="+token);
    }

}
