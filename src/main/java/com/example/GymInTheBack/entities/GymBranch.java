package com.example.GymInTheBack.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;

/**
 * A GymBranch.
 */
@Entity
@Table(name = "gym_branch")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GymBranch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "adress")
    private String adress;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\\\\\.[A-Z]{2,6}$")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "app_password_email")
    private String appPasswordEmail;

    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "opening_date", nullable = false)
    private ZonedDateTime openingDate;

    @NotNull
    @Column(name = "closing_date", nullable = false)
    private ZonedDateTime closingDate;

    @NotNull
    @Column(name = "session_duration_allowed", nullable = false)
    private Float sessionDurationAllowed;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GymBranch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public GymBranch name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public GymBranch latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public GymBranch longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAdress() {
        return this.adress;
    }

    public GymBranch adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return this.email;
    }

    public GymBranch email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppPasswordEmail() {
        return this.appPasswordEmail;
    }

    public GymBranch appPasswordEmail(String appPasswordEmail) {
        this.setAppPasswordEmail(appPasswordEmail);
        return this;
    }

    public void setAppPasswordEmail(String appPasswordEmail) {
        this.appPasswordEmail = appPasswordEmail;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public GymBranch phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ZonedDateTime getOpeningDate() {
        return this.openingDate;
    }

    public GymBranch openingDate(ZonedDateTime openingDate) {
        this.setOpeningDate(openingDate);
        return this;
    }

    public void setOpeningDate(ZonedDateTime openingDate) {
        this.openingDate = openingDate;
    }

    public ZonedDateTime getClosingDate() {
        return this.closingDate;
    }

    public GymBranch closingDate(ZonedDateTime closingDate) {
        this.setClosingDate(closingDate);
        return this;
    }

    public void setClosingDate(ZonedDateTime closingDate) {
        this.closingDate = closingDate;
    }

    public Float getSessionDurationAllowed() {
        return this.sessionDurationAllowed;
    }

    public GymBranch sessionDurationAllowed(Float sessionDurationAllowed) {
        this.setSessionDurationAllowed(sessionDurationAllowed);
        return this;
    }

    public void setSessionDurationAllowed(Float sessionDurationAllowed) {
        this.sessionDurationAllowed = sessionDurationAllowed;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GymBranch)) {
            return false;
        }
        return id != null && id.equals(((GymBranch) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GymBranch{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", adress='" + getAdress() + "'" +
            ", email='" + getEmail() + "'" +
            ", appPasswordEmail='" + getAppPasswordEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", openingDate='" + getOpeningDate() + "'" +
            ", closingDate='" + getClosingDate() + "'" +
            ", sessionDurationAllowed=" + getSessionDurationAllowed() +
            "}";
    }
}
