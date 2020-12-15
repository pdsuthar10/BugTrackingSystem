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

function issuesFilterChange(){
    let valueNow = $("#filter").val();
    console.log(valueNow);
}