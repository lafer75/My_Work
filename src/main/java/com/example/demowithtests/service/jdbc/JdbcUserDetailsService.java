package com.example.demowithtests.service.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

public class JdbcUserDetailsService implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT username, password, role, role_2 FROM users_role WHERE username = ?";
        Map<String, Object> row = jdbcTemplate.queryForMap(sql, username);

        String password = (String) row.get("password");
        String role = (String) row.get("role");
        String role2 = (String) row.get("role_2");

        String passwordWithNoop = "{noop}" + password;

        List<String> roles = new ArrayList<>();
        roles.add(role);
        if (role2 != null) {
            roles.add(role2);
        }
        UserDetails userDetails = User.builder()
                .username(username)
                .password(passwordWithNoop)
                .roles(roles.toArray(new String[0]))
                .build();

        // Add debug logs
        System.out.println("User Roles: " + userDetails.getUsername() + " " + userDetails.getPassword() + " " + userDetails.getAuthorities());

        return userDetails;
    }
}
