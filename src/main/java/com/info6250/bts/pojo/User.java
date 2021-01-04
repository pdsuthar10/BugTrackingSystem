package com.info6250.bts.pojo;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users", schema = "btsdb")
public class User{

    @Id
    @Column(name = "UserID", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID userId;

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

    @OneToMany(mappedBy = "openedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Issue> openedIssues = new ArrayList<>();

    @Column(name = "is_admin")
    private boolean isAdmin;

    public User() {
    }

    public List<Issue> getAssignedIssues() {
        return assignedIssues;
    }

    public List<Issue> getOpenedIssues() {
        return openedIssues;
    }

    public List<Project> getProjectsManagedByMe(){
        List<Project> projects = new ArrayList<>();
        for(Project_User_Role p : this.getProjects()){
            if(p.getRole().getName().equals("manager"))
                projects.add(p.getProject());
        }
        return projects;
    }

    public Set<Issue> getAllIssues(){
        Set<Issue> issues = new HashSet<>();
        for(Issue issue : this.getAssignedIssues())
            issues.add(issue);
        for(Issue issue : this.getOpenedIssues())
            issues.add(issue);
        return issues;
    }
    public void removeLink(Project_User_Role p){
        int index = this.getProjects().indexOf(p);
        this.getProjects().remove(index);
    }

    public void removeAssignedIssue(Issue issue){
        int index = this.getAssignedIssues().indexOf(issue);
        this.getAssignedIssues().remove(index);
    }

    public void removeOpenedIssue(Issue issue){
        this.getOpenedIssues().remove(this.getOpenedIssues().indexOf(issue));
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
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
