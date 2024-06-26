package com.binarybrothers.gymflexapi.entities;

import static org.assertj.core.api.Assertions.assertThat;

import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class MaintiningTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Maintining.class);
        Maintining maintining1 = new Maintining();
        maintining1.setId(1L);
        Maintining maintining2 = new Maintining();
        maintining2.setId(maintining1.getId());
        assertThat(maintining1).isEqualTo(maintining2);
        maintining2.setId(2L);
        assertThat(maintining1).isNotEqualTo(maintining2);
        maintining1.setId(null);
        assertThat(maintining1).isNotEqualTo(maintining2);
    }
}
