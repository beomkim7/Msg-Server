package com.msg.app.user;

import com.msg.app.JwtToneken.JwtFilter;
import com.msg.app.JwtToneken.TokenProvider;
import com.msg.app.user.DTO.TokenDTO;
import com.msg.app.user.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> logIn(@RequestBody UserDTO userDTO)throws Exception {
//        userService.updatePasswords();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getId(), userDTO.getPw());

        // authenticate 메서드가 실행될때 UserService class 의 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //해당 객체 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // authentication 객체를 createToken 메서드를 통해서 JWT Token을 생성
        String token = tokenProvider.createToken(authentication);

        // reponse header에 jwt토큰을 넣어줌
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + token);
        headers.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token);
        //tokendto를 이용해 response body에도 넣어서 리턴
        return new ResponseEntity<>(new TokenDTO(token), headers, HttpStatus.OK);

    }
}
