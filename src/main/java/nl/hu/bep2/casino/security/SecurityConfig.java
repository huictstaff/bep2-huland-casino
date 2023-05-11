package nl.hu.bep2.casino.security;

import nl.hu.bep2.casino.security.application.UserService;
import nl.hu.bep2.casino.security.presentation.filter.JwtAuthenticationFilter;
import nl.hu.bep2.casino.security.presentation.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class configures authentication and authorisation
 * for the application.
 *
 * The configure method
 * - permits all POSTs to the registration and login endpoints
 * - requires all requests other URLs to be authenticated
 * - sets up JWT-based authentication and authorisation
 * - enforces sessions to be stateless (see: REST)
 *
 * We make sure user data is securely stored
 * by utilizing a BcryptPasswordEncoder.
 * We don't store passwords, only hashes of passwords.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    public final static String LOGIN_PATH = "/login";
    public final static String REGISTER_PATH = "/register";

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expiration-in-ms}")
    private Integer jwtExpirationInMs;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http.cors().and()
		    .csrf().disable()
		    .authorizeHttpRequests()
		    .requestMatchers(HttpMethod.POST, REGISTER_PATH).permitAll()
		    .requestMatchers(HttpMethod.POST, LOGIN_PATH).permitAll()
		    .anyRequest().authenticated()
		    .and()
		    .addFilterBefore(
				    new JwtAuthenticationFilter(
						    LOGIN_PATH,
						    this.jwtSecret,
						    this.jwtExpirationInMs,
						    authenticationManager
				    ),
				    UsernamePasswordAuthenticationFilter.class
		    )
		    .addFilter(new JwtAuthorizationFilter(this.jwtSecret, authenticationManager))
		    .sessionManagement()
		    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
		           .userDetailsService(userDetailsService)
		           .passwordEncoder(passwordEncoder)
		           .and()
		           .build();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
