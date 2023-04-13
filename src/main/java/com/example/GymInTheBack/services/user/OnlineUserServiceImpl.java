package com.example.GymInTheBack.services.user;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.GymInTheBack.dtos.user.OnlineUserDTO;
import com.example.GymInTheBack.entities.Equipment;
import com.example.GymInTheBack.entities.OnlineUser;
import com.example.GymInTheBack.repositories.OnlineUserRepository;
import com.example.GymInTheBack.services.mappers.OnlineUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class OnlineUserServiceImpl implements OnlineUserService {

    private final Logger log = LoggerFactory.getLogger(OnlineUserServiceImpl.class);

    private final OnlineUserRepository onlineUserRepository;

    private final OnlineUserMapper onlineUserMapper;

    public OnlineUserServiceImpl(OnlineUserRepository onlineUserRepository, OnlineUserMapper onlineUserMapper) {
        this.onlineUserRepository = onlineUserRepository;
        this.onlineUserMapper = onlineUserMapper;
    }

    @Override
    public OnlineUserDTO save(OnlineUserDTO onlineUserDTO) {
        log.debug("Request to save OnlineUser : {}", onlineUserDTO);
        OnlineUser onlineUser = onlineUserMapper.toEntity(onlineUserDTO);
        onlineUser = onlineUserRepository.save(onlineUser);
        return onlineUserMapper.toDto(onlineUser);
    }

    @Override
    public OnlineUserDTO update(OnlineUserDTO onlineUserDTO) {
        log.debug("Request to update OnlineUser : {}", onlineUserDTO);
        OnlineUser onlineUser = onlineUserMapper.toEntity(onlineUserDTO);
        onlineUser = onlineUserRepository.save(onlineUser);
        return onlineUserMapper.toDto(onlineUser);
    }

    @Override
    public Optional<OnlineUserDTO> partialUpdate(OnlineUserDTO onlineUserDTO) {
        log.debug("Request to partially update OnlineUser : {}", onlineUserDTO);

        return onlineUserRepository
            .findById(onlineUserDTO.getId())
            .map(existingOnlineUser -> {
                onlineUserMapper.partialUpdate(existingOnlineUser, onlineUserDTO);

                return existingOnlineUser;
            })
            .map(onlineUserRepository::save)
            .map(onlineUserMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OnlineUserDTO> findAll() {
        log.debug("Request to get all OnlineUsers");
        return onlineUserRepository.findAll().stream().map(onlineUserMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OnlineUserDTO> findOne(Long id) {
        log.debug("Request to get OnlineUser : {}", id);
        return onlineUserRepository.findById(id).map(onlineUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OnlineUser : {}", id);
        onlineUserRepository.deleteById(id);
    }

    @Override
    public Optional<OnlineUser> findById(Long id) {
        return onlineUserRepository.findById(id);
    }
}
