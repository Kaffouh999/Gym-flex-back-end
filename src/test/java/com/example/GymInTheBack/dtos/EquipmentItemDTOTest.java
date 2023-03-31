package com.example.GymInTheBack.dtos;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.GymInTheBack.dtos.equipmentItem.EquipmentItemDTO;
import com.example.GymInTheBack.web.TestUtil;
import org.junit.jupiter.api.Test;

class EquipmentItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EquipmentItemDTO.class);
        EquipmentItemDTO equipmentItemDTO1 = new EquipmentItemDTO();
        equipmentItemDTO1.setId(1L);
        EquipmentItemDTO equipmentItemDTO2 = new EquipmentItemDTO();
        assertThat(equipmentItemDTO1).isNotEqualTo(equipmentItemDTO2);
        equipmentItemDTO2.setId(equipmentItemDTO1.getId());
        assertThat(equipmentItemDTO1).isEqualTo(equipmentItemDTO2);
        equipmentItemDTO2.setId(2L);
        assertThat(equipmentItemDTO1).isNotEqualTo(equipmentItemDTO2);
        equipmentItemDTO1.setId(null);
        assertThat(equipmentItemDTO1).isNotEqualTo(equipmentItemDTO2);
    }
}
