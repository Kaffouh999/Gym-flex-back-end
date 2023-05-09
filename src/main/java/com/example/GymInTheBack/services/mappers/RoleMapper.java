package com.example.GymInTheBack.services.mappers;

import com.example.GymInTheBack.dtos.role.RoleDTO;
import com.example.GymInTheBack.entities.Role;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role>  {
}
