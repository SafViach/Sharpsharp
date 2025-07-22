package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.UserDTO;
import com.sharp.sharpshap.enums.EnumRole;
import com.sharp.sharpshap.exceptions.UserException;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleService = roleService;
    }

    public User registerUser(UserDTO dtoUser) {
        if (userRepository.findByLogin(dtoUser.getLogin()).isPresent()){
            throw new UserException("Пользователь с логином "+dtoUser.getLogin() + " уже существует");
        }
        if (userRepository.findByLastName(dtoUser.getLastName()).isPresent() &&
        userRepository.findByFirstName(dtoUser.getFirstName()).isPresent()){
            throw new UserException("Пользователь с именем: "+ dtoUser.getLastName() + " и фамилией: " +
                    dtoUser.getFirstName() + " существует");
        }
        User user = new User();
        Set<EnumRole> roles = roleService.getRoleByName("ROLE_USER");
        user.setLogin(dtoUser.getLogin());
        user.setLastName(dtoUser.getLastName());
        user.setFirstName(dtoUser.getFirstName());
        user.setPassword(encoder.encode(dtoUser.getPassword()));
        user.setRoles(roles);
        user.setEnable(true);
        user.setAccountNonLocked(true);
        user.setSalary(0);
        return userRepository.save(user);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserException("Такого User с id= " + id + " нет"));
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserException("Список User пуст");
        }
        return users;
    }

    public User updateUser(UUID id, User updateUser) {
        User oldUser = userRepository.findById(id).orElseThrow();//
        oldUser.setLogin(updateUser.getLogin());
        oldUser.setSalary(updateUser.getSalary());
        oldUser.setPassword(encoder.encode(updateUser.getPassword()));
        oldUser.setRoles(updateUser.getRoles());
        return userRepository.save(oldUser);
    }

    public void deleteUser(UUID id) {
        this.getUserById(id);
        userRepository.deleteById(id);
    }
    public void updatePassword(String newPassword){

    }
    public String getCurrentUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails){
            return authentication.getName();//login user
        }
        throw new UserException("Пользователь не аутентифицирован");
    }

    public User getUser(){
        String login = getCurrentUserName();
        return userRepository.findByLogin(login).orElseThrow(()->new UsernameNotFoundException("Пользователь не найден"));
    }

    public List<User> findUsersByFirstNameOrLastName(String prefix){
        return userRepository.findByLastNameOrFirstName(prefix);
    }
//    public List<Map<String , String>> userIstory(User user){
//        List<Map<String , String>> historyUser = new ArrayList<>();
//        if (user.getLogin() != null){
//            historyUser.add(user.getLogin());
//        }
//    }
}
