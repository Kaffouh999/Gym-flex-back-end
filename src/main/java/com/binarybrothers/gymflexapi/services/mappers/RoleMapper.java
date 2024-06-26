package com.binarybrothers.gymflexapi.services.mappers;

import com.binarybrothers.gymflexapi.dtos.role.RoleDTO;
import com.binarybrothers.gymflexapi.entities.Role;
import com.binarybrothers.gymflexapi.utils.EntityMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role>  {
}
