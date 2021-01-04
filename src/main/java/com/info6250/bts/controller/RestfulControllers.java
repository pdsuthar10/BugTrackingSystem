package com.info6250.bts.controller;

import com.info6250.bts.dao.IssueCommentDAO;
import com.info6250.bts.dao.IssueDAO;
import com.info6250.bts.dao.ProjectDAO;
import com.info6250.bts.dao.UserDAO;
import com.info6250.bts.pojo.Issue;
import com.info6250.bts.pojo.IssueComment;
import com.info6250.bts.pojo.Project;
import com.info6250.bts.pojo.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.*;

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

    @PostMapping(value = "/api/user/{username}/issues", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONArray getIssues(@PathVariable(name = "username") String username,
                                 UserDAO userDAO, @RequestBody @NotNull String filter,
                                 IssueDAO issueDAO){

        System.out.println("type: "+filter);
        User user = userDAO.findUserByUsername(username);
        if(user == null || filter == null || filter.trim().equals("")) return null;

        List<Issue> issues;
        if(filter.equals("assigned"))
            issues = user.getAssignedIssues();
        else if(filter.equals("opened"))
            issues = user.getOpenedIssues();
        else if(filter.equals("all")) {
            Set<Issue> temp = user.getAllIssues();
            issues = new ArrayList<>(temp);
        }else if (filter.equals("open") || filter.equals("closed")) {
            List<Issue> temp = issueDAO.findAllIssues();
            issues = new ArrayList<>();
            for (Issue issue : temp) {
                if (issue.getStatus().getName().equals(filter))
                    issues.add(issue);
            }
        }else if(filter.equals("allIssues")){
            issues = issueDAO.findAllIssues();
        }
        else
            return null;

        System.out.println(issues.size());

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
            if(user.isAdmin() || user.assignedIssue(issue) || user.isManagerForProject(issue.getProject()))
                jsonObject.put("editLink","/bts/project/"+issue.getProject().getId()+"/issues/"+issue.getId()+"/edit-issue");
            if(user.isAdmin() || user.isManagerForProject(issue.getProject()))
                jsonObject.put("deleteLink","/bts/project/"+issue.getProject().getId()+"/issues/"+issue.getId()+"/delete-issue");
            result.put(jsonObject);
        }
        System.out.println(result);
        return result;
    }


    @PostMapping(value = "/api/user/{user_id}/project/{project_id}/issues", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONArray getIssues(@PathVariable(name = "project_id") String project_id,
                               @PathVariable(name = "user_id") String user_id, UserDAO userDAO,
                               ProjectDAO projectDAO, @RequestBody @NotNull String filter,
                               IssueDAO issueDAO){

        int id;
        UUID userId;
        try {
            id = Integer.parseInt(project_id);
            userId = UUID.fromString(user_id);
        }catch (Exception e){
            return null;
        }
        Project project = projectDAO.findProjectById(id);
        User user = userDAO.findById(userId);
        if(project == null || user == null || filter == null || filter.trim().equals("")) return null;

        List<Issue> issues;
        if(filter.equals("all")) {
            issues = project.getIssues();
        }else if (filter.equals("open") || filter.equals("closed")) {
            List<Issue> temp = project.getIssues();
            issues = new ArrayList<>();
            for (Issue issue : temp) {
                if (issue.getStatus().getName().equals(filter))
                    issues.add(issue);
            }
        }
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
            if(user.isAdmin() || user.assignedIssue(issue) || user.isManagerForProject(issue.getProject()))
                jsonObject.put("editLink","/bts/project/"+issue.getProject().getId()+"/issues/"+issue.getId()+"/edit-issue");
            if(user.isAdmin() || user.isManagerForProject(issue.getProject()))
                jsonObject.put("deleteLink","/bts/project/"+issue.getProject().getId()+"/issues/"+issue.getId()+"/delete-issue");
            result.put(jsonObject);
        }
        System.out.println(result);
        return result;
    }

    @PostMapping(value = "/api/user/issues", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONArray getSearchIssues(@RequestBody String filter,
                               IssueDAO issueDAO, HttpSession session){

        User user = (User) session.getAttribute("user");
        List<Issue> issues = new ArrayList<>();
        if(filter.equals("all")){
            issues = issueDAO.findAllIssues();
        }else if(!(filter == null || filter.trim().equals(""))) {
            filter = filter.toLowerCase();
            for(Issue issue: issueDAO.findAllIssues()){
                if(String.valueOf(issue.getId()).equals(filter)
                    || String.valueOf(issue.getProject().getId()).equals(filter)
                    || issue.getTitle().toLowerCase().contains(filter)
                    || issue.getDescription().toLowerCase().contains(filter)
                    || issue.getStatus().getName().toLowerCase().contains(filter)
                    || issue.getIssueType().toLowerCase().contains(filter)
                    || issue.getPriority().getName().toLowerCase().contains(filter)
                    || issue.getOpenedBy().getName().toLowerCase().contains(filter)
                    || issue.getOpenedBy().getUsername().toLowerCase().contains(filter)
                    || issue.getAssignedTo().getName().toLowerCase().contains(filter)
                    || issue.getAssignedTo().getUsername().toLowerCase().contains(filter)){
                    issues.add(issue);
                }
            }
        }else
            issues = issueDAO.findAllIssues();



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
            if(user.isAdmin() || user.assignedIssue(issue) || user.isManagerForProject(issue.getProject()))
                jsonObject.put("editLink","/bts/project/"+issue.getProject().getId()+"/issues/"+issue.getId()+"/edit-issue");
            if(user.isAdmin() || user.isManagerForProject(issue.getProject()))
                jsonObject.put("deleteLink","/bts/project/"+issue.getProject().getId()+"/issues/"+issue.getId()+"/delete-issue");
            result.put(jsonObject);
        }
//        System.out.println(result);
        return result;
    }



}
