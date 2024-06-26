package com.binarybrothers.gymflexapi.entities;

import static org.assertj.core.api.Assertions.assertThat;

import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class SessionMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SessionMember.class);
        SessionMember sessionMember1 = new SessionMember();
        sessionMember1.setId(1L);
        SessionMember sessionMember2 = new SessionMember();
        sessionMember2.setId(sessionMember1.getId());
        assertThat(sessionMember1).isEqualTo(sessionMember2);
        sessionMember2.setId(2L);
        assertThat(sessionMember1).isNotEqualTo(sessionMember2);
        sessionMember1.setId(null);
        assertThat(sessionMember1).isNotEqualTo(sessionMember2);
    }
}
