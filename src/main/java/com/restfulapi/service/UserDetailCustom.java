package com.restfulapi.service;

import com.restfulapi.entity.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailCustom implements UserDetailsService {
    private final UserService userService;

    public UserDetailCustom(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("Không tìm thấy người dùng với email: " + username);
            }
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole().getName());

        String roleName=user.getRole().getName();


        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+roleName))
        );
    }
}
