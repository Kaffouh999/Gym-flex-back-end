package com.binarybrothers.gymflexapi.services.subscriptionmember;


import com.binarybrothers.gymflexapi.dtos.subscription.SubscriptionMemberDTO;
import com.binarybrothers.gymflexapi.dtos.subscription.SubscriptionWithPaymentsDTO;
import com.binarybrothers.gymflexapi.entities.SubscriptionMember;
import com.google.zxing.WriterException;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;


public interface SubscriptionMemberService {
    /**
     * Save a subscriptionmember.
     *
     * @param subscriptionMemberDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionMemberDTO save(SubscriptionMemberDTO subscriptionMemberDTO) throws WriterException, NoSuchAlgorithmException;

    /**
     * Updates a subscriptionmember.
     *
     * @param subscriptionMemberDTO the entity to update.
     * @return the persisted entity.
     */
    SubscriptionMemberDTO update(SubscriptionMemberDTO subscriptionMemberDTO);

    /**
     * Partially updates a subscriptionmember.
     *
     * @param subscriptionMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubscriptionMemberDTO> partialUpdate(SubscriptionMemberDTO subscriptionMemberDTO);

    /**
     * Get all the subscriptionMembers.
     *
     * @return the list of entities.
     */
    List<SubscriptionMemberDTO> findAll();

    /**
     * Get the "id" subscriptionmember.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionMemberDTO> findOne(Long id);

    Optional<SubscriptionMemberDTO> findByCodeSubscription(String codeSubscription);

    /**
     * Delete the "id" subscriptionmember.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

   List<SubscriptionMember> entering(String qrCode);

    List<SubscriptionWithPaymentsDTO>  findByMemberId(Long userId);

    List<SubscriptionMemberDTO> searchSubs(SubscriptionMemberDTO subscriptionMemberDTO);

    byte[] generateMemberCardReport(Long id) throws Exception;

}
