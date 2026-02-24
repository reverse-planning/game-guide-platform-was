package io.github.doi02.ena.common.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key key;

    private final long accessTokenValidity = 1000L * 60 * 30; // 30분
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7일

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(Base64.getEncoder().encodeToString(secretKey.getBytes()));
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // access토큰 생성
    public String createAccessToken(Long id, String nickname) {
        Claims claims = Jwts.claims().setSubject(id.toString());
        claims.put("nickname", nickname);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 발행시간
                .setExpiration(new Date(now.getTime() + accessTokenValidity))
                .signWith(key)
                .compact();
    }

    // refresh토큰 생성
    public String createRefreshToken(Long id) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(id.toString())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidity))
                .signWith(key)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 유저 정보(ID)를 꺼내서 스프링 시큐리티 인증 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                "",
                Collections.emptyList()
        );
    }

    // 토큰에서 Subject(유저 ID) 추출
    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    // 토큰 복호화 및 클레임(데이터) 추출
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
