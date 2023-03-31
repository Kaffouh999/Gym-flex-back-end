package com.example.GymInTheBack.entities;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.GymInTheBack.web.TestUtil;
import org.junit.jupiter.api.Test;

class ReformTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reform.class);
        Reform reform1 = new Reform();
        reform1.setId(1L);
        Reform reform2 = new Reform();
        reform2.setId(reform1.getId());
        assertThat(reform1).isEqualTo(reform2);
        reform2.setId(2L);
        assertThat(reform1).isNotEqualTo(reform2);
        reform1.setId(null);
        assertThat(reform1).isNotEqualTo(reform2);
    }
}
