package com.example.GymInTheBack.dtos;

import static org.assertj.core.api.Assertions.assertThat;


import com.example.GymInTheBack.dtos.assuranceMember.AssuranceMemberDTO;
import com.example.GymInTheBack.web.TestUtil;
import org.junit.jupiter.api.Test;

class AssuranceMemberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssuranceMemberDTO.class);
        AssuranceMemberDTO assuranceMemberDTO1 = new AssuranceMemberDTO();
        assuranceMemberDTO1.setId(1L);
        AssuranceMemberDTO assuranceMemberDTO2 = new AssuranceMemberDTO();
        assertThat(assuranceMemberDTO1).isNotEqualTo(assuranceMemberDTO2);
        assuranceMemberDTO2.setId(assuranceMemberDTO1.getId());
        assertThat(assuranceMemberDTO1).isEqualTo(assuranceMemberDTO2);
        assuranceMemberDTO2.setId(2L);
        assertThat(assuranceMemberDTO1).isNotEqualTo(assuranceMemberDTO2);
        assuranceMemberDTO1.setId(null);
        assertThat(assuranceMemberDTO1).isNotEqualTo(assuranceMemberDTO2);
    }
}
