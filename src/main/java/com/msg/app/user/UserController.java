package com.msg.app.user;

import com.msg.app.JwtToneken.JwtFilter;
import com.msg.app.JwtToneken.TokenProvider;
import com.msg.app.user.DTO.TokenDTO;
import com.msg.app.user.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private static  final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserLoginService userLoginService;

    @PostMapping("/join")
    public ResponseEntity<UserDTO> join(@RequestBody UserDTO userDTO)throws Exception {
        int result = userService.addUser(userDTO);
        return ResponseEntity.ok(result == 1 ? userDTO : null);
    }

    @PutMapping("/users")
    public ResponseEntity<UserDTO> update(@AuthenticationPrincipal User user, @RequestBody UserDTO userDTO)throws Exception {
        logger.info(user+"김범서");
        if(user == null) {
            throw new Exception("로그인이 필요합니다.");
        }



        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> logIn(@RequestBody UserDTO userDTO)throws Exception {


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

//    //DB 비밀번호 인코딩안됐을때 로직
//    @PutMapping("/pass")
//    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO)throws Exception {
//
//        int result = userService.updatePassword(userDTO);
//
//        return ResponseEntity.ok(result == 1 ? userDTO : null);
//    }

}
