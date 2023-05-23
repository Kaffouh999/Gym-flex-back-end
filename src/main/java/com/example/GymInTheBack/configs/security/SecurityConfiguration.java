package com.example.GymInTheBack.configs.security;

import com.example.GymInTheBack.entities.Authority;
import com.example.GymInTheBack.utils.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type"));
    configuration.setExposedHeaders(Arrays.asList("authorization"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers("/api/auth/**")
          .permitAll()
            .requestMatchers("/ws/**")
            .permitAll()
            .requestMatchers(new AntPathRequestMatcher("/api/members/**", HttpMethod.GET.toString())).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/api/plans/**", HttpMethod.GET.toString())).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/api/equipment/**", HttpMethod.GET.toString())).permitAll()

            .requestMatchers("/api/members/**","/api/payments/**","/api/subscription-members/**","/api/plans/**","/api/assurance-members/**,/api/membersProfile/**")
            .hasAnyAuthority(Authority.MEMBERSHIP.name())
            .requestMatchers("/api/categories/**","/api/sub-categories/**","/api/equipment/**","/api/equipment-items/**","/api/maintinings/**","/api/reforms/**")
            .hasAnyAuthority(Authority.INVENTORY.name())
            .requestMatchers("/api/web/**")
            .permitAll()
           .anyRequest()
            .authenticated()
//complete roles configurations TODO

        .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutUrl("/api/auth/logout")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
    ;
    http.cors();

    return http.build();
  }
}
