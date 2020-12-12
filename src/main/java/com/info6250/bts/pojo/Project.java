package com.info6250.bts.pojo;

import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects")
public class Project implements Serializable{

//    @Id
//    @Column(name = "project_id")
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )
//    private UUID id;

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(name = "project_name")
    private String name;

    @NotNull
    @Column(name = "project_description")
    private String description;

    @NotNull
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "target_end_date")
    private Date targetEndDate;

    @Column(name = "actual_end_date")
    @Temporal(TemporalType.DATE)
    private Date actualEndDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    private Date createdOn;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_on")
    private Date modifiedOn;


    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Project_User_Role> assignedUsers = new ArrayList<Project_User_Role>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Issue> issues;

    public Project() {}

    public void addUser(Project_User_Role newLink){
        assignedUsers.add(newLink);
        newLink.setProject(this);
    }

    public void removeUser(Project_User_Role oldLink){
        assignedUsers.remove(oldLink);
        oldLink.setProject(null);
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public User getManager(){
        for(Project_User_Role p : this.getAssignedUsers()){
            if(p.getRole().getName().equals("manager") && p.getProject().id == this.id)
                return p.getUser();
        }
        return null;
    }

    public List<User> getDevelopers(){
        List<User> result = new ArrayList<User>();
        for(Project_User_Role p : this.getAssignedUsers()){
            if(p.getRole().getName().equals("developer"))
                result.add(p.getUser());
        }
        return result;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getTargetEndDate() {
        return targetEndDate;
    }

    public void setTargetEndDate(Date targetEndDate) {
        this.targetEndDate = targetEndDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public List<Project_User_Role> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<Project_User_Role> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", targetEndDate=" + targetEndDate +
                '}';
    }

    public void updateManager(User manager){
//        List<Project_User_Role> assigned = this.getAssignedUsers();
        for(Project_User_Role p : this.getAssignedUsers()){
            if(p.getRole().getName().equals("manager")){
                p.setUser(manager);
            }
        }
    }
}
