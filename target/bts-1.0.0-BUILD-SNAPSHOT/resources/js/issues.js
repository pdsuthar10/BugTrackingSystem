function sendComment(issueId) {
    event.preventDefault();
    let comment = document.getElementById("comment");
    console.log(comment.value);

    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            addTableComment(JSON.parse(this.response).map)
            // let data = JSON.parse(this.responseText);
            // console.log(data)
            let closeButton = document.getElementById("closeModal");
            closeButton.click();
            document.getElementById("commentForm").reset();
        }
    };
    xhttp.open("POST", '/bts/issue/'+issueId+'/add-comment', true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send(comment.value);
}

function addTableComment(comment){
    console.log(comment);
    let table = document.getElementById("commentsTable");
    if(table == null)
    {
        console.log("table not created");
        let table = document.createElement("table");
        table.setAttribute("id","commentsTable");
        table.setAttribute("class", "table table-striped table-hover");
        let thead = document.createElement("thead");
        let trHead = document.createElement("tr");
        let headers = ["Comment ID", "Comment", "Commented By", "Created On"];
        headers.forEach( header => {
            let th = document.createElement("th");
            let text = document.createTextNode(header);
            th.appendChild(text);
            trHead.appendChild(th);
        })
        thead.appendChild(trHead);
        let tbody = document.createElement("tbody");
        table.appendChild(thead);
        table.appendChild(tbody);
        let container = document.getElementsByClassName("container").item(0);
        container.appendChild(table);
        let row = table.tBodies[0].insertRow(0);

        let cell1 = row.insertCell(0);
        let cell2 = row.insertCell(1);
        let cell3 = row.insertCell(2);
        let cell4 = row.insertCell(3);

        cell1.innerHTML = comment.id;
        cell2.innerHTML = comment.comment;
        cell3.innerHTML = comment.commentedBy;
        cell4.innerHTML = comment.createdOn;
    }
    else{
        console.log("already created");
        let row = table.tBodies[0].insertRow(0);

        let cell1 = row.insertCell(0);
        let cell2 = row.insertCell(1);
        let cell3 = row.insertCell(2);
        let cell4 = row.insertCell(3);

        cell1.innerHTML = comment.id;
        cell2.innerHTML = comment.comment;
        cell3.innerHTML = comment.commentedBy;
        cell4.innerHTML = comment.createdOn;
    }
}

function issuesFilterChange(username){
    event.preventDefault();
    let valueNow = $("#filter").val();
    let valuesPossible = [ "all", "opened", "assigned", "open", "closed", "allIssues" ]
    if(!valuesPossible.includes(valueNow)) return;
    sendRequest("POST",'/bts/api/user/'+username+'/issues',valueNow, populateIssues)

}

function searchChange(){
    event.preventDefault();
    let valueNow = $("#searchIssue").val();
    if(valueNow === "")
        valueNow = "all"
    sendRequest("POST", '/bts/api/user/issues', valueNow, populateIssues)
}

function issuesFilterChangeProject(project_id, user_id){
    event.preventDefault();
    let valueNow = $("#filter").val();
    let valuesPossible = [ "all", "open", "closed"]
    if(!valuesPossible.includes(valueNow)) return;
    sendRequest("POST",'/bts/api/user/'+user_id+'/project/'+project_id+'/issues',valueNow, populateIssues)
}

function populateIssues(data){
    data = data.myArrayList
    $("#issueList").empty();
    if(data == undefined || data == null || data.length == 0){
        let tbody = document.getElementById("issueList");
        let row = tbody.insertRow();
        let noresult = row.insertCell(0);
        noresult.innerHTML = "No issues found in this category!"
        noresult.setAttribute("colSpan", 12);
        noresult.setAttribute("align", "center");
        return
    }
    let tbody = document.getElementById("issueList");
    data.forEach( issueObject => {
        let issue = issueObject.map;
        let row = tbody.insertRow();
        let issueId = row.insertCell(0);
        issueId.innerHTML = issue.issueId;
        let projectId = row.insertCell(1);
        projectId.innerHTML = issue.projectId;
        let title = row.insertCell(2);
        title.innerHTML = issue.title;
        let description = row.insertCell(3);
        description.innerHTML = issue.description;
        let status = row.insertCell(4);
        status.innerHTML = issue.status;
        let issueType = row.insertCell(5);
        issueType.innerHTML = issue.issueType;
        let priority = row.insertCell(6);
        priority.innerHTML = issue.priority;
        let openedBy = row.insertCell(7);
        openedBy.innerHTML = issue.openedBy;
        let assignedTo = row.insertCell(8);
        assignedTo.innerHTML = issue.assignedTo;
        let createdOn = row.insertCell(9);
        createdOn.innerHTML = issue.createdOn;
        let modifiedOn = row.insertCell(10);
        modifiedOn.innerHTML = issue.modifiedOn;
        let action = row.insertCell(11);
        let viewLink = document.createElement("a");
        viewLink.setAttribute("href", issue.viewLink);
        viewLink.innerHTML = "View";
        action.append(viewLink);
        if(!(issue.editLink == undefined || issue.editLink == null || issue.editLink === "")){
            let text = document.createTextNode("\u00A0|\u00A0");
            action.append(text);
            let editLink = document.createElement("a");
            editLink.setAttribute("href", issue.editLink);
            editLink.innerHTML = "Edit";
            action.append(editLink);
        }

        if(!(issue.deleteLink == undefined || issue.deleteLink == null || issue.deleteLink === "")){
            let text = document.createTextNode("\u00A0|\u00A0");
            action.append(text);
            let deleteLink = document.createElement("a");
            deleteLink.setAttribute("href", issue.deleteLink);
            deleteLink.innerHTML = "Delete";
            action.append(deleteLink);
        }

    })


}

function sendRequest(method, url, dataToSend, callback){
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            callback(JSON.parse(this.response));
        }
    };

    xhttp.open(method, url, true);
    if(method === "POST") {
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send(dataToSend);
    }else
        xhttp.send();
}