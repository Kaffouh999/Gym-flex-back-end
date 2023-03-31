package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.EquipmentItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface EquipmentItemRepository extends JpaRepository<EquipmentItem, Long> {}
