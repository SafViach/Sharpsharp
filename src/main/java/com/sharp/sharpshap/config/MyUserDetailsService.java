package com.sharp.sharpshap.config;

import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.enums.EnumRole;
import com.sharp.sharpshap.repository.RoleRepository;
import com.sharp.sharpshap.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    
    @Autowired
    public MyUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        System.out.println("Attempting to load user with login: " + login);

        Optional<User> userOpt = userRepository.findByLogin(login);
        if (userOpt.isEmpty()) {
            System.out.println("User not found with login: " + login);
            throw new UsernameNotFoundException("пользователь с логином: " + login + " не найден");
        }
        
        User user = userOpt.get();
        System.out.println("Found user: " + user.getLogin());
        System.out.println("User enabled: " + user.isEnable());
        System.out.println("User account non-locked: " + user.isAccountNonLocked());

        Set<String> userRoles = user.getRoles().stream()
                .map(EnumRole::getName)
                .collect(Collectors.toSet());
        System.out.println("User roles: " + userRoles);
        
        return new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            user.getPassword(),
            user.isEnable(),
            true,
            true,
            user.isAccountNonLocked(),
            user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet())
        );
    }
}

