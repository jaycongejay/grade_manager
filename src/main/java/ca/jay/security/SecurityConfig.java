package ca.jay.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private LoginAccessDeniedHandler ladh;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		http.csrf().disable();
		http.headers().frameOptions().disable();
		
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/register").permitAll()
			.antMatchers("/user/professor/**", "/add").hasRole("PROFESSOR")
			.antMatchers("/user/student/**").hasRole("STUDENT")
			.antMatchers("/view").hasAnyRole("PROFESSOR", "STUDENT")
			.antMatchers("/", "/css/**", "/images/**", "/register").permitAll()
			.antMatchers("/h2-console/**").permitAll()
		.and()
			.formLogin()
			.loginPage("/login")
			.permitAll()
		.and()
			.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login?logout")
			.permitAll()
		.and()
			.exceptionHandling()
			.accessDeniedHandler(ladh);
			
	}
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
}
