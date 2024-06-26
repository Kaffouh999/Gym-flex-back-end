package com.binarybrothers.gymflexapi.services.role;

import com.binarybrothers.gymflexapi.dtos.role.RoleDTO;

import java.util.List;
import java.util.Optional;

public interface RoleService {


    RoleDTO save(RoleDTO roleDTO);


    RoleDTO update(RoleDTO roleDTO);


    Optional<RoleDTO> partialUpdate(RoleDTO roleDTO);


    List<RoleDTO> findAll();


    Optional<RoleDTO> findOne(Long id);


    void delete(Long id);
    boolean existsByName(String name);
}
