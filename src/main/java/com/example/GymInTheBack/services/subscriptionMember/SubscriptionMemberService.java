package com.example.GymInTheBack.services.subscriptionMember;


import com.example.GymInTheBack.dtos.subscription.SubscriptionMemberDTO;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.google.zxing.WriterException;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;


public interface SubscriptionMemberService {
    /**
     * Save a subscriptionMember.
     *
     * @param subscriptionMemberDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriptionMemberDTO save(SubscriptionMemberDTO subscriptionMemberDTO) throws WriterException, NoSuchAlgorithmException;

    /**
     * Updates a subscriptionMember.
     *
     * @param subscriptionMemberDTO the entity to update.
     * @return the persisted entity.
     */
    SubscriptionMemberDTO update(SubscriptionMemberDTO subscriptionMemberDTO);

    /**
     * Partially updates a subscriptionMember.
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
     * Get the "id" subscriptionMember.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriptionMemberDTO> findOne(Long id);

    Optional<SubscriptionMemberDTO> findByCodeSubscription(String codeSubscription);

    /**
     * Delete the "id" subscriptionMember.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
