package com.binarybrothers.gymflexapi.repositories;

import com.binarybrothers.gymflexapi.entities.EquipmentItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface EquipmentItemRepository extends JpaRepository<EquipmentItem, Long> {
    EquipmentItem findByBareCode(String bareCode);
}
