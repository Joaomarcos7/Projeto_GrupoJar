package br.edu.ifpb.pweb2.Jar.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/coordenadores/**").hasRole("ADMIN")
                        .requestMatchers("/empresas/**").hasRole("EMPRESA")
                        .requestMatchers("/alunos/**").hasRole("ALUNO")
                        .requestMatchers("/css/**", "/imagens/**", "/").permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout((logout) -> logout.logoutUrl("/auth/logout"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails aluno = User.withUsername("aluno").password(passwordEncoder().encode("senhaAluno")).roles("ALUNO").build();
        UserDetails empresa = User.withUsername("empresa").password(passwordEncoder().encode("senhaEmpresa")).roles("EMPRESA").build();
        UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("admin123")).roles("ADMIN").build();

        // Evita duplicação dos usuários no banco
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        if (!users.userExists(aluno.getUsername())) {
            users.createUser(aluno);
            users.createUser(empresa);
            users.createUser(admin);
        }

        return users;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
