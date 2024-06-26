package com.binarybrothers.gymflexapi.controllers;

import com.binarybrothers.gymflexapi.dtos.mailing.ContactMailDTO;
import com.binarybrothers.gymflexapi.dtos.user.OnlineUserDTO;
import com.binarybrothers.gymflexapi.entities.OnlineUser;
import com.binarybrothers.gymflexapi.repositories.OnlineUserRepository;
import com.binarybrothers.gymflexapi.services.mappers.OnlineUserMapper;
import com.binarybrothers.gymflexapi.services.upload.IUploadService;
import com.binarybrothers.gymflexapi.services.user.OnlineUserService;
import com.binarybrothers.gymflexapi.utils.BadRequestAlertException;
import com.binarybrothers.gymflexapi.utils.HeaderUtil;
import com.binarybrothers.gymflexapi.utils.ResponseUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OnlineUserResource {

    private final Logger log = LoggerFactory.getLogger(OnlineUserResource.class);
    private static final String ENTITY_NAME = "onlineUser";

    @Value("${APPLICATION_NAME}")
    private String applicationName;

    private final IUploadService uploadService;
    private final OnlineUserMapper onlineUserMapper;
    private final OnlineUserService onlineUserService;
    private final OnlineUserRepository onlineUserRepository;

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
        validateOnlineUser(onlineUserDTO);
        OnlineUserDTO result = onlineUserService.save(onlineUserDTO);
        return ResponseEntity.created(new URI("/api/online-users/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /online-users/:id} : Updates an existing onlineUser.
     *
     * @param id            the id of the onlineUserDTO to save.
     * @param onlineUserDTO the onlineUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated onlineUserDTO,
     * or with status {@code 400 (Bad Request)} if the onlineUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the onlineUserDTO couldn't be updated.
     */
    @PutMapping("/online-users/{id}")
    public ResponseEntity<OnlineUserDTO> updateOnlineUser(@PathVariable(value = "id", required = false) Long id, @Valid @RequestBody OnlineUserDTO onlineUserDTO) {
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

        validateOnlineUser(onlineUserDTO);
        OnlineUserDTO result = onlineUserService.update(onlineUserDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, onlineUserDTO.getId().toString())).body(result);
    }

    /**
     * {@code PATCH  /online-users/:id} : Partial updates given fields of an existing onlineUser, field will ignore if it is null
     *
     * @param id            the id of the onlineUserDTO to save.
     * @param onlineUserDTO the onlineUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated onlineUserDTO,
     * or with status {@code 400 (Bad Request)} if the onlineUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the onlineUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the onlineUserDTO couldn't be updated.
     */
    @PatchMapping(value = "/online-users/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<OnlineUserDTO> partialUpdateOnlineUser(@PathVariable(value = "id", required = false) Long id, @NotNull @RequestBody OnlineUserDTO onlineUserDTO) {
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

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, onlineUserDTO.getId().toString()));
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code POST  /online-users/upload/{name}} : Handles file upload for member profile picture.
     *
     * @param name The name for the uploaded file.
     * @param file The file to be uploaded.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the uploaded file URL in body, or with status {@code 500 (Internal Server Error)} if an error occurs during upload.
     */
    @PostMapping("/online-users/upload/{name}")
    public ResponseEntity<Object> handleFileUpload(@PathVariable String name, @RequestParam(value = "file", required = false) MultipartFile file) {
        String folderUrl = "/images/membersProfile/";
        String msg = "message";
        Map<String, String> response = new HashMap<>();
        try {
            if (file != null) {
                String fileName = uploadService.handleFileUpload(name, folderUrl, file);
                if (fileName == null) {
                    throw new IOException("Error uploading file");
                }

                response.put(msg, folderUrl + fileName);
            } else {
                response.put(msg, "");
            }
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put(msg, "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * {@code PUT  /online-users/upload/{id}} : Updates member profile picture.
     *
     * @param id   The ID of the online user.
     * @param file The file to be uploaded.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the uploaded file URL in body, or with status {@code 500 (Internal Server Error)} if an error occurs during upload.
     */
    @PutMapping("/online-users/upload/{id}")
    public ResponseEntity<Object> updateFileUpload(@PathVariable Long id, @RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        String fileName;
        String msg = "message";
        Optional<OnlineUser> onlineUser = onlineUserService.findById(id);
        String imageUrl = onlineUser.get().getProfilePicture();
        String prodileImagefolders = "/images/membersProfile/";

        try {
            if (file != null) {
                if (imageUrl == null || imageUrl.isEmpty()) {
                    imageUrl = onlineUser.get().getEmail();
                    fileName = uploadService.handleFileUpload(imageUrl, prodileImagefolders, file);
                } else {
                    uploadService.deleteDocument(prodileImagefolders, imageUrl);
                    fileName = uploadService.updateFileUpload(imageUrl, prodileImagefolders, file);
                }

                if (fileName == null) {
                    throw new IOException("Error uploading file");
                } else {
                    onlineUser.get().setProfilePicture(prodileImagefolders + fileName);
                    onlineUserService.save(onlineUserMapper.toDto(onlineUser.get()));
                }

                response.put(msg, prodileImagefolders + fileName);
            } else {
                response.put(msg, "");
            }
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response = new HashMap<>();
            response.put(msg, "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * {@code POST  /controllers/contact} : Handles contact requests.
     *
     * @param contactMail The contact email details.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if the email is sent successfully, or {@code 500 (Internal Server Error)} if an error occurs.
     */
    @PostMapping("/web/contact")
    public ResponseEntity<Object> contact(@RequestBody ContactMailDTO contactMail) {
        try {
            if (onlineUserService.contactUs(contactMail)) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
            }
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email: " + e.getMessage());
        }
    }

    /**
     * {@code GET  /controllers/verify/{validationkey}} : Verifies email address.
     *
     * @param validationKey The validation key to verify email.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} if the email is verified successfully, or {@code 500 (Internal Server Error)} if an error occurs.
     */
    @GetMapping("/web/verify/{validationKey}")
    public ResponseEntity<Object> verifyEmail(@PathVariable String validationKey) {
        if (onlineUserService.validateMail(validationKey)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    private void validateOnlineUser(OnlineUserDTO onlineUserDTO) {
        if (onlineUserDTO.getFirstName() == null || onlineUserDTO.getFirstName().trim().isEmpty()) {
            throw new BadRequestAlertException("A new onlineUser must have a first name", ENTITY_NAME, "firstnamerequired");
        }
        if (onlineUserDTO.getLastName() == null || onlineUserDTO.getLastName().trim().isEmpty()) {
            throw new BadRequestAlertException("A new onlineUser must have a last name", ENTITY_NAME, "lastnamerequired");
        }
        if (onlineUserDTO.getEmail() == null || onlineUserDTO.getEmail().trim().isEmpty()) {
            throw new BadRequestAlertException("A new onlineUser must have an email", ENTITY_NAME, "emailrequired");
        }
        if (onlineUserDTO.getPassword() == null || onlineUserDTO.getPassword().trim().isEmpty()) {
            throw new BadRequestAlertException("A new onlineUser must have a password", ENTITY_NAME, "passwordrequired");
        }
        if (onlineUserDTO.getLogin() == null || onlineUserDTO.getLogin().trim().isEmpty()) {
            throw new BadRequestAlertException("A new onlineUser must have a login", ENTITY_NAME, "loginrequired");
        }
    }
}
