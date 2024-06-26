package com.binarybrothers.gymflexapi.dtos;

import static org.assertj.core.api.Assertions.assertThat;

import com.binarybrothers.gymflexapi.dtos.maintining.MaintiningDTO;
import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class MaintiningDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaintiningDTO.class);
        MaintiningDTO maintiningDTO1 = new MaintiningDTO();
        maintiningDTO1.setId(1L);
        MaintiningDTO maintiningDTO2 = new MaintiningDTO();
        assertThat(maintiningDTO1).isNotEqualTo(maintiningDTO2);
        maintiningDTO2.setId(maintiningDTO1.getId());
        assertThat(maintiningDTO1).isEqualTo(maintiningDTO2);
        maintiningDTO2.setId(2L);
        assertThat(maintiningDTO1).isNotEqualTo(maintiningDTO2);
        maintiningDTO1.setId(null);
        assertThat(maintiningDTO1).isNotEqualTo(maintiningDTO2);
    }
}
