package com.greenfoxacademy.tradebackend.security.config;



import com.greenfoxacademy.tradebackend.security.Jwt.FilterChainExceptionHandler;
import com.greenfoxacademy.tradebackend.security.Jwt.JwtRequestFilter;
import com.greenfoxacademy.tradebackend.security.Jwt.MyUserDetailsService;
import com.greenfoxacademy.tradebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  @Autowired
  private MyUserDetailsService myUserDetailsService;
  @Autowired
  private JwtRequestFilter jwtRequestFilter;
  @Autowired
  private FilterChainExceptionHandler filterChainExceptionHandler;

  private final UserService userService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public WebSecurityConfig(UserService userService,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userService = userService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .addFilterBefore(filterChainExceptionHandler, UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers("/login", "/register", "/register/confirm").permitAll()
        .anyRequest().authenticated().and()
        .oauth2Login().and()
        .exceptionHandling().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);


    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider =
        new DaoAuthenticationProvider();
    provider.setPasswordEncoder(bCryptPasswordEncoder);
    provider.setUserDetailsService(userService);
    return provider;
  }
}
