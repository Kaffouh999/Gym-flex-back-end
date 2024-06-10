package com.example.GymInTheBack.web;

import com.example.GymInTheBack.dtos.cart.CartDTO;
import com.example.GymInTheBack.repositories.CartRepository;
import com.example.GymInTheBack.services.cart.CartService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing carts.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartResource {

    private final Logger log = LoggerFactory.getLogger(CartResource.class);

    private static final String ENTITY_NAME = "cart";

    @Value("${APPLICATION_NAME}")
    private String applicationName;

    private final CartService cartService;
    private final CartRepository cartRepository;


    /**
     * POST  /carts : Create a new cart.
     *
     * @param cartDTO the cartDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cartDTO, or with status 400 (Bad Request) if the cart has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/carts")
    public ResponseEntity<CartDTO> createCart(@Valid @RequestBody CartDTO cartDTO) throws URISyntaxException {
        log.debug("REST request to save Cart : {}", cartDTO);
        validateNewCart(cartDTO);

        CartDTO result = cartService.save(cartDTO);
        return ResponseEntity.created(new URI("/api/carts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /carts/:id : Updates an existing cart.
     *
     * @param id      the id of the cartDTO to save
     * @param cartDTO the cartDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cartDTO,
     * or with status 400 (Bad Request) if the cartDTO is not valid,
     * or with status 500 (Internal Server Error) if the cartDTO couldn't be updated
     */
    @PutMapping("/carts/{id}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable Long id, @Valid @RequestBody CartDTO cartDTO) {
        log.debug("REST request to update Cart : {}, {}", id, cartDTO);
        validateCartId(cartDTO.getId(), id);

        CartDTO result = cartService.update(cartDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cartDTO.getId().toString()))
                .body(result);
    }

    /**
     * PATCH  /carts/:id : Partial updates given fields of an existing cart, field will ignore if it is null
     *
     * @param id      the id of the cartDTO to save
     * @param cartDTO the cartDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cartDTO,
     * or with status 400 (Bad Request) if the cartDTO is not valid,
     * or with status 404 (Not Found) if the cartDTO is not found,
     * or with status 500 (Internal Server Error) if the cartDTO couldn't be updated
     */
    @PatchMapping(value = "/carts/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<CartDTO> partialUpdateCart(@PathVariable Long id, @NotNull @RequestBody CartDTO cartDTO) {
        log.debug("REST request to partial update Cart partially : {}, {}", id, cartDTO);
        validateCartId(cartDTO.getId(), id);

        Optional<CartDTO> result = cartService.partialUpdate(cartDTO);
        return ResponseUtil.wrapOrNotFound(result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cartDTO.getId().toString()));
    }

    /**
     * GET  /carts : get all the carts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of carts in body
     */
    @GetMapping("/carts")
    public List<CartDTO> getAllCarts() {
        log.debug("REST request to get all Carts");
        return cartService.findAll();
    }

    /**
     * GET  /carts/:id : get the "id" cart.
     *
     * @param id the id of the cartDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cartDTO, or with status 404 (Not Found)
     */
    @GetMapping("/carts/{id}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long id) {
        log.debug("REST request to get Cart : {}", id);
        Optional<CartDTO> cartDTO = cartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cartDTO);
    }

    /**
     * DELETE  /carts/:id : delete the "id" cart.
     *
     * @param id the id of the cartDTO to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/carts/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        log.debug("REST request to delete Cart : {}", id);
        cartService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    private void validateNewCart(CartDTO cartDTO) {
        if (cartDTO.getId() != null) {
            throw new BadRequestAlertException("A new cart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (cartDTO.getShippingAdress() == null || cartDTO.getShippingAdress().trim().isEmpty()) {
            throw new BadRequestAlertException("A new cart should have a shipping address", ENTITY_NAME, "shippingAddressRequired");
        }
    }

    private void validateCartId(Long cartDTOId, Long id) {
        if (cartDTOId == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cartDTOId)) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!cartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }
}
