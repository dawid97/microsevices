package com.javasolution.app.bookingservice.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.javasolution.app.bookingservice.security.SecurityConstants.HEADER_AUTHORIZATION;

public class AuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) { return; }
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(HEADER_AUTHORIZATION);
        if (token == null) { return; }
        template.header("Authorization", token);
    }
}
