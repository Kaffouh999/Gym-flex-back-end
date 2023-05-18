package com.example.GymInTheBack.web;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.category.CategoryDTO;
import com.example.GymInTheBack.dtos.role.RoleDTO;
import com.example.GymInTheBack.entities.Category;
import com.example.GymInTheBack.entities.OnlineUser;
import com.example.GymInTheBack.entities.Role;
import com.example.GymInTheBack.entities.SubCategory;
import com.example.GymInTheBack.repositories.RoleRepository;
import com.example.GymInTheBack.services.role.RoleService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RoleResource {
    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    private static final String ENTITY_NAME = "role";


    private String applicationName ="GymFlex";

    private final RoleService roleService;

    private final RoleRepository roleRepository;

    public RoleResource(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/roles")
    public ResponseEntity<Object> createRole(@Valid @RequestBody RoleDTO roleDTO) throws URISyntaxException {
        log.debug("REST request to save Role : {}", roleDTO);

        if ( roleService.existsByName(roleDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Role name is already used");
        }

        if (roleDTO.getId() != null) {
            throw new BadRequestAlertException("A new role cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (roleDTO.getName() == null || roleDTO.getName().trim().equals("")) {
            throw new BadRequestAlertException("A new role must have a name", ENTITY_NAME, "namerequired");
        }

        RoleDTO result = roleService.save(roleDTO);
        return ResponseEntity
                .created(new URI("/api/roles/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }


    @PutMapping("/roles/{id}")
    public ResponseEntity<Object> updateRole(
            @PathVariable(value = "id", required = false) final Long id,
            @Valid @RequestBody RoleDTO roleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Role : {}, {}", id, roleDTO);

        RoleDTO oldRole = roleService.findOne(id).get();
        if ( !oldRole.getName().equals(roleDTO.getName()) && roleService.existsByName(roleDTO.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Role name is already used");
        }

        if (roleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoleDTO result = roleService.update(roleDTO);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleDTO.getId().toString()))
                .body(result);
    }


    @PatchMapping(value = "/roles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoleDTO> partialUpdateRole(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody RoleDTO roleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Role partially : {}, {}", id, roleDTO);
        if (roleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleDTO> result = roleService.partialUpdate(roleDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleDTO.getId().toString())
        );
    }



    @GetMapping("/roles")
    public List<RoleDTO> getAllCRoles() {
        log.debug("REST request to get all Categories");
        List<RoleDTO> roleDTOS = roleService.findAll();
        return roleService.findAll();
    }


    @GetMapping("/roles/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable Long id) {
        log.debug("REST request to get Role : {}", id);
        Optional<RoleDTO> roleDTO = roleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roleDTO);
    }


    @DeleteMapping(value = "/roles/{id}",produces = "text/plain")
    public ResponseEntity<Object> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role : {}", id);
        Role role = roleRepository.findById(id).orElse(null);

        if(role.getOnlineUserList() != null && !role.getOnlineUserList().isEmpty()){
            String errorMessage = "Cannot delete category with those associated subcategories : ";
            for(OnlineUser onlineUser : role.getOnlineUserList()){
                errorMessage += " -> (" +onlineUser.getFirstName()+" "+onlineUser.getLastName()+")" ;
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
        }

        roleService.delete(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}
