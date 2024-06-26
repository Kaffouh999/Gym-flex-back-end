package com.binarybrothers.gymflexapi.repositories;

import com.binarybrothers.gymflexapi.entities.Order;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {}
