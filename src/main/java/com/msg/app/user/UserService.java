package com.msg.app.user;

import com.msg.app.user.DTO.UserDTO;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserMapper userMapper;

    @Autowired
    public UserService(final UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Override
    public UserDetails loadUserByUsername(String id) {
        try {
            Optional<UserDTO> optional = userMapper.getUserById(id);
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

    public void updatePasswords() throws  Exception{
        List<UserDTO> users = userMapper.getAllUsers(); // 모든 사용자 정보를 가져오는 메서드
        for (UserDTO user : users) {
            String encodedPassword = passwordEncoder.encode(user.getPw()); // 비밀번호를 인코딩
            user.setPw(encodedPassword); // 인코딩된 비밀번호를 설정
            userMapper.updateUser(user); // 데이터베이스에 사용자 정보를 업데이트
        }
    }
}
