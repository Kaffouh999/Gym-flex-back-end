package com.example.GymInTheBack.services.sessionMember;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.GymInTheBack.dtos.gymbranch.GymBranchDTO;
import com.example.GymInTheBack.dtos.sessionMember.SessionMemberDTO;
import com.example.GymInTheBack.dtos.statistics.EnteringTimeStatisticDTO;
import com.example.GymInTheBack.entities.GymBranch;
import com.example.GymInTheBack.entities.Notification;
import com.example.GymInTheBack.entities.SessionMember;
import com.example.GymInTheBack.entities.SubscriptionMember;
import com.example.GymInTheBack.repositories.SessionMemberRepository;
import com.example.GymInTheBack.services.gymbranch.GymBranchService;
import com.example.GymInTheBack.services.mappers.SessionMemberMapper;
import com.example.GymInTheBack.services.notification.NotificationService;
import com.example.GymInTheBack.services.subscriptionMember.SubscriptionMemberService;
import com.example.GymInTheBack.utils.ReminderScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SessionMemberServiceImpl implements SessionMemberService {

    private final Logger log = LoggerFactory.getLogger(SessionMemberServiceImpl.class);

    private final SessionMemberRepository sessionMemberRepository;
    private final SubscriptionMemberService subscriptionMemberService;

    private final GymBranchService gymBranchService;
    private final NotificationService notificationService;
    private final SessionMemberMapper sessionMemberMapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public SessionMemberServiceImpl(SessionMemberRepository sessionMemberRepository, SessionMemberMapper sessionMemberMapper , SubscriptionMemberService subscriptionMemberService, GymBranchService gymBranchService, NotificationService notificationService) {
        this.sessionMemberRepository = sessionMemberRepository;
        this.sessionMemberMapper = sessionMemberMapper;
        this.subscriptionMemberService = subscriptionMemberService;
        this.gymBranchService = gymBranchService;
        this.notificationService = notificationService;
    }

    @Override
    public SessionMemberDTO save(SessionMemberDTO sessionMemberDTO) {
        log.debug("Request to save SessionMember : {}", sessionMemberDTO);
        SessionMember sessionMember = sessionMemberMapper.toEntity(sessionMemberDTO);
        sessionMember = sessionMemberRepository.save(sessionMember);
        return sessionMemberMapper.toDto(sessionMember);
    }

    @Override
    public SessionMemberDTO update(SessionMemberDTO sessionMemberDTO) {
        log.debug("Request to update SessionMember : {}", sessionMemberDTO);
        SessionMember sessionMember = sessionMemberMapper.toEntity(sessionMemberDTO);
        sessionMember = sessionMemberRepository.save(sessionMember);
        return sessionMemberMapper.toDto(sessionMember);
    }

    @Override
    public Optional<SessionMemberDTO> partialUpdate(SessionMemberDTO sessionMemberDTO) {
        log.debug("Request to partially update SessionMember : {}", sessionMemberDTO);

        return sessionMemberRepository
            .findById(sessionMemberDTO.getId())
            .map(existingSessionMember -> {
                sessionMemberMapper.partialUpdate(existingSessionMember, sessionMemberDTO);

                return existingSessionMember;
            })
            .map(sessionMemberRepository::save)
            .map(sessionMemberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionMemberDTO> findAll() {
        log.debug("Request to get all SessionMembers");
        return sessionMemberRepository.findAll().stream().map(sessionMemberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionMemberDTO> findOne(Long id) {
        log.debug("Request to get SessionMember : {}", id);
        return sessionMemberRepository.findById(id).map(sessionMemberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SessionMember : {}", id);
        sessionMemberRepository.deleteById(id);
    }

    @Override
    public Integer entering(String qrCode) {
        List<SubscriptionMember> subscriptionMembersEntering = subscriptionMemberService.entering(qrCode);
        List<SessionMember> sessionMemberIn = sessionMemberRepository.alreadyIn(qrCode);
        SessionMember sessionMember ;
        if(subscriptionMembersEntering.size() > 0 ) {
            if (sessionMemberIn.size() == 0) {
                SubscriptionMember subscriptionMemberentering = subscriptionMembersEntering.get(0);
                sessionMember = new SessionMember();
                sessionMember.setSubscriptionMember(subscriptionMemberentering);
                ZonedDateTime currentDate = ZonedDateTime.now();
                sessionMember.setEnteringTime(currentDate);
                sessionMember.setGymBranch(subscriptionMemberentering.getMember().getGymBranch());
                sessionMember.setManagerAtTheTime(subscriptionMemberentering.getMember());
                sessionMemberRepository.save(sessionMember);

                Long sessionDurationAllowed = sessionMember.getGymBranch().getSessionDurationAllowed().longValue();
                LocalDateTime reminderTime = currentDate.toLocalDateTime().plusMinutes(sessionDurationAllowed);
                String fullName = sessionMember.getSubscriptionMember().getMember().getOnlineUser().getLastName()+" "+ sessionMember.getSubscriptionMember().getMember().getOnlineUser().getLastName();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String message = "person with name "+fullName+" should leave , he entered at "+currentDate.toLocalDateTime().format(formatter);
                Notification notification = new Notification();
                notification.setMessage(message);
                notification.setReaded(false);
                notification.setAttachUrl(sessionMember.getSubscriptionMember().getMember().getOnlineUser().getProfilePicture());


                Long numberNotifNotRead = notificationService.numNotifNotRead();
                Runnable task = () -> {

                    notificationService.save(notification);
                    messagingTemplate.convertAndSend("/topic/shouldLeave", numberNotifNotRead);
                };

                ReminderScheduler.scheduleReminder(reminderTime, task);

                return 1;
            } else  {
                sessionMember = sessionMemberIn.get(0);
                ZonedDateTime currentDate = ZonedDateTime.now();
                sessionMember.setLeavingTime(currentDate);
                sessionMemberRepository.save(sessionMember);

                return 2;
            }

        }
        return  0;
    }

    @Override
    public List<SessionMember> alreadyIn(String qrCode) {
        return sessionMemberRepository.alreadyIn(qrCode);
    }

    @Override
    public List<SessionMemberDTO> findSessionsByMember(Long idMember) {
        List<SessionMember> sessionMemberList = sessionMemberRepository.findSessionByMemberId(idMember);
        return sessionMemberRepository.findSessionByMemberId(idMember).stream().map(sessionMemberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }



    @Override
    public List<EnteringTimeStatisticDTO> getSessionStatistic(Long idGymBranch) {
        List<EnteringTimeStatisticDTO> statisticEnteringList =  new ArrayList<EnteringTimeStatisticDTO>();
        GymBranchDTO gymBranch = gymBranchService.findOne(idGymBranch).get();
        ZonedDateTime startDateTime = gymBranch.getOpeningDate();
        ZonedDateTime endDateTime = gymBranch.getClosingDate();

        List<SessionMember> statistics = sessionMemberRepository.findAll();


        return statisticEnteringList;
    }

}
