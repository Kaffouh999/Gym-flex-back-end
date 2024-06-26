package com.binarybrothers.gymflexapi.dtos.member;

import com.binarybrothers.gymflexapi.dtos.gymbranch.GymBranchDTO;
import com.binarybrothers.gymflexapi.dtos.user.OnlineUserDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;


@SuppressWarnings("common-java:DuplicatedBlocks")
public class MemberDTO implements Serializable {

    private Long id;

    @NotNull
    private String cin;

    @NotNull
    @Min(value = 3)
    @Max(value = 100)
    private Integer age;

    private String adress;

    @NotNull
    private Boolean gender;

    private GymBranchDTO gymBranch;

    @NotNull
    private OnlineUserDTO onlineUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public GymBranchDTO getGymBranch() {
        return gymBranch;
    }

    public void setGymBranch(GymBranchDTO gymBranch) {
        this.gymBranch = gymBranch;
    }

    @JsonIgnoreProperties(value = "onlineUser")
    public OnlineUserDTO getOnlineUser() {
        return onlineUser;
    }

    public void setOnlineUser(OnlineUserDTO onlineUser) {
        this.onlineUser = onlineUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberDTO)) {
            return false;
        }

        MemberDTO memberDTO = (MemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberDTO{" +
            "id=" + getId() +
            ", cin='" + getCin() + "'" +
            ", age=" + getAge() +
            ", adress='" + getAdress() + "'" +
            ", gender='" + getGender() + "'" +
            ", gymBranch=" + getGymBranch() +
                ", onlineUser=" + getOnlineUser() +
            "}";
    }
}
