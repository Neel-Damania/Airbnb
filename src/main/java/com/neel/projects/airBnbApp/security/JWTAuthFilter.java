package com.neel.projects.airBnbApp.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.neel.projects.airBnbApp.entity.User;
import com.neel.projects.airBnbApp.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter{

    private final JWTService jwtService;
    private final UserService UserService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                
                try{
                    final String requestTokenHeader = request.getHeader("Authorization");
                    
                    if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")){
                        filterChain.doFilter(request, response);
                        return;
                    }

                    String token = requestTokenHeader.split("Bearer ")[1];
                    String email = jwtService.getUserEmailFromToken(token); // Getting email because Refresh Token doesnt contain email, thus throwing exception

                    if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){ 
                        User user = (User)UserService.loadUserByUsername(email);

                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                    filterChain.doFilter(request, response);
                }catch(Exception e){
                    handlerExceptionResolver.resolveException(request, response, null, e);
                }
                
            }
}

