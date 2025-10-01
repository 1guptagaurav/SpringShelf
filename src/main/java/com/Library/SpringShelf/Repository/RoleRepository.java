package com.Library.SpringShelf.Repository;

import com.Library.SpringShelf.Model.Role;
import com.Library.SpringShelf.Model.Rolename;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(Rolename role);
}
