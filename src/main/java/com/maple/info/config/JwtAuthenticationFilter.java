package com.maple.info.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import com.maple.info.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 인증 없이 접근 허용할 URL 리스트
    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/api/menu/list",
            "/api/notice/gitlab",
            "/api/auth/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
    	
    	String requestURI = request.getRequestURI();

        // permitAll URL이면 토큰 검증 없이 통과
        for (String pattern : EXCLUDE_URLS) {
            if (pathMatcher.match(pattern, requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        
        // 인증 필요 URL: Authorization 헤더 확인
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (!jwtTokenProvider.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401 반환
                return;
            }
            // 토큰 유효 -> SecurityContext에 인증 객체 설정
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userId, null, Collections.singletonList(new SimpleGrantedAuthority("USER"))
            );
            // //Security에서 403 대신 정상적으로 200 반환
            SecurityContextHolder.getContext().setAuthentication(auth); // permitAll URL은 그대로 토큰 검증 없이 통과
            
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401 반환
            return;
        }
        // 토큰 유효하면 통과
        filterChain.doFilter(request, response);
    }
}
