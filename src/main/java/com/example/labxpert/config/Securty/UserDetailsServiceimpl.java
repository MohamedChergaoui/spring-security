package com.example.labxpert.config.Securty;

import com.example.labxpert.Dtos.UserDto;
import com.example.labxpert.Service.Impl.UserServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
@Service
public class UserDetailsServiceimpl implements UserDetailsService {
    private UserServiceImpl userService;

    public UserDetailsServiceimpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto user = userService.getByEmailobj(email);
        if (user == null) throw new UsernameNotFoundException("User Not Found");
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
        authorities.add(authority);
        User userDetails = new User(user.getEmail(), user.getPassword(), authorities);
        return userDetails;
    }
}
