package com.binarybrothers.gymflexapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private  Long id;

  private String name;

  private  String description;

  private Boolean analytics;

  private Boolean membership;

  private Boolean payments; // only if he has membership

  private Boolean inventory;

  private Boolean training;

  private Boolean settings;

  private Boolean preferences;

  private Boolean manageWebSite;

  private Boolean coach;

  private Boolean blogs;

  private Boolean store;

  @OneToMany(mappedBy = "role")
  @JsonIgnore
  private List<OnlineUser> onlineUserList;


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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getAnalytics() {
    return analytics;
  }

  public void setAnalytics(Boolean analytics) {
    this.analytics = analytics;
  }

  public Boolean getMembership() {
    return membership;
  }

  public void setMembership(Boolean membership) {
    this.membership = membership;
  }

  public Boolean getPayments() {
    return payments;
  }

  public void setPayments(Boolean payments) {
    this.payments = payments;
  }

  public Boolean getInventory() {
    return inventory;
  }

  public void setInventory(Boolean inventory) {
    this.inventory = inventory;
  }

  public Boolean getTraining() {
    return training;
  }

  public void setTraining(Boolean training) {
    this.training = training;
  }

  public Boolean getSettings() {
    return settings;
  }

  public void setSettings(Boolean settings) {
    this.settings = settings;
  }

  public Boolean getPreferences() {
    return preferences;
  }

  public void setPreferences(Boolean preferences) {
    this.preferences = preferences;
  }

  public Boolean getManageWebSite() {
    return manageWebSite;
  }

  public void setManageWebSite(Boolean manageWebSite) {
    this.manageWebSite = manageWebSite;
  }

  public Boolean getCoach() {
    return coach;
  }

  public void setCoach(Boolean coach) {
    this.coach = coach;
  }

  public Boolean getBlogs() {
        return blogs;
    }

  public void setBlogs(Boolean blogs) {
        this.blogs = blogs;
  }

  public Boolean getStore() {
        return store;
  }

  public void setStore(Boolean store) {
        this.store = store;
  }

  public List<OnlineUser> getOnlineUserList() {
    return onlineUserList;
  }

  public void setOnlineUserList(List<OnlineUser> onlineUserList) {
    this.onlineUserList = onlineUserList;
  }
}
