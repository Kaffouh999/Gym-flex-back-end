package com.example.GymInTheBack.services.role;

import com.example.GymInTheBack.dtos.role.RoleDTO;
import com.example.GymInTheBack.entities.Category;
import com.example.GymInTheBack.entities.Role;
import com.example.GymInTheBack.repositories.CategoryRepository;
import com.example.GymInTheBack.repositories.RoleRepository;
import com.example.GymInTheBack.services.category.CategoryServiceImpl;
import com.example.GymInTheBack.services.mappers.CategoryMapper;
import com.example.GymInTheBack.services.mappers.RoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        log.debug("Request to save Role : {}", roleDTO);
        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    @Override
    public RoleDTO update(RoleDTO roleDTO) {
        log.debug("Request to save Role : {}", roleDTO);
        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    @Override
    public Optional<RoleDTO> partialUpdate(RoleDTO roleDTO) {
        log.debug("Request to partially update Role : {}", roleDTO);

        return roleRepository
                .findById(roleDTO.getId())
                .map(existingRole -> {
                    roleMapper.partialUpdate(existingRole, roleDTO);

                    return existingRole;
                })
                .map(roleRepository::save)
                .map(roleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll() {
        log.debug("Request to get all roles");
        List<Role> roles=roleRepository.findAll();
        return roleRepository.findAll().stream().map(roleMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDTO> findOne(Long id) {
        log.debug("Request to get Role : {}", id);
        return roleRepository.findById(id).map(roleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Role : {}", id);
        roleRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.findByName(name) != null ? true:false;

    }
}
