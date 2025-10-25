package com.highload.architect.soc.network.security;

import com.highload.architect.soc.network.constants.SecurityConstants;
import com.highload.architect.soc.network.model.AccountInfo;
import com.highload.architect.soc.network.model.SimpleToken;
import com.highload.architect.soc.network.model.UserInfo;
import com.highload.architect.soc.network.repository.AccountInfoRepository;
import com.highload.architect.soc.network.repository.UserInfoRepository;
import com.highload.architect.soc.network.service.UserDetailsImpl;
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
    private final SimpleTokenProvider tokenProvider;
    private final UserInfoRepository userInfoRepository;
    private final AccountInfoRepository accountInfoRepository;

    public TokenAuthenticationFilter(final AccountInfoRepository accountInfoRepository,
                                     final SimpleTokenProvider tokenProvider,
                                     final UserInfoRepository userInfoRepository) {
        this.accountInfoRepository = accountInfoRepository;
        this.tokenProvider = tokenProvider;
        this.userInfoRepository = userInfoRepository;
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

            UserInfo userInfo = userInfoRepository.findById(userId).orElse(null);
            AccountInfo accountInfo = accountInfoRepository.findById(userId).orElse(null);

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
