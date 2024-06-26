package com.binarybrothers.gymflexapi.entities;

import static org.assertj.core.api.Assertions.assertThat;

import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class GymBranchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GymBranch.class);
        GymBranch gymBranch1 = new GymBranch();
        gymBranch1.setId(1L);
        GymBranch gymBranch2 = new GymBranch();
        gymBranch2.setId(gymBranch1.getId());
        assertThat(gymBranch1).isEqualTo(gymBranch2);
        gymBranch2.setId(2L);
        assertThat(gymBranch1).isNotEqualTo(gymBranch2);
        gymBranch1.setId(null);
        assertThat(gymBranch1).isNotEqualTo(gymBranch2);
    }
}
