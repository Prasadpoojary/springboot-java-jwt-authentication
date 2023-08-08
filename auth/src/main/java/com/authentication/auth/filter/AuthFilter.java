package com.authentication.auth.filter;

import com.authentication.auth.config.UserInfoDetailService;
import com.authentication.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter
{
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoDetailService userInfoDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String authorization=request.getHeader("Authorization");
        String username=null;
        String token=null;

        if(authorization!=null && authorization.startsWith("Bearer "))
        {
             token=request.getHeader("Authorization").substring(7);
             username=jwtService.getUsername(token);
        }

        if(username!=null && jwtService.validateToken(token,userInfoDetailService.loadUserByUsername(username)) && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails user=userInfoDetailService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request,response);
    }
}
