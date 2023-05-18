package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.Category;
import com.example.GymInTheBack.entities.Equipment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Equipment findByName(String name);
}
