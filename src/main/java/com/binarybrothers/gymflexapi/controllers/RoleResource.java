package com.binarybrothers.gymflexapi.controllers;

import com.binarybrothers.gymflexapi.dtos.role.RoleDTO;
import com.binarybrothers.gymflexapi.entities.OnlineUser;
import com.binarybrothers.gymflexapi.entities.Role;
import com.binarybrothers.gymflexapi.repositories.RoleRepository;
import com.binarybrothers.gymflexapi.services.role.RoleService;
import com.binarybrothers.gymflexapi.utils.BadRequestAlertException;
import com.binarybrothers.gymflexapi.utils.HeaderUtil;
import com.binarybrothers.gymflexapi.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RoleResource {
    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    private static final String ENTITY_NAME = "role";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;

    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @PostMapping("/roles")
    public ResponseEntity<Object> createRole(@Valid @RequestBody RoleDTO roleDTO) throws URISyntaxException {
        log.debug("REST request to save Role: {}", roleDTO);

        if (roleService.existsByName(roleDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Role name is already used");
        }

        if (roleDTO.getId() != null) {
            throw new BadRequestAlertException("A new role cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (roleDTO.getName() == null || roleDTO.getName().trim().isEmpty()) {
            throw new BadRequestAlertException("A new role must have a name", ENTITY_NAME, "namerequired");
        }

        RoleDTO result = roleService.save(roleDTO);
        return ResponseEntity
                .created(new URI("/api/roles/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable(value = "id", required = false) Long id, @Valid @RequestBody RoleDTO roleDTO) {
        log.debug("REST request to update Role: {}, {}", id, roleDTO);

        RoleDTO oldRole = roleService.findOne(id).orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));

        if (!oldRole.getName().equals(roleDTO.getName()) && roleService.existsByName(roleDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Role name is already used");
        }

        if (!Objects.equals(id, roleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        RoleDTO result = roleService.update(roleDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, roleDTO.getId().toString()))
                .body(result);
    }

    @PatchMapping(value = "/roles/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<RoleDTO> partialUpdateRole(@PathVariable(value = "id", required = false) Long id, @NotNull @RequestBody RoleDTO roleDTO) {
        log.debug("REST request to partial update Role partially: {}, {}", id, roleDTO);
        if (!Objects.equals(id, roleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleDTO> result = roleService.partialUpdate(roleDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, roleDTO.getId().toString()));
    }

    @GetMapping("/roles")
    public List<RoleDTO> getAllRoles() {
        log.debug("REST request to get all Roles");
        return roleService.findAll();
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable Long id) {
        log.debug("REST request to get Role: {}", id);
        Optional<RoleDTO> roleDTO = roleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roleDTO);
    }

    @DeleteMapping(value = "/roles/{id}", produces = "text/plain")
    public ResponseEntity<Object> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role: {}", id);
        Role role = roleRepository.findById(id).orElse(null);

        if (role != null && role.getOnlineUserList() != null && !role.getOnlineUserList().isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Cannot delete category with those associated subcategories : ");
            for (OnlineUser onlineUser : role.getOnlineUserList()) {
                errorMessage.append(" -> (").append(onlineUser.getFirstName()).append(" ").append(onlineUser.getLastName()).append(")");
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage.toString());
        }

        roleService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString()))
                .build();
    }
}