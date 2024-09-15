package com.msg.app.user;

import lombok.extern.java.Log;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class UserService implements UserDetailsService {
    private final NonSocialMemberRepository nonSocialmemberRepository;
    private final SocialMemberRepository socialMemberRepository;

    public MemberService(NonSocialMemberRepository nonSocialmemberRepository, SocialMemberRepository socialMemberRepository) {
        this.nonSocialmemberRepository = nonSocialmemberRepository;
        this.socialMemberRepository = socialMemberRepository;
    }

    public Long join (NonSocialMemberSaveForm nonSocialMemberSaveForm) throws NoSuchAlgorithmException {
        int login_type = nonSocialMemberSaveForm.getLogin_type();
        //중복 처리 한번더 검증
        validateDuplicateEmail(nonSocialMemberSaveForm,login_type);
        validateDuplicateName(nonSocialMemberSaveForm,login_type);
        Long user_id = -1L;

        //로그인 타입별 다른 회원가입 로직
        switch(login_type) {
            case 0: // non-social 회원가입의 경우
            {
                NonSocialMember member = new NonSocialMember(); // DAO (Entity)로 바꾸는 작업
                member.setUser_name(nonSocialMemberSaveForm.getUser_name());
                member.setUser_email(nonSocialMemberSaveForm.getUser_email());
                member.setUser_pw(nonSocialMemberSaveForm.getUser_pw());
                NonSocialMember saveMember = nonSocialmemberRepository.save(member);
                user_id = saveMember.getUser_id();
                return user_id;
            }
            case 1: //social 회원가입의 경우 -> 요청 필요
            {
                throw new NotYetImplementException("해당 요청은 아직 구현되지 않았습니다.");
            }
        }
        return user_id; // non valid request, return -1
    }

    public void validateDuplicateName(NonSocialMemberSaveForm memberForm, Integer login_type){
        MemberRepository memberRepository = null;
        switch(login_type){
            case 0: {
                memberRepository = nonSocialmemberRepository;
                break;
            }
            case 1: {
                memberRepository = socialMemberRepository;
                break;
            }
        }
        log.info("check = {}",memberRepository.findByName("hi"));
        memberRepository.findByName(memberForm.getUser_name()).ifPresent(m->{
            log.info("name check");
            throw new IllegalStateException("이미 존재하는 회원 닉네임입니다.");
        });
    }

    public void validateDuplicateEmail(NonSocialMemberSaveForm memberForm, Integer login_type){
        MemberRepository memberRepository = null;
        switch(login_type){
            case 0: {
                memberRepository = nonSocialmemberRepository;
                break;
            }
            case 1: {
                memberRepository = socialMemberRepository;
                break;
            }
        }
        memberRepository.findByEmail(memberForm.getUser_email()).ifPresent(m->{
            throw new IllegalStateException("이미 존재하는 회원 이메일입니다.");
        });
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<NonSocialMember> nonSocialMember = nonSocialmemberRepository.findByEmail(userEmail);

        if (nonSocialMember.isPresent()) {
            NonSocialMember member = nonSocialMember.get();
            Log.info("member info in loadByUsername method = {}", member.getAuth_id());
            return new CustomUserDetails(member.getAuth_id(),member.getUser_email(),member.getUser_pw(),true,false );
        } else {
            throw new UsernameNotFoundException("User not found with userEmail: " + userEmail);
        }
    }
}
