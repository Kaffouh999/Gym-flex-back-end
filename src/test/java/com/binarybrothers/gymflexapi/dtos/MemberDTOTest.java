package com.binarybrothers.gymflexapi.dtos;

import static org.assertj.core.api.Assertions.assertThat;


import com.binarybrothers.gymflexapi.dtos.member.MemberDTO;
import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class MemberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberDTO.class);
        MemberDTO memberDTO1 = new MemberDTO();
        memberDTO1.setId(1L);
        MemberDTO memberDTO2 = new MemberDTO();
        assertThat(memberDTO1).isNotEqualTo(memberDTO2);
        memberDTO2.setId(memberDTO1.getId());
        assertThat(memberDTO1).isEqualTo(memberDTO2);
        memberDTO2.setId(2L);
        assertThat(memberDTO1).isNotEqualTo(memberDTO2);
        memberDTO1.setId(null);
        assertThat(memberDTO1).isNotEqualTo(memberDTO2);
    }
}
