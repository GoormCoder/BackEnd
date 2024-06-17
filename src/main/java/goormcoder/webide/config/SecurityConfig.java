package goormcoder.webide.config;

import goormcoder.webide.jwt.JwtProvider;
import goormcoder.webide.util.filter.CustomAccessDeniedHandler;
import goormcoder.webide.util.filter.CustomAuthenticationEntryPointHandler;
import goormcoder.webide.util.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String TEST_WHITE_LIST = "/h2-console/**";
    private static final String[] SWAGGER = {"/swagger-ui/**", "/v3/api-docs/**"};

    private static final String ADMIN = "/admin/**";
    private static final String[] WHITE_LIST = {"/", "/members/join", "/members/login", "/auth/refresh"};

    private final JwtProvider jwtProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(new CustomAuthenticationEntryPointHandler());
                    exception.accessDeniedHandler(new CustomAccessDeniedHandler());
                });;

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(SWAGGER).permitAll()
                .requestMatchers(TEST_WHITE_LIST).permitAll()
                .requestMatchers(WHITE_LIST).permitAll()
                .requestMatchers(ADMIN).hasRole("ADMIN")
                .anyRequest().authenticated());

        // for using H2
        http.headers(headers -> headers.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin
        ));
        
        http.addFilterBefore(new JwtAuthorizationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
