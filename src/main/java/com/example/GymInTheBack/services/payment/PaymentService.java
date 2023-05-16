package com.example.GymInTheBack.services.payment;


import com.example.GymInTheBack.dtos.payment.PaymentDTO;
import com.example.GymInTheBack.entities.Payment;

import java.util.List;
import java.util.Optional;


public interface PaymentService {
    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentDTO save(PaymentDTO paymentDTO);

    /**
     * Updates a payment.
     *
     * @param paymentDTO the entity to update.
     * @return the persisted entity.
     */
    PaymentDTO update(PaymentDTO paymentDTO);

    /**
     * Partially updates a payment.
     *
     * @param paymentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentDTO> partialUpdate(PaymentDTO paymentDTO);

    /**
     * Get all the payments.
     *
     * @return the list of entities.
     */
    List<PaymentDTO> findAll();

    /**
     * Get the "id" payment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentDTO> findOne(Long id);

    /**
     * Delete the "id" payment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    List<PaymentDTO> findByMemberId(Long memberId);
}
