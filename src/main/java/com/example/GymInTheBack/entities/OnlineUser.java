package com.example.GymInTheBack.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.*;

/**
 * A OnlineUser.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "online_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OnlineUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\\\\\.[A-Z]{2,6}$")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @Column(name = "profile_picture")
    private String profilePicture;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Role role;

    @OneToOne(mappedBy = "onlineUser")
    private Member member;

    private String validationKey;//null when email is validated
// jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OnlineUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public OnlineUser firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public OnlineUser lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return this.login;
    }

    public OnlineUser login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return this.email;
    }

    public OnlineUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnoreProperties(value = "onlineUser")
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getValidationKey() {
        return validationKey;
    }

    public void setValidationKey(String validationKey) {
        this.validationKey = validationKey;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> userauthorities= new ArrayList<>();
        if(role != null) {
            if (role.getAnalytics()!= null && role.getAnalytics() ) {
                userauthorities.add(new SimpleGrantedAuthority(Authority.ANALYTICS.name()));
            }
            if (role.getMembership() != null && role.getMembership()) {
                userauthorities.add(new SimpleGrantedAuthority(Authority.MEMBERSHIP.name()));
            }
            if (role.getPayments() != null && role.getPayments()) {
                userauthorities.add(new SimpleGrantedAuthority(Authority.PAYMENT.name()));
            }
            if (role.getInventory() != null && role.getInventory()) {
                userauthorities.add(new SimpleGrantedAuthority(Authority.INVENTORY.name()));
            }
            if (role.getTraining() != null && role.getTraining()) {
                userauthorities.add(new SimpleGrantedAuthority(Authority.TRAINING.name()));
            }
            if (role.getSettings() != null && role.getSettings()) {
                userauthorities.add(new SimpleGrantedAuthority(Authority.SETTINGS.name()));
            }
            if (role.getPreferences() != null && role.getPreferences()) {
                userauthorities.add(new SimpleGrantedAuthority(Authority.PREFERENCES.name()));
            }
            if (role.getManageWebSite() != null && role.getManageWebSite()) {
                userauthorities.add(new SimpleGrantedAuthority(Authority.MANAGEWEBSITE.name()));
            }
        }

        return userauthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public OnlineUser password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public OnlineUser profilePicture(String profilePicture) {
        this.setProfilePicture(profilePicture);
        return this;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OnlineUser)) {
            return false;
        }
        return id != null && id.equals(((OnlineUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OnlineUser{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", login='" + getLogin() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", profilePicture='" + getProfilePicture() + "'" +
            "}";
    }
}
