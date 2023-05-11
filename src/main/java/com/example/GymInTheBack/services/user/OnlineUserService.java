package com.example.GymInTheBack.services.user;

import com.example.GymInTheBack.dtos.mailing.ContactMailDTO;
import com.example.GymInTheBack.dtos.user.OnlineUserDTO;
import com.example.GymInTheBack.entities.Equipment;
import com.example.GymInTheBack.entities.OnlineUser;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Optional;


public interface OnlineUserService {
    /**
     * Save a onlineUser.
     *
     * @param onlineUserDTO the entity to save.
     * @return the persisted entity.
     */
    OnlineUserDTO save(OnlineUserDTO onlineUserDTO);

    /**
     * Updates a onlineUser.
     *
     * @param onlineUserDTO the entity to update.
     * @return the persisted entity.
     */
    OnlineUserDTO update(OnlineUserDTO onlineUserDTO);

    /**
     * Partially updates a onlineUser.
     *
     * @param onlineUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OnlineUserDTO> partialUpdate(OnlineUserDTO onlineUserDTO);

    /**
     * Get all the onlineUsers.
     *
     * @return the list of entities.
     */
    List<OnlineUserDTO> findAll();

    /**
     * Get the "id" onlineUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OnlineUserDTO> findOne(Long id);

    /**
     * Delete the "id" onlineUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    public Optional<OnlineUser> findById(Long id);

    public Boolean contactUs(ContactMailDTO contactMailDTO) throws MessagingException;
    public boolean validateMail(String validationKey);
}
