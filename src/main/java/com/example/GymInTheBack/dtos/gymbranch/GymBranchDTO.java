package com.example.GymInTheBack.dtos.gymbranch;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;


@SuppressWarnings("common-java:DuplicatedBlocks")
public class GymBranchDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Double latitude;

    private Double longitude;

    private String adress;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\\\\\.[A-Z]{2,6}$")
    private String email;

    private String appPasswordEmail;

    private String phoneNumber;

    @NotNull
    private ZonedDateTime openingDate;

    @NotNull
    private ZonedDateTime closingDate;

    @NotNull
    private Float sessionDurationAllowed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppPasswordEmail() {
        return appPasswordEmail;
    }

    public void setAppPasswordEmail(String appPasswordEmail) {
        this.appPasswordEmail = appPasswordEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ZonedDateTime getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(ZonedDateTime openingDate) {
        this.openingDate = openingDate;
    }

    public ZonedDateTime getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(ZonedDateTime closingDate) {
        this.closingDate = closingDate;
    }

    public Float getSessionDurationAllowed() {
        return sessionDurationAllowed;
    }

    public void setSessionDurationAllowed(Float sessionDurationAllowed) {
        this.sessionDurationAllowed = sessionDurationAllowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GymBranchDTO)) {
            return false;
        }

        GymBranchDTO gymBranchDTO = (GymBranchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gymBranchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GymBranchDTO{" +
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
