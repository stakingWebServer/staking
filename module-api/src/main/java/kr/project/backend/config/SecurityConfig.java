package kr.project.backend.config;

import kr.project.backend.filter.JwtAuthorizationFilter;
import kr.project.backend.handler.CustomAccessDeniedHandler;
import kr.project.backend.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    /** HttpSecurity set */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler) //403 custom
                .authenticationEntryPoint(customAuthenticationEntryPoint) //401 custom
                .and()
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/swagger-ui/**",
                                         "/swagger-resources/**",
                                         "/v3/api-docs/**",
                                         "/api/v1/user/login",
                                         "/api/v1/user/join",
                                         "/api/v1/user/refresh/authorize",
                                         "/api/v1/user/use-clauses",
                                         "/api/v1/user/use-clauses/before",
                                         "/api/v1/admin/account/accessKey/**",
                                         "/api/v1/common/file/image/**",
                                         "/api/v1/admin/auth",
                                         "/error",
                                         "/")
                                        .permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
                ;
        return http.build();
    }

    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .requestMatchers("/swagger-ui/**","/swagger-resources/**","/v3/api-docs/**","/api/v1/account/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //
                .build();
    }*/

    /** cros 허용 */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /** 더블슬래쉬 허용 */
    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }


}
