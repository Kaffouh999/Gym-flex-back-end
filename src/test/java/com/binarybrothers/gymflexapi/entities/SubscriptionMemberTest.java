package com.binarybrothers.gymflexapi.entities;

import static org.assertj.core.api.Assertions.assertThat;

import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class SubscriptionMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionMember.class);
        SubscriptionMember subscriptionMember1 = new SubscriptionMember();
        subscriptionMember1.setId(1L);
        SubscriptionMember subscriptionMember2 = new SubscriptionMember();
        subscriptionMember2.setId(subscriptionMember1.getId());
        assertThat(subscriptionMember1).isEqualTo(subscriptionMember2);
        subscriptionMember2.setId(2L);
        assertThat(subscriptionMember1).isNotEqualTo(subscriptionMember2);
        subscriptionMember1.setId(null);
        assertThat(subscriptionMember1).isNotEqualTo(subscriptionMember2);
    }
}
