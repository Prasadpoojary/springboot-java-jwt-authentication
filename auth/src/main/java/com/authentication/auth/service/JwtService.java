package com.authentication.auth.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService
{
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public String generateToken(String email)
    {
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,email);
    }


    private String createToken(Map<String,Object> claims,String email)
    {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(signInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key signInKey()
    {
        byte[] secretByte= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(secretByte);
    }

    public Boolean validateToken(String token, UserDetails userDetails)
    {
        return getUsername(token).equals(userDetails.getUsername()) && !getExpiration(token).before(new Date());
    }

    public String getUsername(String token)
    {
        return extractClaims(token,Claims::getSubject);
    }

    private Date getExpiration(String token)
    {
        return extractClaims(token,Claims::getExpiration);
    }



    private <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
        Claims claims = getAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaims(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(signInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
