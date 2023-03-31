package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.Maintining;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface MaintiningRepository extends JpaRepository<Maintining, Long> {}
