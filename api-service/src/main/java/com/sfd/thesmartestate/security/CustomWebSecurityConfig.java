package com.sfd.thesmartestate.security;


import com.sfd.thesmartestate.common.Constants;
import com.sfd.thesmartestate.multitenancy.filters.TenantFilter;
import com.sfd.thesmartestate.users.services.LoginDetailsService;
import com.sfd.thesmartestate.users.services.UserService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@Configuration
@SuppressFBWarnings("EI_EXPOSE_REP")
public class CustomWebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TenantFilter tenantFilter;
    private final LoginDetailsService loginDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("#{'${public.urls}'.split(',')}")
    private List<String> publicUrls;

    public CustomWebSecurityConfig(final JwtAuthenticationFilter jwtAuthenticationFilter,
                                   final LoginDetailsService loginDetailsService,
                                   final BCryptPasswordEncoder bCryptPasswordEncoder,
                                   final TenantFilter tenantFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.loginDetailsService = loginDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tenantFilter = tenantFilter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(publicUrls.toArray(new String[0]))
                .requestMatchers("/actuator**")
                .requestMatchers("/swagger-ui**")
                .requestMatchers("/tehsmartestateapi**")
                .requestMatchers("/v3/api-docs**")
                .requestMatchers(Constants.TOKEN_URL);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        .anyRequest()
                        .authenticated())
                .userDetailsService(loginDetailsService)
                .sessionManagement(sessionManagementConfigure())
                .addFilterBefore(tenantFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public Customizer<SessionManagementConfigurer<HttpSecurity>> sessionManagementConfigure() {
        Customizer<SessionManagementConfigurer<HttpSecurity>> customizer = Customizer.withDefaults();
        SessionManagementConfigurer<HttpSecurity> sessionManagementConfigurer = new SessionManagementConfigurer<>();
        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        customizer.customize(sessionManagementConfigurer);
        return customizer;
    }

    @Bean("authenticationManager")
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(loginDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }
}
