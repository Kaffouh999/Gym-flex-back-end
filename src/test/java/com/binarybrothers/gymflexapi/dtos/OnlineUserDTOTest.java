package com.binarybrothers.gymflexapi.dtos;

import static org.assertj.core.api.Assertions.assertThat;


import com.binarybrothers.gymflexapi.dtos.user.OnlineUserDTO;
import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class OnlineUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OnlineUserDTO.class);
        OnlineUserDTO onlineUserDTO1 = new OnlineUserDTO();
        onlineUserDTO1.setId(1L);
        OnlineUserDTO onlineUserDTO2 = new OnlineUserDTO();
        assertThat(onlineUserDTO1).isNotEqualTo(onlineUserDTO2);
        onlineUserDTO2.setId(onlineUserDTO1.getId());
        assertThat(onlineUserDTO1).isEqualTo(onlineUserDTO2);
        onlineUserDTO2.setId(2L);
        assertThat(onlineUserDTO1).isNotEqualTo(onlineUserDTO2);
        onlineUserDTO1.setId(null);
        assertThat(onlineUserDTO1).isNotEqualTo(onlineUserDTO2);
    }
}
