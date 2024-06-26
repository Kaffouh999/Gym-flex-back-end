package com.binarybrothers.gymflexapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.*;

/**
 * A Category.
 */
@Entity
@Table (name = "category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "is_for_client", nullable = false)
    private Boolean isForClient;

    @NotNull
    @Column(name = "is_for_inventory", nullable = false)
    private Boolean isForInventory;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<SubCategory> subCategoryList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Category description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsForClient() {
        return this.isForClient;
    }

    public Category isForClient(Boolean isForClient) {
        this.setIsForClient(isForClient);
        return this;
    }

    public void setIsForClient(Boolean isForClient) {
        this.isForClient = isForClient;
    }

    public Boolean getIsForInventory() {
        return this.isForInventory;
    }

    public Category isForInventory(Boolean isForInventory) {
        this.setIsForInventory(isForInventory);
        return this;
    }

    public void setIsForInventory(Boolean isForInventory) {
        this.isForInventory = isForInventory;
    }

    public List<SubCategory> getSubCategoryList() {
        return subCategoryList;
    }
// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isForClient='" + getIsForClient() + "'" +
            ", isForInventory='" + getIsForInventory() + "'" +
            "}";
    }
}
