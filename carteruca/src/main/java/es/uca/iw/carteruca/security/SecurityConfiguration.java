package es.uca.iw.carteruca.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import es.uca.iw.carteruca.views.login.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/layout/**/*.png")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/layout/**/*.svg")).permitAll()
        );

        // Llamar a la configuración de Vaadin primero
        setLoginView(http, LoginView.class);

        http.formLogin(form -> form
                .successHandler((request, response, authentication) -> {
                    String redirectUrl = "/home"; // Redirección predeterminada

                    if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_Admin"))) {
                        redirectUrl = "/home-admin";
                    }

//                    authentication.getAuthorities().forEach(auth ->  System.out.println("Authority: " + auth.getAuthority())
//                    ); //ver mi rol
                    response.sendRedirect(redirectUrl);
                })
                .permitAll()
        );

        super.configure(http);
    }
}
