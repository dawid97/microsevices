package com.javasolution.app.bookingservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilterAfter(new JwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/bookings/**").hasAnyRole("ADMIN","STAFF","CUSTOMER")
                .antMatchers(HttpMethod.GET, "/bookings/**").hasAnyRole("ADMIN","STAFF","RESOURCE","CUSTOMER")
                .antMatchers(HttpMethod.DELETE, "/bookings/**").hasAnyRole("ADMIN","STAFF","CUSTOMER","RESOURCE")
                .antMatchers(HttpMethod.GET, "/scheduling/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/scheduling/**").permitAll()
                .antMatchers("/v2/api-docs","/configuration/**","/swagger*/**","/webjars/**").permitAll()
                .anyRequest().authenticated();
    }


    @Bean
    AuthInterceptor authFeign() {
        return new AuthInterceptor();
    }
}
