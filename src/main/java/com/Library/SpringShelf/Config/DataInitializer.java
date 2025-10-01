package com.Library.SpringShelf.Config;

import com.Library.SpringShelf.Model.Role;
import com.Library.SpringShelf.Model.Rolename;
import com.Library.SpringShelf.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {

        for (Rolename roleName : Rolename.values()) {
            if (roleRepository.findByRole(roleName).isEmpty()) {
                Role newRole = new Role();
                newRole.setRole(roleName);
                roleRepository.save(newRole);
            }
        }
    }
}