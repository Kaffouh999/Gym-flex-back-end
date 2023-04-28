package com.example.GymInTheBack.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.*;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "cin", nullable = false, unique = true)
    private String cin;

    @NotNull
    @Min(value = 3)
    @Max(value = 100)
    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "adress")
    private String adress;

    @NotNull
    @Column(name = "gender", nullable = false)
    private Boolean gender;

    @ManyToOne(optional = false)
    @NotNull
    private GymBranch gymBranch;

    @OneToOne(optional = false,cascade = CascadeType.MERGE)
    @NotNull
    private OnlineUser onlineUser;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AssuranceMember> assuranceMemberList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SubscriptionMember> subscriptionMemberList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Member id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCin() {
        return this.cin;
    }

    public Member cin(String cin) {
        this.setCin(cin);
        return this;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Integer getAge() {
        return this.age;
    }

    public Member age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAdress() {
        return this.adress;
    }

    public Member adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Boolean getGender() {
        return this.gender;
    }

    public Member gender(Boolean gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public GymBranch getGymBranch() {
        return this.gymBranch;
    }

    public void setGymBranch(GymBranch gymBranch) {
        this.gymBranch = gymBranch;
    }

    public Member gymBranch(GymBranch gymBranch) {
        this.setGymBranch(gymBranch);
        return this;
    }

    public OnlineUser getOnlineUser() {
        return onlineUser;
    }

    public void setOnlineUser(OnlineUser onlineUser) {
        this.onlineUser = onlineUser;
    }

    public Member onlineUser(OnlineUser onlineUser) {
        this.setOnlineUser(onlineUser);
        return this;
    }

    public List<AssuranceMember> getAssuranceMemberList() {
        return assuranceMemberList;
    }

    public List<SubscriptionMember> getSubscriptionMemberList() {
        return subscriptionMemberList;
    }
// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return id != null && id.equals(((Member) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", cin='" + getCin() + "'" +
            ", age=" + getAge() +
            ", adress='" + getAdress() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
