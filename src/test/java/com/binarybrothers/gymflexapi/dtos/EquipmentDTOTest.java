package com.binarybrothers.gymflexapi.dtos;

import static org.assertj.core.api.Assertions.assertThat;

import com.binarybrothers.gymflexapi.dtos.equipment.EquipmentDTO;
import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class EquipmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentDTO.class);
        EquipmentDTO equipmentDTO1 = new EquipmentDTO();
        equipmentDTO1.setId(1L);
        EquipmentDTO equipmentDTO2 = new EquipmentDTO();
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
        equipmentDTO2.setId(equipmentDTO1.getId());
        assertThat(equipmentDTO1).isEqualTo(equipmentDTO2);
        equipmentDTO2.setId(2L);
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
        equipmentDTO1.setId(null);
        assertThat(equipmentDTO1).isNotEqualTo(equipmentDTO2);
    }
}
