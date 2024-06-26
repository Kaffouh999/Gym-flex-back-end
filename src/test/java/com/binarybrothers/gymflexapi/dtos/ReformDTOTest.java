package com.binarybrothers.gymflexapi.dtos;

import static org.assertj.core.api.Assertions.assertThat;


import com.binarybrothers.gymflexapi.dtos.reform.ReformDTO;
import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class ReformDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReformDTO.class);
        ReformDTO reformDTO1 = new ReformDTO();
        reformDTO1.setId(1L);
        ReformDTO reformDTO2 = new ReformDTO();
        assertThat(reformDTO1).isNotEqualTo(reformDTO2);
        reformDTO2.setId(reformDTO1.getId());
        assertThat(reformDTO1).isEqualTo(reformDTO2);
        reformDTO2.setId(2L);
        assertThat(reformDTO1).isNotEqualTo(reformDTO2);
        reformDTO1.setId(null);
        assertThat(reformDTO1).isNotEqualTo(reformDTO2);
    }
}
