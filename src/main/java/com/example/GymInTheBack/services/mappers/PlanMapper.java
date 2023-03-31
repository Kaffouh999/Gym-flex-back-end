package com.example.GymInTheBack.services.mappers;


import com.example.GymInTheBack.dtos.plan.PlanDTO;
import com.example.GymInTheBack.entities.Plan;
import com.example.GymInTheBack.utils.EntityMapper;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface PlanMapper extends EntityMapper<PlanDTO, Plan> {}
