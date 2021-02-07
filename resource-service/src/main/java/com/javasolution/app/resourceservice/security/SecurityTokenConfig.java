package com.javasolution.app.resourceservice.security;

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
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilterAfter(new JwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/resources/**").hasAnyRole("ADMIN","STAFF","RESOURCE")
                .antMatchers(HttpMethod.GET, "/resources/**").hasAnyRole("ADMIN","STAFF","RESOURCE","CUSTOMER")
                .antMatchers(HttpMethod.DELETE, "/resources/**").hasAnyRole("ADMIN","STAFF","RESOURCE")
                .antMatchers(HttpMethod.POST, "/slots").hasAnyRole("ADMIN","STAFF","RESOURCE")
                .antMatchers(HttpMethod.POST, "/slots/**").hasAnyRole("ADMIN","STAFF","RESOURCE","CUSTOMER")
                .antMatchers(HttpMethod.GET, "/slots/**").hasAnyRole("ADMIN","STAFF","RESOURCE","CUSTOMER")
                .antMatchers(HttpMethod.DELETE, "/slots/**").hasAnyRole("ADMIN","STAFF","RESOURCE")
                .antMatchers("/v2/api-docs","/configuration/**","/swagger*/**","/webjars/**").permitAll()
                .anyRequest().authenticated();
    }


    @Bean
    AuthInterceptor authFeign() {
        return new AuthInterceptor();
    }
}
