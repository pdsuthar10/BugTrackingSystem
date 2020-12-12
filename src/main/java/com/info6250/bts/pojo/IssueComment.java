package com.info6250.bts.pojo;

import javax.persistence.*;

@Entity
@Table(name = "issues_comments")
public class IssueComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User commentedBy;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

    public IssueComment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(User commentedBy) {
        this.commentedBy = commentedBy;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
}
