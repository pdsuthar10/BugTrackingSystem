package com.info6250.bts.controller;

import com.info6250.bts.dao.IssueCommentDAO;
import com.info6250.bts.dao.IssueDAO;
import com.info6250.bts.dao.UserDAO;
import com.info6250.bts.pojo.Issue;
import com.info6250.bts.pojo.IssueComment;
import com.info6250.bts.pojo.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/user/{user_id}/issues")
    public JSONArray getMyIssues(@PathVariable(name = "user_id") String user_id,
                                 UserDAO userDAO, HttpServletRequest request){
        int id;
        try {
            id=Integer.parseInt(user_id);
        }catch (Exception e){
            return null;
        }
        String type = request.getParameter("type");
        User user = userDAO.findById(id);
        if(user == null || type == null || type.trim().equals("")) return null;

        List<Issue> issues;
        if(type.equals("assigned"))
            issues = user.getAssignedIssues();
        else if(type.equals("opened"))
            issues = user.getOpenedIssues();
        else if(type.equals("all"))
            issues = user.getAllIssues();
        else
            return null;

        JSONArray result = new JSONArray();
        for(Issue issue : issues){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("issueId",issue.getId());
            jsonObject.put("projectId", issue.getProject().getId());
            jsonObject.put("title", issue.getTitle());
            jsonObject.put("description", issue.getDescription());
            jsonObject.put("status", issue.getStatus().getName());
            jsonObject.put("issueType", issue.getIssueType());
            jsonObject.put("priority", issue.getPriority().getName());
            jsonObject.put("openedBy", issue.getOpenedBy().getName()+" ("+issue.getOpenedBy().getUsername()+")");
            jsonObject.put("assignedTo", issue.getAssignedTo().getName()+" ("+issue.getAssignedTo().getUsername()+")");
            jsonObject.put("createdOn", issue.getCreatedOn().toLocaleString());
            jsonObject.put("modifiedOn", issue.getModifiedOn().toLocaleString());
            jsonObject.put("viewLink","/bts/project/"+issue.getProject().getId()+"/issues/"+issue.getId()+"/details");
            jsonObject.put("editLink","/bts/project/"+issue.getProject().getId()+"/issues/"+issue.getId()+"/edit-issue");
            jsonObject.put("deleteLink","/bts/project/"+issue.getProject().getId()+"/issues/"+issue.getId()+"/delete-issue");
            result.put(jsonObject);
        }
        return result;
    }
}
