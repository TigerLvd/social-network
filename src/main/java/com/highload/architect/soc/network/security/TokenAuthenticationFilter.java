package com.highload.architect.soc.network.security;

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
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        SimpleToken tokenFromRequest = tokenProvider.getTokenFromRequest(request);
        UUID userId = tokenFromRequest.getUserId();

        UserInfo userInfo = userInfoRepository.findUserInfoById(userId);
        AccountInfo accountInfo = accountInfoRepository.getAccountInfoById(userId);

        if (accountInfo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Создаём аутентификацию и устанавливаем в SecurityContext
            UserDetailsImpl userDetails = UserDetailsImpl.build(userInfo, accountInfo);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}
