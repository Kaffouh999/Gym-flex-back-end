package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.Category;
import com.example.GymInTheBack.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
