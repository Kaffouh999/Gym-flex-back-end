package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.dtos.statistics.EquipmentStatisticsDTO;
import com.example.GymInTheBack.entities.Category;
import com.example.GymInTheBack.entities.Equipment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@SuppressWarnings("unused")
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Equipment findByName(String name);

    @Query("SELECT NEW com.example.GymInTheBack.dtos.statistics.EquipmentStatisticsDTO(e.name, COUNT(ei)) FROM Equipment e LEFT JOIN e.equipmentItemList ei GROUP BY e")
    List<EquipmentStatisticsDTO> getEquipmentStatistics();
}
