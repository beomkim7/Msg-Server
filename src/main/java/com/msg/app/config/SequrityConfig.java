package com.msg.app.config;


import com.msg.app.JwtToneken.JwtAccessDeniedHandler;
import com.msg.app.JwtToneken.JwtAuthenticationEntryPoint;
import com.msg.app.JwtToneken.JwtFilter;
import com.msg.app.JwtToneken.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SequrityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;



    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
        http

                //token을 사용하는 방식 csrf는 disable
                .csrf(AbstractHttpConfigurer::disable)
                //HttpServletRequest를 사용하는 요청들에 대한 접근제한
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/api/login").permitAll() //로그인
//                        .requestMatchers("/api/join").permitAll() //회원가입
                        .requestMatchers("/api/**").permitAll() //api 밑에 모든경로 허용 익셉션 적용
                                .requestMatchers("/ws","/sub","/pub").permitAll()
                )
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.accessDeniedHandler(jwtAccessDeniedHandler)
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)

                        )
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
        .addFilterBefore(
                new JwtFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter.class
        );

//        new JwtSecurityConfig(tokenProvider).configure(http);



        return http.build();
    }

}
