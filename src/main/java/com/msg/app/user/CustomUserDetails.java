//package com.msg.app.user;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//
//import java.util.Collection;
//
//
//public class CustomUserDetails extends User {
//    //username(Email), password, authorities
//    private final String nickname;  //닉네임
//    private final Long memberId;    //PK
//
//    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
//                            String nickname, long memberId) {
//        super(
//                username, password, authorities
//        );
//        this.nickname = nickname;
//        this.memberId = memberId;
//    }
//
//    public String getNickname() {
//        return nickname;
//    }
//
//    public Long getUserId() {
//        return memberId;
//    }
//}
