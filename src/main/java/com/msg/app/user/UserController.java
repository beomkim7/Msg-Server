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

    @PostMapping("/users")
    public ResponseEntity<UserDTO> join(@RequestBody com.msg.app.user.DTO.UserDTO userDTO)throws Exception {
        int result = userService.addUser(userDTO);
        return ResponseEntity.ok(result == 1 ? userDTO : null);
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<Integer> checkEmail(@PathVariable String email)throws Exception {

        if(!userService.emailValidate(email)){
            throw new Exception("이메일 형식이 맞지 않습니다.");
        }
        int result = userService.checkEmail(email);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/users")
    public ResponseEntity<Integer> update(@AuthenticationPrincipal UserDTO user, @RequestBody UserDTO userDTO)throws Exception {

        System.out.println(user);
        if(user == null) {
            throw new Exception("로그인이 필요합니다.");
        }

        if(userDTO.getName()==null) userDTO.setName(user.getName());
        if(userDTO.getEmail()==null) userDTO.setEmail(user.getEmail());

        user = UserDTO.of(user.getId(),userDTO.getName(), userDTO.getEmail());

        //정보변경후 principal 객체 정보 업데이트
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        userDTO.setId(user.getId());
        int result = userService.changeName(userDTO);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDTO userDTO,@CookieValue(name = "token",required = false) String token)throws Exception {

        if(userDTO == null && userService.addBlackList(token))throw new Exception("로그인이 되어있지 않음");
        userService.addBlack(token);
        HttpHeaders headers = new HttpHeaders();
        if(token != null) {
            headers.add(HttpHeaders.SET_COOKIE, "token=; Max-Age=0; Path=/; HttpOnly");
        }

        SecurityContextHolder.clearContext();

        System.out.println(userDTO+"로그아웃되야돼");

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> logIn(@RequestBody UserDTO userDTO)throws Exception {


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getId(), userDTO.getPw());

        // authenticate 메서드가 실행될때 UserService class 의 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //해당 객체 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(authentication.getPrincipal().getClass().getName());
        // authentication 객체를 createToken 메서드를 통해서 JWT Token을 생성
        String token = tokenProvider.createToken(authentication);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE,tokenProvider.cookie(token).toString());

        //tokendto를 이용해 response body에도 넣어서 리턴
//        return new ResponseEntity<>(new TokenDTO(token), headers, HttpStatus.OK);
//          @AuthenticationPrincipal에 담길 정보는 토큰이 생성될때 해야된다 
//          오늘 뻘짓 너무많이했네 TokenProvider 확인
        return ResponseEntity.ok()
                .headers(headers)
                .body(new TokenDTO(token)
                );
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
