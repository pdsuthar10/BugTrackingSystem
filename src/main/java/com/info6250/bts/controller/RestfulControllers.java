package com.info6250.bts.controller;

import com.info6250.bts.dao.IssueCommentDAO;
import com.info6250.bts.dao.IssueDAO;
import com.info6250.bts.pojo.Issue;
import com.info6250.bts.pojo.IssueComment;
import com.info6250.bts.pojo.User;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RestfulControllers {

    @GetMapping(value = "/issues/{issue_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject getIssueDetails(@PathVariable(name = "issue_id") String issue_id,
                                      IssueDAO issueDAO){
        Issue issue = issueDAO.findById(Long.parseLong(issue_id));
        JSONObject result = new JSONObject();
        result.put("title", issue.getTitle());
        result.put("description", issue.getDescription());
        result.put("priority",issue.getPriority().getName());
        result.put("issueType",issue.getIssueType());
        result.put("assignedTo", issue.getAssignedTo().getUsername());
        List<User> assignedDevelopers = issue.getProject().getDevelopers();
        List<String> developers = new ArrayList<>();
        for(User u : assignedDevelopers)
            developers.add(u.getUsername());
        result.put("developers", developers);
        return result;
    }

    @PostMapping(value = "/issue/{issue_id}/add-comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject addComment(@PathVariable(name = "issue_id") String issue_id,
                                 IssueDAO issueDAO, @RequestBody String commentInput,
                                 IssueCommentDAO issueCommentDAO, HttpSession session){
        Issue issue = issueDAO.findById(Integer.parseInt(issue_id));
        System.out.println("in controlleerrrr");
        System.out.println(commentInput);
        if(commentInput == null || commentInput.trim().equals("")) return null;
        User user = (User) session.getAttribute("user");
        System.out.println(commentInput);
        IssueComment comment = new IssueComment();
        comment.setComment(commentInput);
        comment.setIssue(issue);
        comment.setCommentedBy(user);
        issueCommentDAO.addComment(comment);
        JSONObject com = new JSONObject();
        com.put("id", comment.getId());
        com.put("comment", comment.getComment());
        com.put("commentedBy", user.getName()+" ("+user.getUsername()+")");
        com.put("createdOn", comment.getCreatedOn());
        return com;
    }
}
