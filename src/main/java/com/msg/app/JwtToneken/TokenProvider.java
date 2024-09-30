package com.msg.app.JwtToneken;

import com.msg.app.user.DTO.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private String secret;
    private final long tokenValidityInMilliseconds;
    private Key key;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.token-validity-in-seconds}") long tokenValidityInseconds) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInseconds * 1000;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret); // Base64 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes); // HS512에 맞는 키 생성
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        UserDTO principal = (UserDTO) authentication.getPrincipal();
        System.out.println(principal+"여기냐 ?");
        long now = (new Date()).getTime();
        Date expiration = new Date(now + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("name",principal.getName())
                .claim("email",principal.getEmail())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiration)
                .compact();
    }

//    public Authentication getauAuthentication(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        User principal = new User(claims.getSubject(), "", authorities);
//        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
//    }

    public Authentication getauAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 권한을 가져올 때 null 체크
        Collection<? extends GrantedAuthority> authorities;
        String authoritiesString = claims.get(AUTHORITIES_KEY) != null ? claims.get(AUTHORITIES_KEY).toString() : "";

        // 권한 정보가 없을 경우 기본값 설정
        if (authoritiesString.isEmpty()) {
            authorities = Collections.emptyList(); // 기본값으로 비어있는 권한 리스트를 설정
        } else {
            authorities = Arrays.stream(authoritiesString.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        // 주체(subject) 가져오기
        String subject = claims.getSubject();
        String name = claims.get("name", String.class);
        String email = claims.get("email", String.class);
        UserDTO principal = new UserDTO(subject, "", name, email, true, null, null);

        // UsernamePasswordAuthenticationToken 반환
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.error("잘못된 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            logger.error("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            logger.error("지원하지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }
}
