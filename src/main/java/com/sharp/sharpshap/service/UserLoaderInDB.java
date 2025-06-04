package com.sharp.sharpshap.service;

import com.sharp.sharpshap.enums.EnumRole;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.repository.RoleRepository;
import com.sharp.sharpshap.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserLoaderInDB implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserLoaderInDB(UserRepository userRepository, PasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0){
            System.out.println("Загружаем пользователя");
            EnumRole userRoleUser = roleRepository.findByName("ROLE_USER").orElse(null);
            EnumRole userRoleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);

            if (userRoleAdmin == null){
                userRoleAdmin = new EnumRole();
                userRoleAdmin.setName("ROLE_ADMIN");
            }

            if (userRoleUser == null){
                userRoleUser = new EnumRole();
                userRoleUser.setName("ROLE_USER");


                roleRepository.save(userRoleUser);
            }
            User user = new User();
            user.setLogin("Slava");
            user.setFirstName("Slava");
            user.setLastName("Safonau");
            user.getRoles().add(userRoleUser);
            user.getRoles().add(userRoleAdmin);
            user.setSalary(4000);
            user.setAccountNonLocked(true);
            user.setPassword(encoder.encode("123123"));
            user.setEnable(true);
            userRepository.save(user);
            System.out.println("Пользователь: "+ user.getLogin()+ " с паролью:"+ user.getPassword() +
                    " зашифрованный пароль :" + encoder.encode(user.getPassword()) + " " + " создан");
        }else {
            System.out.println("Пользователь уже есть");
        }
    }
}
