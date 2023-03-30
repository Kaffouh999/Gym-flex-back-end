package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.Equipment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {}
