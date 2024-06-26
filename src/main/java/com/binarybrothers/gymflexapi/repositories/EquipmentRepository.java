package com.binarybrothers.gymflexapi.repositories;

import com.binarybrothers.gymflexapi.dtos.statistics.EquipmentStatisticsDTO;
import com.binarybrothers.gymflexapi.entities.Equipment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@SuppressWarnings("unused")
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Equipment findByName(String name);

    @Query("SELECT NEW com.binarybrothers.gymflexapi.dtos.statistics.EquipmentStatisticsDTO(e.name, COUNT(ei)) FROM Equipment e LEFT JOIN e.equipmentItemList ei GROUP BY e")
    List<EquipmentStatisticsDTO> getEquipmentStatistics();
}
