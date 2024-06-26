package com.binarybrothers.gymflexapi.dtos;

import static org.assertj.core.api.Assertions.assertThat;

import com.binarybrothers.gymflexapi.dtos.gymbranch.GymBranchDTO;
import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class GymBranchDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GymBranchDTO.class);
        GymBranchDTO gymBranchDTO1 = new GymBranchDTO();
        gymBranchDTO1.setId(1L);
        GymBranchDTO gymBranchDTO2 = new GymBranchDTO();
        assertThat(gymBranchDTO1).isNotEqualTo(gymBranchDTO2);
        gymBranchDTO2.setId(gymBranchDTO1.getId());
        assertThat(gymBranchDTO1).isEqualTo(gymBranchDTO2);
        gymBranchDTO2.setId(2L);
        assertThat(gymBranchDTO1).isNotEqualTo(gymBranchDTO2);
        gymBranchDTO1.setId(null);
        assertThat(gymBranchDTO1).isNotEqualTo(gymBranchDTO2);
    }
}
