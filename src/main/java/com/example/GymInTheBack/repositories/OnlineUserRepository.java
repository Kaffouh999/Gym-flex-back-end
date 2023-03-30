package com.example.GymInTheBack.repositories;


import com.example.GymInTheBack.entities.OnlineUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OnlineUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OnlineUserRepository extends JpaRepository<OnlineUser, Long> {}
