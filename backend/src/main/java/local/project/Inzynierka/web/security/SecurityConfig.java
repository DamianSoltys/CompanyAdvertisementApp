package local.project.Inzynierka.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationsUserDetailService applicationsUserDetailService;

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(ApplicationsUserDetailService applicationsUserDetailService, PasswordEncoder passwordEncoder) {
        this.applicationsUserDetailService = applicationsUserDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/auth/registration*").permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/api/newsletter*").permitAll()
                .antMatchers("/test-cors").permitAll()
                .antMatchers("/test-cors-methods").permitAll()
                .and()
                .httpBasic().and()
                .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(applicationsUserDetailService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .exposedHeaders(HttpHeaders.AUTHORIZATION)
                        .allowedMethods("PUT", "GET", "POST", "PATCH", "DELETE");
            }
        };
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
