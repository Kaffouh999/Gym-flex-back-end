package com.example.GymInTheBack.entities;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.GymInTheBack.web.TestUtil;
import org.junit.jupiter.api.Test;

class AssuranceMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssuranceMember.class);
        AssuranceMember assuranceMember1 = new AssuranceMember();
        assuranceMember1.setId(1L);
        AssuranceMember assuranceMember2 = new AssuranceMember();
        assuranceMember2.setId(assuranceMember1.getId());
        assertThat(assuranceMember1).isEqualTo(assuranceMember2);
        assuranceMember2.setId(2L);
        assertThat(assuranceMember1).isNotEqualTo(assuranceMember2);
        assuranceMember1.setId(null);
        assertThat(assuranceMember1).isNotEqualTo(assuranceMember2);
    }
}
