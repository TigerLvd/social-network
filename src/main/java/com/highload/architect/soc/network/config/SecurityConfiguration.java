package com.highload.architect.soc.network.config;

import com.highload.architect.soc.network.constants.SecurityConstants;
import com.highload.architect.soc.network.exception.ErrorResponseHandler;
import com.highload.architect.soc.network.repository.AccountInfoRepository;
import com.highload.architect.soc.network.repository.UserInfoRepository;
import com.highload.architect.soc.network.security.SimpleTokenProvider;
import com.highload.architect.soc.network.security.TokenAuthenticationFilter;
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

    private final AccountInfoRepository accountInfoRepository;
    private final SimpleTokenProvider tokenProvider;
    private final UserInfoRepository userInfoRepository;

    private final ErrorResponseHandler accessDeniedHandler;

    public SecurityConfiguration(final SimpleTokenProvider tokenProvider,
                                 final ErrorResponseHandler accessDeniedHandler,
                                 AccountInfoRepository accountInfoRepository,
                                 UserInfoRepository userInfoRepository) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.accountInfoRepository = accountInfoRepository;
        this.tokenProvider = tokenProvider;
        this.userInfoRepository = userInfoRepository;
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
                        .requestMatchers(SecurityConstants.SIGNIN_ENTRY_POINT).permitAll()
                        .requestMatchers(SecurityConstants.SIGNUP_ENTRY_POINT).permitAll()
                        .requestMatchers(SecurityConstants.SWAGGER_ENTRY_POINT).permitAll()
                        .requestMatchers(SecurityConstants.API_DOCS_ENTRY_POINT).permitAll()
                        .requestMatchers(SecurityConstants.TOKEN_REFRESH_ENTRY_POINT).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        buildTokenAuthenticationFilter(accountInfoRepository, tokenProvider, userInfoRepository),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    protected TokenAuthenticationFilter buildTokenAuthenticationFilter(final AccountInfoRepository accountInfoRepository,
                                                                       final SimpleTokenProvider tokenProvider,
                                                                       final UserInfoRepository userInfoRepository) {
        return new TokenAuthenticationFilter(accountInfoRepository, tokenProvider, userInfoRepository);
    }
}
