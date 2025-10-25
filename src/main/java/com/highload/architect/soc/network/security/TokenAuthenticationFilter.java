package com.highload.architect.soc.network.security;

import com.highload.architect.soc.network.constants.SecurityConstants;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.service.UserService;
import com.highload.architect.soc.network.service.AccountInfoService;
import com.highload.architect.soc.network.service.impl.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AccountInfoService accountInfoService;
    private final SimpleTokenProvider tokenProvider;
    private final UserService userService;

    public TokenAuthenticationFilter(final AccountInfoService accountInfoService,
                                     final SimpleTokenProvider tokenProvider,
                                     final UserService userService) {
        this.accountInfoService = accountInfoService;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            SimpleToken tokenFromRequest = tokenProvider.getTokenFromRequest(request);
            UUID userId = tokenFromRequest.getUserId();

            UserInfo userInfo = userService.getById(userId);
            AccountInfo accountInfo = accountInfoService.getById(userId);

            if (accountInfo != null && userInfo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Создаём аутентификацию и устанавливаем в SecurityContext
                UserDetailsImpl userDetails = UserDetailsImpl.build(userInfo, accountInfo);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Логируем ошибку и продолжаем выполнение фильтра
            // Spring Security обработает отсутствие аутентификации
        }
        filterChain.doFilter(request, response);
    }
}
