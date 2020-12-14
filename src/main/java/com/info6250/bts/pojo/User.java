package com.info6250.bts.pojo;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "btsdb")
public class User implements Serializable{

//    @Id
//    @Column(name = "UserID")
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )
//    private UUID userId;

    @Id
    @Column(name = "UserID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    private String username;
    private String password;
    private String name;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    private Date createdOn;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_on")
    private Date modifiedOn;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project_User_Role> projects = new ArrayList<Project_User_Role>();

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Issue> assignedIssues = new ArrayList<>();

    @Column(name = "is_admin")
    private boolean isAdmin;

    public User() {
    }

    public List<Issue> getAssignedIssues() {
        return assignedIssues;
    }

    public void removeLink(Project_User_Role p){
        int index = this.getProjects().indexOf(p);
        this.getProjects().remove(index);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "UserName", unique = true)
    public String getUsername() {
        return username;
    }

    public boolean isDeveloperForProject(Project project){
        List<Project_User_Role> assignedUsers = project.getAssignedUsers();
        for(Project_User_Role p : assignedUsers){
            if(p.getRole().getName().equals("developer") && p.getUser().getUsername().equals(this.username))
                return true;
        }
        return false;
    }

    public boolean isManagerForProject(Project project){
       if(project.getManager().username.equals(this.username)) return true;
       return false;
    }

    public boolean isDeveloper(){
        for(Project_User_Role p : this.getProjects()){
            if(p.getRole().getName().equals("developer")) return true;
        }
        return false;
    }

    public boolean assignedIssue(Issue issue){
        if(issue.getAssignedTo().getUsername().equals(this.username)) return true;
        return false;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String userPassword) {
        this.password = userPassword;
    }

    public List<Project_User_Role> getProjects() {
        return projects;
    }

    public void setProjects(List<Project_User_Role> projects) {
        this.projects = projects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", createdOn=" + createdOn +
                ", modifiedOn=" + modifiedOn +
                ", projects=" + projects +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
