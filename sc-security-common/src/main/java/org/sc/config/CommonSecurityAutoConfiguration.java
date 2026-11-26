package org.sc.config;

import jakarta.servlet.http.HttpServletResponse;
import org.sc.security.DevByPassAuthenticationFilter;
import org.sc.security.JwtAuthenticationFilter;
import org.sc.security.JwtValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Instant;

@AutoConfiguration
@EnableWebSecurity
@EnableMethodSecurity
public class CommonSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtValidator jwtValidator(@Value("${app.security.jwt.secretkey}") String base64Secret){
        return new JwtValidator(base64Secret);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtValidator jwtValidator){
        return new JwtAuthenticationFilter(jwtValidator);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "app.security.jwt.enabled",havingValue = "true", matchIfMissing = true)
    public SecurityFilterChain securedFilterChain(HttpSecurity http,JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint((req,res,authEx) ->{
                  res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                  res.setContentType("application/json");
                  res.getWriter().write("""
                         {"timestamp": "%s","status":401,"error":"Valid bearer token is required","path":"%s"}
                          """.formatted(Instant.now(),req.getRequestURI()));
        }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health","/error","/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "app.security.jwt.enabled",havingValue = "false")
    public SecurityFilterChain UnSecuredFilterChain(HttpSecurity http,JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .addFilterBefore(new DevByPassAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
                .build();
    }


}
