package com.sharp.sharpshap.config;

import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.enums.EnumRole;
import com.sharp.sharpshap.repository.RoleRepository;
import com.sharp.sharpshap.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        logger.info("MyUserDetailsService: loadUserByUsername ---Получение логина ->" + login);

        logger.info("MyUserDetailsService: loadUserByUsername ---Поиск User findByLogin(login) ->" + login);

        Optional<User> userOpt = userRepository.findByLogin(login);
        User user = userOpt.orElseThrow(() ->
                new UsernameNotFoundException("Пользователь с логином " + login + " не найден"));

        logger.info("Пользватель с логином " + login +" найден" + user.getLastName() +" "+ user.getFirstName());

        logger.info("Получение ролей пользователя (необходимо переделать)");
        Set<String> userRoles = user.getRoles().stream()
                .map(EnumRole::getName)
                .collect(Collectors.toSet());
        System.out.println("User roles: " + userRoles);
        logger.info("Роли пользователя :");
        userRoles.stream().forEach(role -> logger.info(role));

        
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

