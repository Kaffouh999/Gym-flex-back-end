package com.binarybrothers.gymflexapi.dtos;

import static org.assertj.core.api.Assertions.assertThat;


import com.binarybrothers.gymflexapi.dtos.sessionMember.SessionMemberDTO;
import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class SessionMemberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionMemberDTO.class);
        SessionMemberDTO sessionMemberDTO1 = new SessionMemberDTO();
        sessionMemberDTO1.setId(1L);
        SessionMemberDTO sessionMemberDTO2 = new SessionMemberDTO();
        assertThat(sessionMemberDTO1).isNotEqualTo(sessionMemberDTO2);
        sessionMemberDTO2.setId(sessionMemberDTO1.getId());
        assertThat(sessionMemberDTO1).isEqualTo(sessionMemberDTO2);
        sessionMemberDTO2.setId(2L);
        assertThat(sessionMemberDTO1).isNotEqualTo(sessionMemberDTO2);
        sessionMemberDTO1.setId(null);
        assertThat(sessionMemberDTO1).isNotEqualTo(sessionMemberDTO2);
    }
}
