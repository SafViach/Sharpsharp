package com.sharp.sharpshap.service;

import com.sharp.sharpshap.enums.EnumRole;
import com.sharp.sharpshap.exceptions.RoleNotFoundException;
import com.sharp.sharpshap.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<EnumRole> getRoleByName(String name) {
        Set<EnumRole> roles = new HashSet<>();
        roles.add(roleRepository.findByName(name).orElseThrow(() -> new RoleNotFoundException("роль = " + name + "не найдена")));
        return roles;
    }
}
