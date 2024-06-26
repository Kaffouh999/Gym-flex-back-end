package com.binarybrothers.gymflexapi.entities;

import static org.assertj.core.api.Assertions.assertThat;

import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class EquipmentItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentItem.class);
        EquipmentItem equipmentItem1 = new EquipmentItem();
        equipmentItem1.setId(1L);
        EquipmentItem equipmentItem2 = new EquipmentItem();
        equipmentItem2.setId(equipmentItem1.getId());
        assertThat(equipmentItem1).isEqualTo(equipmentItem2);
        equipmentItem2.setId(2L);
        assertThat(equipmentItem1).isNotEqualTo(equipmentItem2);
        equipmentItem1.setId(null);
        assertThat(equipmentItem1).isNotEqualTo(equipmentItem2);
    }
}
