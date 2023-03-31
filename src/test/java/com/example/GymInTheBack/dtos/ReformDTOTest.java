package com.example.GymInTheBack.dtos;

import static org.assertj.core.api.Assertions.assertThat;


import com.example.GymInTheBack.dtos.reform.ReformDTO;
import com.example.GymInTheBack.web.TestUtil;
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
