package com.example.coffeeshop.config;

import com.example.coffeeshop.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SpringSecurity {

    private final CustomUserDetailsService userDetailsService;

    public SpringSecurity(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Mã hoá mật khẩu bằng BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình authentication provider dùng CustomUserDetailsService
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Cấu hình bảo mật HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF để tránh lỗi khi post form (trong demo)
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        // Các trang được truy cập công khai
                        .requestMatchers("/", "/login", "/register/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // Trang chỉ ADMIN mới vào được
                        .requestMatchers("/users").hasRole("ADMIN")
                        // Các request còn lại ai cũng có thể xem (vd: /products)
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")                  // Trang đăng nhập
                        .loginProcessingUrl("/login")         // URL xử lý form login
                        .defaultSuccessUrl("/products", true) // ✅ Sau khi login, chuyển về /products
                        .failureUrl("/login?error=true")      // Khi sai thông tin
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true") // ✅ Sau khi logout, quay lại trang login
                        .permitAll()
                );

        return http.build();
    }
}
