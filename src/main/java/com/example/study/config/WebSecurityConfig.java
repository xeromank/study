package com.example.study.config;

import com.fasterxml.jackson.core.filter.TokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/swagger-ui/**", "/h2-console", "/h2-console/**"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        String[] permitAll = {"/user/**", "/enums", "/enums/**"};

        http.headers().frameOptions().disable()
            .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers(permitAll).permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().csrf().disable()
            ;
   		return http.build();

//        http.addFilterBefore(encodingFilter, CsrfFilter.class);
//        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(exceptionHandlerFilter, TokenFilter.class);
    }
}
