package com.tnbin.practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 스프링 시큐리티는 사용자 인증(로그인 작업)시 비밀번호에 대해 단방향 해시 암호화를 진행하여 저장되어 있는
     * 비밀번호와 대조한다.
     * <p>양방향 : 대칭키 / 비대칭키 || 단방향 : 해시</p>
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* 인가 작업 설정 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/login").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated());
        /* 커스텀 로그인 페이지 경로 설정 */
        http.formLogin((auth) -> auth.loginPage("/login")
                .loginProcessingUrl("/loginProc").permitAll());
        /* POST 요청시, CSRF 토큰도 함꼐 전달해야 로그인 진행이 됨. 따라서 우선적으로 비활성화 */
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}