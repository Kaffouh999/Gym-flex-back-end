package com.example.GymInTheBack.services.user;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.GymInTheBack.dtos.gymbranch.GymBranchDTO;
import com.example.GymInTheBack.dtos.mailing.ContactMailDTO;
import com.example.GymInTheBack.dtos.user.OnlineUserDTO;
import com.example.GymInTheBack.entities.OnlineUser;
import com.example.GymInTheBack.entities.Role;
import com.example.GymInTheBack.repositories.OnlineUserRepository;
import com.example.GymInTheBack.repositories.RoleRepository;
import com.example.GymInTheBack.services.gymbranch.GymBranchService;
import com.example.GymInTheBack.services.mailing.EmailService;
import com.example.GymInTheBack.services.mappers.OnlineUserMapper;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class OnlineUserServiceImpl implements OnlineUserService {

    private final Logger log = LoggerFactory.getLogger(OnlineUserServiceImpl.class);

    private final OnlineUserRepository onlineUserRepository;
    private final GymBranchService gymBranchService;
    private final OnlineUserMapper onlineUserMapper;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    public OnlineUserServiceImpl(OnlineUserRepository onlineUserRepository, OnlineUserMapper onlineUserMapper, RoleRepository roleRepository, GymBranchService gymBranchService, EmailService emailService) {
        this.onlineUserRepository = onlineUserRepository;
        this.onlineUserMapper = onlineUserMapper;
        this.roleRepository = roleRepository;
        this.gymBranchService = gymBranchService;
        this.emailService = emailService;
    }

    @Override
    public OnlineUserDTO save(OnlineUserDTO onlineUserDTO) {
        log.debug("Request to save OnlineUser : {}", onlineUserDTO);
        OnlineUser onlineUser = onlineUserMapper.toEntity(onlineUserDTO);
        Role userRole = roleRepository.findById(onlineUser.getRole().getId()).get();
        onlineUser.setRole(userRole);
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

    @Override
    public Boolean contactUs(ContactMailDTO contactMailDTO)  {


        String subject = contactMailDTO.getSubject();
        String name = contactMailDTO.getFullName();
        String messageSent = contactMailDTO.getMessage();
        String message = "<html><body><h2>mail from "+name+"</h2><br/><p>"+messageSent+"</p></body></html>";

            if(contactMailDTO.getIdUser() != null) {
                OnlineUser user = onlineUserRepository.findById(contactMailDTO.getIdUser()).get();
                if(user != null) {
                    String mailFrom = user.getMember().getGymBranch().getEmail();
                    String mailTo = mailFrom;
                    String appPassword = user.getMember().getGymBranch().getAppPasswordEmail();
                    try {
                        emailService.sendEmail(mailFrom, appPassword, mailTo, subject, message);
                    } catch (MessagingException e) {

                        return false;
                    }
                }else{
                    return  false;
                }
            }else{
                List<GymBranchDTO> gymBranchList = gymBranchService.findAll();
                for(GymBranchDTO gym : gymBranchList){
                    String mailFrom = gym.getEmail();
                    String mailTo = mailFrom;
                    String appPassword = gym.getAppPasswordEmail();
                    try {
                        emailService.sendEmail(mailFrom, appPassword, mailTo, subject, message);
                    } catch (MessagingException e) {
                        return false;
                    }
                }
        }
            return true;

    }

    public boolean validateMail(String validationKey){
        String[] arr= validationKey.split("_");
        Long idUser = Long.parseLong(arr[0]);
        String validationToken = arr[1];
        OnlineUser user = onlineUserRepository.findById(idUser).get();
        if(user != null && validationKey == user.getValidationKey()){
            user.setValidationKey(null);
            onlineUserRepository.save(user);
            return true;
        }

        return  false;
    }
}
