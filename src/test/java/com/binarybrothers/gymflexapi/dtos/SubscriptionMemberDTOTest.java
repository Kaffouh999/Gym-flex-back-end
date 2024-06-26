package com.binarybrothers.gymflexapi.dtos;

import static org.assertj.core.api.Assertions.assertThat;

import com.binarybrothers.gymflexapi.dtos.subscription.SubscriptionMemberDTO;

import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionMemberDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionMemberDTO.class);
        SubscriptionMemberDTO subscriptionMemberDTO1 = new SubscriptionMemberDTO();
        subscriptionMemberDTO1.setId(1L);
        SubscriptionMemberDTO subscriptionMemberDTO2 = new SubscriptionMemberDTO();
        assertThat(subscriptionMemberDTO1).isNotEqualTo(subscriptionMemberDTO2);
        subscriptionMemberDTO2.setId(subscriptionMemberDTO1.getId());
        assertThat(subscriptionMemberDTO1).isEqualTo(subscriptionMemberDTO2);
        subscriptionMemberDTO2.setId(2L);
        assertThat(subscriptionMemberDTO1).isNotEqualTo(subscriptionMemberDTO2);
        subscriptionMemberDTO1.setId(null);
        assertThat(subscriptionMemberDTO1).isNotEqualTo(subscriptionMemberDTO2);
    }
}
