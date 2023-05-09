package com.example.GymInTheBack.web;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.user.OnlineUserDTO;
import com.example.GymInTheBack.entities.Equipment;
import com.example.GymInTheBack.entities.OnlineUser;
import com.example.GymInTheBack.repositories.OnlineUserRepository;
import com.example.GymInTheBack.services.mappers.OnlineUserMapper;
import com.example.GymInTheBack.services.upload.IUploadService;
import com.example.GymInTheBack.services.user.OnlineUserService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OnlineUserResource {

    private final Logger log = LoggerFactory.getLogger(OnlineUserResource.class);

    private static final String ENTITY_NAME = "onlineUser";


    private String applicationName = "GymFlex";

    private final OnlineUserService onlineUserService;

    private final OnlineUserMapper onlineUserMapper;
    private final OnlineUserRepository onlineUserRepository;

    private final IUploadService uploadService;

    public OnlineUserResource(OnlineUserService onlineUserService, OnlineUserRepository onlineUserRepository , IUploadService uploadService , OnlineUserMapper onlineUserMapper) {
        this.onlineUserService = onlineUserService;
        this.onlineUserRepository = onlineUserRepository;
        this.uploadService = uploadService;
        this.onlineUserMapper=onlineUserMapper;
    }

    /**
     * {@code POST  /online-users} : Create a new onlineUser.
     *
     * @param onlineUserDTO the onlineUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new onlineUserDTO, or with status {@code 400 (Bad Request)} if the onlineUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/online-users")
    public ResponseEntity<OnlineUserDTO> createOnlineUser(@Valid @RequestBody OnlineUserDTO onlineUserDTO) throws URISyntaxException {
        log.debug("REST request to save OnlineUser : {}", onlineUserDTO);
        if (onlineUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new onlineUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (onlineUserDTO.getFirstName() == null || onlineUserDTO.getFirstName().trim().equals("")) {
            throw new BadRequestAlertException("A new onlineUser must hve a fisrt name", ENTITY_NAME, "firstnamerequired");
        }
        if (onlineUserDTO.getLastName() == null || onlineUserDTO.getLastName().trim().equals("")) {
            throw new BadRequestAlertException("A new onlineUser must hve a last name", ENTITY_NAME, "lastnamerequired");
        }
        if (onlineUserDTO.getEmail() == null || onlineUserDTO.getEmail().trim().equals("")) {
            throw new BadRequestAlertException("A new onlineUser must hve a email", ENTITY_NAME, "emailrequired");
        }
        if (onlineUserDTO.getPassword() == null || onlineUserDTO.getPassword().trim().equals("")) {
            throw new BadRequestAlertException("A new onlineUser must hve a password", ENTITY_NAME, "passwordrequired");
        }
        if (onlineUserDTO.getLogin() == null || onlineUserDTO.getLogin().trim().equals("")) {
            throw new BadRequestAlertException("A new onlineUser must hve a login", ENTITY_NAME, "loginrequired");
        }
        OnlineUserDTO result = onlineUserService.save(onlineUserDTO);
        return ResponseEntity
            .created(new URI("/api/online-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /online-users/:id} : Updates an existing onlineUser.
     *
     * @param id the id of the onlineUserDTO to save.
     * @param onlineUserDTO the onlineUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated onlineUserDTO,
     * or with status {@code 400 (Bad Request)} if the onlineUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the onlineUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/online-users/{id}")
    public ResponseEntity<OnlineUserDTO> updateOnlineUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OnlineUserDTO onlineUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OnlineUser : {}, {}", id, onlineUserDTO);
        if (onlineUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, onlineUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!onlineUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OnlineUserDTO result = onlineUserService.update(onlineUserDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, onlineUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /online-users/:id} : Partial updates given fields of an existing onlineUser, field will ignore if it is null
     *
     * @param id the id of the onlineUserDTO to save.
     * @param onlineUserDTO the onlineUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated onlineUserDTO,
     * or with status {@code 400 (Bad Request)} if the onlineUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the onlineUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the onlineUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/online-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OnlineUserDTO> partialUpdateOnlineUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OnlineUserDTO onlineUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OnlineUser partially : {}, {}", id, onlineUserDTO);
        if (onlineUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, onlineUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!onlineUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OnlineUserDTO> result = onlineUserService.partialUpdate(onlineUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, onlineUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /online-users} : get all the onlineUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of onlineUsers in body.
     */
    @GetMapping("/online-users")
    public List<OnlineUserDTO> getAllOnlineUsers() {
        log.debug("REST request to get all OnlineUsers");
        return onlineUserService.findAll();
    }

    /**
     * {@code GET  /online-users/:id} : get the "id" onlineUser.
     *
     * @param id the id of the onlineUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the onlineUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/online-users/{id}")
    public ResponseEntity<OnlineUserDTO> getOnlineUser(@PathVariable Long id) {
        log.debug("REST request to get OnlineUser : {}", id);
        Optional<OnlineUserDTO> onlineUserDTO = onlineUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(onlineUserDTO);
    }

    /**
     * {@code DELETE  /online-users/:id} : delete the "id" onlineUser.
     *
     * @param id the id of the onlineUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/online-users/{id}")
    public ResponseEntity<Void> deleteOnlineUser(@PathVariable Long id) {
        log.debug("REST request to delete OnlineUser : {}", id);
        onlineUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }


    @PostMapping("/membersProfile/upload/{name}")
    public ResponseEntity<Object> handleFileUpload(@PathVariable String name ,  @RequestParam(value = "file",required = false) MultipartFile file) {
        String folerUrl = "/images/membersProfile/";
        Map<String, String> response = new HashMap<>();
        try {
            if(file != null) {
                String fileName = uploadService.handleFileUpload(name, folerUrl, file);
                if (fileName == null) {
                    throw new IOException("Error uploading file");
                }

                response.put("message", "http://localhost:5051" + folerUrl + fileName);
            }else{
                response.put("message", "");
            }
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/membersProfile/upload/{id}")
    public ResponseEntity<Object> updateFileUpload(@PathVariable Long id , @RequestParam(value = "file",required = false) MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        String fileName;
        Optional<OnlineUser> onlineUser = onlineUserService.findById(id);
        String imageUrl = onlineUser.get().getProfilePicture();
        String folderUrl = "/images/membersProfile/";


        try {
            if(file != null){


            if (imageUrl == null || imageUrl.equals("")) {
                imageUrl = onlineUser.get().getPassword() + "_" + onlineUser.get().getEmail();
                fileName = uploadService.handleFileUpload(imageUrl, folderUrl, file);
            }else {
                uploadService.deleteDocument(folderUrl, imageUrl);
                fileName = uploadService.updateFileUpload(imageUrl, folderUrl, file);
            }


            if (fileName == null) {
                throw new IOException("Error uploading file");
            } else {
                onlineUser.get().setProfilePicture("http://localhost:5051" + folderUrl + fileName);
                onlineUserService.save(onlineUserMapper.toDto(onlineUser.get()));
            }

            response.put("message", "http://localhost:5051" + folderUrl + fileName);
        }else{

                response.put("message", "");
            }
            return ResponseEntity.ok(response);

        }catch (IOException e){
             response = new HashMap<>();
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
