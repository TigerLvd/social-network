package com.highload.architect.soc.network.config;

import com.highload.architect.soc.network.exception.ErrorResponseHandler;
import com.highload.architect.soc.network.security.SimpleTokenProvider;
import com.highload.architect.soc.network.security.TokenAuthenticationFilter;
import com.highload.architect.soc.network.service.AccountInfoService;
import com.highload.architect.soc.network.service.UserService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfiguration {

    public static final String SIGNIN_ENTRY_POINT = "/login";
    public static final String SIGNUP_ENTRY_POINT = "/user/register";
    public static final String SWAGGER_ENTRY_POINT = "/swagger-ui/**";
    public static final String ACTUATOR_ENTRY_POINT = "/actuator/**";
    public static final String API_DOCS_ENTRY_POINT = "/v3/api-docs/**";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/auth/refreshToken";

    private final AccountInfoService accountInfoService;
    private final SimpleTokenProvider tokenProvider;
    private final UserService userService;

    private final ErrorResponseHandler accessDeniedHandler;

    public SecurityConfiguration(final SimpleTokenProvider tokenProvider,
                                 final ErrorResponseHandler accessDeniedHandler,
                                 AccountInfoService accountInfoService,
                                 UserService userService) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.accountInfoService = accountInfoService;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(configurer -> configurer
                        .accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(configurer -> configurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SIGNIN_ENTRY_POINT).permitAll()
                        .requestMatchers(SIGNUP_ENTRY_POINT).permitAll()
                        .requestMatchers(SWAGGER_ENTRY_POINT).permitAll()
                        .requestMatchers(ACTUATOR_ENTRY_POINT).permitAll()
                        .requestMatchers(API_DOCS_ENTRY_POINT).permitAll()
                        .requestMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        buildTokenAuthenticationFilter(accountInfoService, tokenProvider, userService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    protected TokenAuthenticationFilter buildTokenAuthenticationFilter(final AccountInfoService accountInfoService,
                                                                       final SimpleTokenProvider tokenProvider,
                                                                       final UserService userService) {
        return new TokenAuthenticationFilter(accountInfoService, tokenProvider, userService);
    }
}
