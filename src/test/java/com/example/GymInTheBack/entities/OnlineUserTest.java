package com.example.GymInTheBack.entities;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.GymInTheBack.web.TestUtil;
import org.junit.jupiter.api.Test;

class OnlineUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OnlineUser.class);
        OnlineUser onlineUser1 = new OnlineUser();
        onlineUser1.setId(1L);
        OnlineUser onlineUser2 = new OnlineUser();
        onlineUser2.setId(onlineUser1.getId());
        assertThat(onlineUser1).isEqualTo(onlineUser2);
        onlineUser2.setId(2L);
        assertThat(onlineUser1).isNotEqualTo(onlineUser2);
        onlineUser1.setId(null);
        assertThat(onlineUser1).isNotEqualTo(onlineUser2);
    }
}
