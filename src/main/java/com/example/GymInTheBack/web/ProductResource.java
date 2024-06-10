package com.example.GymInTheBack.web;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.GymInTheBack.dtos.product.ProductDTO;
import com.example.GymInTheBack.entities.Product;
import com.example.GymInTheBack.repositories.ProductRepository;
import com.example.GymInTheBack.services.mappers.ProductMapper;
import com.example.GymInTheBack.services.product.ProductService;
import com.example.GymInTheBack.services.upload.IUploadService;
import com.example.GymInTheBack.utils.BadRequestAlertException;
import com.example.GymInTheBack.utils.HeaderUtil;
import com.example.GymInTheBack.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "product";

    @Value("${APPLICATION_NAME}")
    private String APPLICATION_NAME;
    private final ProductMapper productMapper;
    private final IUploadService uploadService;
    private final ProductService productService;
    private final ProductRepository productRepository;


    /**
     * {@code POST  /products} : Create a new product.
     *
     * @param productDTO the productDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productDTO, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to save Product : {}", productDTO);
        if (productDTO.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (productDTO.getPrice() == null || productDTO.getPrice() <= 0) {
            throw new BadRequestAlertException("A new product should have a price", ENTITY_NAME, "pricerequired");
        }
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new BadRequestAlertException("A new product should have a name", ENTITY_NAME, "namerequired");
        }
        ProductDTO result = productService.save(productDTO);
        return ResponseEntity.created(new URI("/api/products/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * {@code PUT  /products/:id} : Updates an existing product.
     *
     * @param id         the id of the productDTO to save.
     * @param productDTO the productDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDTO,
     * or with status {@code 400 (Bad Request)} if the productDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productDTO couldn't be updated.
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody ProductDTO productDTO) {
        log.debug("REST request to update Product : {}, {}", id, productDTO);
        if (productDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductDTO result = productService.update(productDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, productDTO.getId().toString())).body(result);
    }

    /**
     * {@code PATCH  /products/:id} : Partial updates given fields of an existing product, field will ignore if it is null
     *
     * @param id         the id of the productDTO to save.
     * @param productDTO the productDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productDTO,
     * or with status {@code 400 (Bad Request)} if the productDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productDTO couldn't be updated.
     */
    @PatchMapping(value = "/products/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<ProductDTO> partialUpdateProduct(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody ProductDTO productDTO) {
        log.debug("REST request to partial update Product partially : {}, {}", id, productDTO);
        if (productDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductDTO> result = productService.partialUpdate(productDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, productDTO.getId().toString()));
    }

    /**
     * {@code GET  /products} : get all the products.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of products in body.
     */
    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        log.debug("REST request to get all Products");
        return productService.findAll();
    }

    /**
     * {@code GET  /products/:id} : get the "id" product.
     *
     * @param id the id of the productDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        Optional<ProductDTO> productDTO = productService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productDTO);
    }

    /**
     * {@code DELETE  /products/:id} : delete the "id" product.
     *
     * @param id the id of the productDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/products/upload/{name}")
    public ResponseEntity<Object> handleFileUpload(@PathVariable String name, @RequestParam(value = "file", required = false) MultipartFile file) {
        String folderUrl = "/images/productsPictures/";
        Map<String, String> response = new HashMap<>();
        try {
            if (file != null) {
                String fileName = uploadService.handleFileUpload(name, folderUrl, file);
                if (fileName == null) {
                    throw new IOException("Error uploading file");
                }

                response.put("message", "http://localhost:5051" + folderUrl + fileName);
            } else {
                response.put("message", "");
            }
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/products/upload/{id}")
    public ResponseEntity<Object> updateFileUpload(@PathVariable Long id, @RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        String fileName;
        Optional<Product> product = productService.findById(id);
        String imageUrl = product.get().getImageUrl();
        String folderUrl = "/images/productsPictures/";

        try {
            if (file != null) {
                uploadService.deleteDocument(folderUrl, imageUrl);

                if (imageUrl == null || imageUrl.isEmpty()) {
                    imageUrl = product.get().getName() + "_" + product.get().getSubCategory();
                    fileName = uploadService.handleFileUpload(imageUrl, folderUrl, file);
                } else {
                    fileName = uploadService.updateFileUpload(imageUrl, folderUrl, file);
                }

                if (fileName == null) {
                    throw new IOException("Error uploading file");
                } else {
                    product.get().setImageUrl("http://localhost:5051" + folderUrl + fileName);
                    productService.save(productMapper.toDto(product.get()));
                }

                response.put("message", "http://localhost:5051" + folderUrl + fileName);
            } else {

                response.put("message", "");
            }
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response = new HashMap<>();
            response.put("message", "Error uploading file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
