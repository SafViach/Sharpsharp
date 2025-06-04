package com.sharp.sharpshap.config;

import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.repository.RoleRepository;
import com.sharp.sharpshap.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {//загружает автоматом из базы при входе
       User user =  userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("пользователь с логином: "
                + login + " не найден"));
       return new org.springframework.security.core.userdetails.User(
               user.getLogin(),
               user.getPassword(),
               user.isEnable(),
               true,
               true,
               user.isAccountNonLocked(),
               user.getRoles().stream()
                       .map(role -> new SimpleGrantedAuthority((role.getName())))
                       .collect(Collectors.toSet())
       );
    }
}
