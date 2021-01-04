package com.info6250.bts.controller;

import com.info6250.bts.dao.*;
import com.info6250.bts.pojo.*;
import com.info6250.bts.validator.IssueValidator;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.*;

@Controller
public class IssueController {

    @Autowired
    IssueValidator issueValidator;

    @GetMapping("/user/issues")
    public String issuesAssigned(IssueDAO issueDAO, Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        List<Issue> issues = issueDAO.findIssuesAssignedOfUser(user.getUsername());
        model.addAttribute("issues", issues);

        return "issues-assigned";
    }

    @GetMapping("/project/{project_id}/issues/create-issue")
    public String createIssue(@PathVariable(name = "project_id") String project_id,
                              HttpSession session, ProjectDAO projectDao, UserDAO userDAO,
                              Model model){

        Project project = projectDao.findProjectById(Integer.parseInt(project_id));
        if(project.getTargetEndDate().before(new Date())){
            model.addAttribute("projectEnded", "The project has ended. Cannot report an issue");
            model.addAttribute("manager", project.getManager());
            model.addAttribute("project", project);
            model.addAttribute("developers", project.getDevelopers());
            model.addAttribute("issues", project.getIssues());
            model.addAttribute("unassignedDevelopers", userDAO.findUnassignedUsersProject(project));
            return "project-details";
        }

        User user = (User) session.getAttribute("user");

        List<User> developers = new ArrayList<User>();
        List<String> issueTypes = new ArrayList<>(Arrays.asList("Bug", "Task", "Error"));
        model.addAttribute("developers", project.getDevelopers());
        model.addAttribute("manager", project.getManager());
        model.addAttribute("project", project);
        model.addAttribute("issueTypes", issueTypes);
        model.addAttribute("issue", new Issue());
        return "create-issue";
    }

    @PostMapping("/project/{project_id}/issues/submit-issue")
    public String submitIssue(@PathVariable(name = "project_id") String project_id, HttpServletRequest request,
                              @ModelAttribute("issue") @Valid Issue issue,BindingResult result, SessionStatus sessionStatus,
                              HttpSession session, ProjectDAO projectDAO, PriorityDAO priorityDAO,
                              StatusDAO statusDAO, IssueDAO issueDAO, UserDAO userDAO, Model model){

        Project project = projectDAO.findProjectById(Integer.parseInt(project_id));
        Map<String, String> error = new HashMap<>();

        issueValidator.validate(issue, result);
        if(result.hasErrors()){
            List<String> issueTypes = new ArrayList<>(Arrays.asList("Bug", "Task", "Error"));
            model.addAttribute("developers", project.getDevelopers());
            model.addAttribute("manager", project.getManager());
            model.addAttribute("project", project);
            model.addAttribute("issueTypes", issueTypes);
            model.addAttribute("issue", new Issue());
//            model.addAttribute("error", error);
            return "create-issue";
        }
        sessionStatus.setComplete();

        String priorityValue = request.getParameter("priorityIssue");
        if(priorityValue == null || priorityValue.trim().equals(""))
            error.put("priority", "Please select priority!");
        String assignToValue = request.getParameter("assignTo");
        if(assignToValue == null || assignToValue.trim().equals(""))
            error.put("assignTo", "Please select a developer to assign to");
        if(error.size() > 0){
            List<String> issueTypes = new ArrayList<>(Arrays.asList("Bug", "Task", "Error"));
            model.addAttribute("developers", project.getDevelopers());
            model.addAttribute("manager", project.getManager());
            model.addAttribute("project", project);
            model.addAttribute("issueTypes", issueTypes);
            model.addAttribute("error", error);
            model.addAttribute("issue", new Issue());
            return "create-issue";
        }
        Priority priority = priorityDAO.findByName(request.getParameter("priorityIssue"));
        if(priority == null)
            error.put("priority", "Invalid Value");
        Status status = statusDAO.findByName("open");
        User assignTo = userDAO.findUserByUsername(request.getParameter("assignTo"));
        if(assignTo == null)
            error.put("assignTo", "Invalid Value");
        User openedBy = (User) session.getAttribute("user");

        if(error.size() > 0){
            List<String> issueTypes = new ArrayList<>(Arrays.asList("Bug", "Task", "Error"));
            model.addAttribute("developers", project.getDevelopers());
            model.addAttribute("manager", project.getManager());
            model.addAttribute("project", project);
            model.addAttribute("issueTypes", issueTypes);
            model.addAttribute("issue", new Issue());
            return "create-issue";
        }

        issueDAO.addIssue(issue.getTitle(),issue.getDescription(),
                priority,status,issue.getIssueType(), assignTo, project,
                openedBy);
        Email email = new HtmlEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("bts.info6250", "msiscsye2020?"));
        email.setSSLOnConnect(true);
        try {
            email.setFrom("bts.info6250@gmail.com");
            email.setSubject("Bug-Tracker :: Assigned an Issue");
            email.setMsg("<h1>Issue Details</h1>" + "<br>" +
                    "<p><b>A new issue was assigned to you recently. The following are the details:</b></p>"+
                    "<h3>Issue Title: " +request.getParameter("title")+ "</h3>" + "<br>" +
                    "<h3>Description: "+request.getParameter("description")+ "</h3>" +
                    "<h4>Priority: " + priority.getName() + "</h4>" +
                    "<h4>Status: " +status.getName()+ "</h4>" +
                    "<h4>Issue Type: "+ request.getParameter("issueType")+ "</h4>" +
                    "<h4>Associated Project ID: " + project.getId() +"</h4>"+ "<br>" +
                    "<h4>Project Manager: "+ project.getManager().getUsername() + "</h4>" +
                    "<h4>Opened By: "+ openedBy.getName() +" ("+ openedBy.getUsername() +") "+ "</h4>");
            email.addTo(assignTo.getUsername());
            email.send();

        } catch (EmailException e) {

            e.printStackTrace();
        }

        return "redirect:/admin";
    }

    @GetMapping("/project/{project_id}/issues/{issue_id}/details")
    public String issueDetails(@PathVariable(name = "project_id") String project_id,
                               @PathVariable(name = "issue_id") String issue_id,
                               ProjectDAO projectDAO, IssueDAO issueDAO, HttpSession session,
                               Model model){

        Issue issue = issueDAO.findById(Long.parseLong(issue_id));
        model.addAttribute("issue", issue);
        return "issue-details";
    }

    @PostMapping("/issues/{issue_id}/resolve")
    public String resolveIssue(@PathVariable(name = "issue_id") String issue_id,
                               HttpServletRequest request, HttpSession session, IssueDAO issueDAO,
                               StatusDAO statusDAO, Model model){

        Issue issue = issueDAO.findById(Long.parseLong(issue_id));
        if(issue == null)
            return "redirect:/user/dashboard";
        String resolutionSummary = request.getParameter("resolutionSummary");
        if(resolutionSummary == null || resolutionSummary.trim().equals("")) return "redirect:/user/dashboard";

        User user = (User) session.getAttribute("user");
        Status status = statusDAO.findByName("closed");
        issueDAO.resolveIssue(resolutionSummary,issue,user,status);

        Email email = new HtmlEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("bts.info6250", "msiscsye2020?"));
        email.setSSLOnConnect(true);
        try {
            email.setFrom("bts.info6250@gmail.com");
            email.setSubject("Bug-Tracker :: Issue resolved");
            email.setMsg("Hello "+issue.getOpenedBy().getName()+","+"\n"+
                    "Your issue was resolved by "+issue.getClosedBy().getUsername()+"\n"+
                    "ISSUE DETAILS" + "\n" +
                    "Issue Title: " +issue.getTitle() + "\n" +
                    "Description: "+issue.getDescription()+ "\n" +
                    "Priority: " + issue.getPriority().getName() + "\n" +
                    "Status: " +issue.getStatus().getName()+ "\n" +
                    "Issue Type: "+ issue.getIssueType() + "\n" +
                    "Associated Project ID: " + issue.getProject().getId() +"\n"+
                    "Project Manager: "+ issue.getProject().getManager().getUsername() + "\n" +
                    "Closed By: "+ issue.getClosedBy().getName() + " (" + issue.getClosedBy().getUsername()+")" + "\n"+
                    "Closed On: "+issue.getClosedOn().toLocaleString());
            email.addTo(issue.getOpenedBy().getUsername());
            email.send();
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("issue", issue);
        return "issue-details";

    }

    @GetMapping("/project/{project_id}/issues/{issue_id}/edit-issue")
    public String editIssue(@PathVariable(name = "issue_id") String issue_id,
                            @PathVariable(name = "project_id") String project_id,
                            IssueDAO issueDAO, ProjectDAO projectDAO, Model model, PriorityDAO priorityDAO,
                            HttpSession session){
        Issue issue = issueDAO.findById(Long.parseLong(issue_id));
        Project project = projectDAO.findProjectById(Integer.parseInt(project_id));
        List<String> priorities = priorityDAO.findAllPriorities();

        String[] issueTypes = {"Bug", "Error", "Task"};

        model.addAttribute("issue", issue);
        model.addAttribute("project", project);
        model.addAttribute("priorities", priorities);
        model.addAttribute("issueTypes", issueTypes);
        return "edit-issue";
    }

    @PostMapping("/project/{project_id}/issues/{issue_id}/submit-change")
    public String submitChange(@PathVariable(name = "project_id") String project_id,
                               @PathVariable(name = "issue_id") String issue_id,
                               HttpServletRequest request, IssueDAO issueDAO,
                               ProjectDAO projectDAO, Model model, PriorityDAO priorityDAO,
                               UserDAO userDAO){

        Issue issue = issueDAO.findById(Long.parseLong(issue_id));
        Project project = projectDAO.findProjectById(Integer.parseInt(project_id));

        Map<String, String> errors = new HashMap<>();
        String description = request.getParameter("description");
        if(description == null || description.trim().equals(""))
            errors.put("description","Description cannot be empty");
        String priorityValue = request.getParameter("priority");
        if(priorityValue == null || priorityValue.trim().equals(""))
            errors.put("priority","Please select a priority");
        String issueType = request.getParameter("issueType");
        if(issueType == null || issueType.trim().equals(""))
            errors.put("issueType","Please select an issue type");
        String assignTo = request.getParameter("assignTo");
        if(assignTo == null || assignTo.trim().equals(""))
            errors.put("assignTo","Please select a developer");

        Priority priority = priorityDAO.findByName(priorityValue);
        if(priority == null) errors.put("priority", "Invalid Value");
        List<String> issueTypes = new ArrayList<>(Arrays.asList("Bug","Error", "Task"));
        if(!issueTypes.contains(issueType)) errors.put("issueType", "Invalid Value");
        User user = userDAO.findUserByUsername(assignTo);
        if(user == null) errors.put("assignTo", "Invalid Value");
        if(errors.size()>0){
            model.addAttribute("issue", issue);
            model.addAttribute("project", project);
            model.addAttribute("priorities", priorityDAO.findAllPriorities());
            model.addAttribute("issueTypes", issueTypes);
            model.addAttribute("error", errors);
            return "edit-issue";
        }

        if(!issue.getDescription().equals(description))
            issue.setDescription(description);

        if(!issue.getIssueType().equals(issueType))
            issue.setIssueType(issueType);

        if(!issue.getPriority().getName().equals(priority.getName()));
            issue.setPriority(priority);

        if(!issue.getAssignedTo().getUsername().equals(user.getUsername())) {
            issue.setAssignedTo(user);
            Email email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator("bts.info6250", "msiscsye2020?"));
            email.setSSLOnConnect(true);
            try {
                email.setFrom("bts.info6250@gmail.com");
                email.setSubject("Bug-Tracker :: Assigned an Issue");
                email.setMsg("<h1>Issue Details</h1>" + "<br>" +
                        "<p><b>A new issue was assigned to you recently. The following are the details:</b></p>"+
                        "<h3>Issue Title: " +issue.getTitle()+ "</h3>" + "<br>" +
                        "<h3>Description: "+issue.getDescription()+ "</h3>" +
                        "<h4>Priority: " + issue.getPriority().getName() + "</h4>" +
                        "<h4>Status: " +issue.getStatus().getName()+ "</h4>" +
                        "<h4>Issue Type: "+ issue.getIssueType() + "</h4>" +
                        "<h4>Associated Project ID: " + issue.getProject().getId() +"</h4>"+ "<br>" +
                        "<h4>Project Manager: "+ issue.getProject().getManager().getUsername() + "</h4>" +
                        "<h4>Opened By: "+ issue.getOpenedBy().getName() +" ("+ issue.getOpenedBy().getUsername() +") "+ "</h4>");
                email.addTo(issue.getAssignedTo().getUsername());
                email.send();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        issueDAO.updateIssue(issue);
        return "redirect:/user/dashboard";

    }

    @PostMapping(value = "/user/{user_id}/issues")
    public String getMyIssues(@PathVariable(name = "user_id") String user_id,
                                 UserDAO userDAO, HttpServletRequest request, Model model){
        String filter = request.getParameter("filter");
        User user = userDAO.findById(UUID.fromString(user_id));
        if(user == null || filter == null || filter.trim().equals("")) return "not-found";

        List<Issue> issues;
        if(filter.equals("assigned"))
            issues = user.getAssignedIssues();
        else if(filter.equals("opened"))
            issues = user.getOpenedIssues();
        else if(filter.equals("all")) {
            Set<Issue> temp = user.getAllIssues();
            issues = new ArrayList<>(temp);
        }
        else
            return "not-found";

        model.addAttribute("issues", issues);
        model.addAttribute("filter", filter);
        return "issues-assigned";
    }

    @GetMapping("/project/{project_id}/issues/{issue_id}/delete-issue")
    public String deleteIssue(@PathVariable(name = "project_id") String project_id,
                              @PathVariable(name = "issue_id") String issue_id, ProjectDAO projectDAO,
                              IssueDAO issueDAO, IssueCommentDAO issueCommentDAO){

        Issue issue = issueDAO.findById(Long.parseLong(issue_id));

        issueCommentDAO.deleteCommentsIssue(issue.getId());
//        issue.setComments(null);
        issue = issueDAO.findById(Integer.parseInt(issue_id));
        issueDAO.deleteIssue(issue, issueCommentDAO);
        return "redirect:/user/dashboard";
    }
}
