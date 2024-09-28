package com.msg.app.user;

import com.msg.app.user.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
public class UserLoginService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserLoginService.class);
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

//    public UserLoginService(final UserMapper userMapper, final PasswordEncoder passwordEncoder) {
//        this.userMapper = userMapper;
//        this.passwordEncoder = passwordEncoder;
//    }
    @Override
    public UserDetails loadUserByUsername(String id) {

        try {
            Optional<UserDTO> optional = userMapper.login(id);
            UserDTO userDTO = optional
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + id));
            return createUser(userDTO);
        } catch (UsernameNotFoundException e) {
            log.error("사용자를 찾을 수 없다: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("사용자 로드 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("사용자 로드 중 오류 발생", e);
        }
    }

    public User createUser(UserDTO userDTO) {
        if (!userDTO.getEnabled()) {
            throw new UsernameNotFoundException(userDTO.getId() + "가 비활성화 상태입니다.");
        }
        if (userDTO.getRoleVOS() == null) {
            userDTO.setRoleVOS(new ArrayList<>()); // 빈 리스트로 초기화
        }

        List<GrantedAuthority> authorities = userDTO.getRoleVOS().stream()
                .filter(role -> role.getRoleName() != null) // null 체크 추가
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());


        return new User(userDTO.getId(), userDTO.getPw(), authorities); // 비밀번호 암호화 고려 필요
    }


    //DB 비밀번호 인코딩안됐을때 로직
//    public int updatePassword(UserDTO userDTO) throws Exception {
//        UserDTO existingUser = userMapper.findUserById(userDTO.getId());
//
//        // 비밀번호가 암호화되지 않은 경우에만 업데이트
//        if (existingUser != null && !ispassword(existingUser.getPw())) {
//            existingUser.setPw(passwordEncoder.encode(userDTO.getPw())); // 비밀번호 암호화
//            userMapper.updateUser(existingUser); // 데이터베이스 업데이트
//        }
//        return userMapper.updateUser(existingUser);
//    }

    private boolean ispassword(String password) {
        return password!=null && password.length() == 60;
    }


}
