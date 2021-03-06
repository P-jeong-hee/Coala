package com.clone.finalProject.security;


import com.clone.finalProject.security.filter.FormLoginFilter;
import com.clone.finalProject.security.filter.JwtAuthFilter;
import com.clone.finalProject.security.jwt.HeaderTokenExtractor;
import com.clone.finalProject.security.provider.FormLoginAuthProvider;
import com.clone.finalProject.security.provider.JWTAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTAuthProvider jwtAuthProvider;
    private final HeaderTokenExtractor headerTokenExtractor;

    public WebSecurityConfig(
            JWTAuthProvider jwtAuthProvider,
            HeaderTokenExtractor headerTokenExtractor
    ) {
        this.jwtAuthProvider = jwtAuthProvider;
        this.headerTokenExtractor = headerTokenExtractor;
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(formLoginAuthProvider())
                .authenticationProvider(jwtAuthProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        web
                .ignoring()
                .antMatchers("/h2-console/**")
                .antMatchers("/static/**","/css/**","/js/**","/images/**")
                .antMatchers("/swagger-ui/index.html", "/webjars/**", "/swagger/**")
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers();


        http
                .cors()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();


        /* 1.
         * UsernamePasswordAuthenticationFilter 이전에 FormLoginFilter, JwtFilter 를 등록합니다.
         * FormLoginFilter : 로그인 인증을 실시합니다.
         * JwtFilter       : 서버에 접근시 JWT 확인 후 인증을 실시합니다.
         */
        http
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                //시큐리티 인증 예외처리 추가
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.authorizeRequests()
                // 그 외 어떤 요청이든 ''
                .anyRequest().permitAll()
                .and()
                // [로그아웃 기능]
                .logout()
                // 로그아웃 요청 처리 URL
                .logoutUrl("/user/logout")
                .permitAll();

    }

    @Bean
    public FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter formLoginFilter = new FormLoginFilter(authenticationManager());
        formLoginFilter.setFilterProcessesUrl("/user/login");
        formLoginFilter.setAuthenticationSuccessHandler(formLoginSuccessHandler());
        formLoginFilter.afterPropertiesSet();
        return formLoginFilter;
    }

    @Bean
    public FormLoginSuccessHandler formLoginSuccessHandler() {
        return new FormLoginSuccessHandler();
    }

    @Bean
    public FormLoginAuthProvider formLoginAuthProvider() {
        return new FormLoginAuthProvider(encodePassword());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {return new AuthenticationEntryPoint() {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws IOException, ServletException {

        }
    };}

    private JwtAuthFilter jwtFilter() throws Exception {
        List<String> skipPathList = new ArrayList<>();

        // 회원 관리 API 허용
        skipPathList.add("POST,/user/**");
        skipPathList.add("GET,/user/**");
        skipPathList.add("GET,/post/**");
        skipPathList.add("POST,/images/upload");
        skipPathList.add("GET,/answer/**");
        skipPathList.add("GET,/tag/**");
        skipPathList.add("GET,/category/**");
        skipPathList.add("DELETE,/image/delete");
        skipPathList.add("GET,/comment/**");


        skipPathList.add("GET,/test/**");
        skipPathList.add("POST,/test/**");
        skipPathList.add("GET,/read/**");

        //성능test
//        skipPathList.add("DELETE,/post/delete/**");

        //소켓통신을 위한 허용
        skipPathList.add("GET,/ws-coala/**");
        skipPathList.add("POST,/ws-coala/**");
        skipPathList.add("GET,/mainchat/**");
        skipPathList.add("GET,/postchat/**");

        skipPathList.add("GET,/app/**");
        skipPathList.add("POST,/app/**");

        skipPathList.add("GET,/profile");
        skipPathList.add("GET,/");

        // Swagger
//        skipPathList.add("GET, /swagger-ui/index.html");
//        skipPathList.add("GET, /swagger/**");
//        skipPathList.add("GET, /swagger*/**");
//        skipPathList.add("GET, /swagger-resources/**");
//        skipPathList.add("GET, /webjars/**");
//        skipPathList.add("GET, /v2/**");
//        skipPathList.add("GET, /api/v2-docs");
//        skipPathList.add("GET, /configuration/**");
//        skipPathList.add("GET, /static/**");
//        skipPathList.add("GET, /css/**");
//        skipPathList.add("GET, /js/**");
//        skipPathList.add("GET, /images/**");



        FilterSkipMatcher matcher = new FilterSkipMatcher(
                skipPathList,
                "/**"
        );

        JwtAuthFilter filter = new JwtAuthFilter(
                matcher,
                headerTokenExtractor
        );
        filter.setAuthenticationManager(super.authenticationManagerBean());

        return filter;
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
