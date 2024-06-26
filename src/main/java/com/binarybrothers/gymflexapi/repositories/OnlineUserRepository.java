package com.binarybrothers.gymflexapi.repositories;


import com.binarybrothers.gymflexapi.entities.OnlineUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the OnlineUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OnlineUserRepository extends JpaRepository<OnlineUser, Long> {

    Optional<OnlineUser> findByEmailIsIgnoreCase(String email);
}
