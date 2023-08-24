package nl.hu.bep2.casino.security;

import nl.hu.bep2.casino.security.presentation.filter.JwtAuthenticationFilter;
import nl.hu.bep2.casino.security.presentation.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * This class configures authentication and authorisation
 * for the application.
 * <p>
 * The configure method
 * - permits all POSTs to the registration and login endpoints
 * - requires all requests other URLs to be authenticated
 * - sets up JWT-based authentication and authorisation
 * - enforces sessions to be stateless (see: REST)
 * <p>
 * We make sure user data is securely stored
 * by utilizing a BcryptPasswordEncoder.
 * We don't store passwords, only hashes of passwords.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
	private static final String LOGIN_PATH = "/login";
	private static final String REGISTER_PATH = "/register";
	@Value("${security.jwt.expiration-in-ms}")
	private Integer jwtExpirationInMs;
	@Value("${security.jwt.secret}")
	private String jwtSecret;

	@Bean
	protected AuthenticationManager authenticationManager(final PasswordEncoder passwordEncoder, final UserDetailsService userDetailsService) {
		final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(provider);
	}

	@Bean
	protected SecurityFilterChain filterChain(final HttpSecurity http, final AuthenticationManager authenticationManager) throws Exception {
		http.cors(Customizer.withDefaults())
		    .csrf(AbstractHttpConfigurer::disable)
		    .authorizeHttpRequests(r -> r
				    .requestMatchers(antMatcher(POST, REGISTER_PATH)).permitAll()
				    .requestMatchers(antMatcher(POST, LOGIN_PATH)).permitAll()
				    .requestMatchers(antMatcher("/error")).anonymous()
				    .anyRequest().authenticated()
		    )
		    .addFilterBefore(new JwtAuthenticationFilter(
				    LOGIN_PATH,
				    jwtSecret,
				    jwtExpirationInMs,
				    authenticationManager
		    ), UsernamePasswordAuthenticationFilter.class)
		    .addFilter(new JwtAuthorizationFilter(jwtSecret, authenticationManager))
		    .sessionManagement(s -> s.sessionCreationPolicy(STATELESS));
		return http.build();
	}

	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
