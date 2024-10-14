package com.koitourdemo.demo.config;

import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.AuthException;
import com.koitourdemo.demo.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver handlerExceptionResolver;

    private final List<String> AUTH_PERMISSION = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/user/login",
            "/api/user/register",
            "/api/user/get-all",
            "/api/user/forgot-password"
    );

    public boolean checkIsPublicAPI(String uri){
        AntPathMatcher pathMatch = new AntPathMatcher();
        return AUTH_PERMISSION.stream().anyMatch(pattern -> pathMatch.match(pattern, uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isPublicAPI = checkIsPublicAPI(request.getRequestURI());
        if(isPublicAPI){
            filterChain.doFilter(request, response);
        }else{
            String token = getToken(request);
            if(token == null){
                handlerExceptionResolver.resolveException(request, response, null, new AuthException("Empty token!"));
                return;
            }

            User user;
            try{
                user = tokenService.getUserByToken(token);
            }catch (ExpiredJwtException e){
                handlerExceptionResolver.resolveException(request, response, null, new AuthException("Expired token!"));
                return;
            }catch (MalformedJwtException e){
                handlerExceptionResolver.resolveException(request, response, null, new AuthException("Invalid token!"));
                return;
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user, token, user.getAuthorities());
            authenticationToken .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        }
    }

    public String getToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.substring(7);
    }
}
