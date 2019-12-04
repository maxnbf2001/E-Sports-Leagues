var keys = ["Rank", "Name", "Games Played", "Wins", "Draws", "Losses", "GF", "GA", "GD", "Points"];
var list = [{"Games Played":1,"Points":1,"Losses":0,"Rank":1,"GA":1,"Draws":1,"GD":0,"Wins":0,"Name":"Team 1","GF":1},{"Games Played":1,"Points":1,"Losses":0,"Rank":2,"GA":1,"Draws":1,"GD":0,"Wins":0,"Name":"Team 4","GF":1},{"Games Played":0,"Points":0,"Losses":0,"Rank":3,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 2","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":4,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 3","GF":0}];
function generateTableHead(table, data) {
    let thead = table.createTHead();
    let row = thead.insertRow();

    for (let key of keys) {
        let th = document.createElement("th");
        let text = document.createTextNode(key);

        th.appendChild(text);
        row.appendChild(th);
    }
}

function generateTable(table, data) {
    for (let element of data) {
        let row = table.insertRow();
        for (key of keys) {
            let cell = row.insertCell();
            let text = document.createTextNode(element[key]);
            cell.appendChild(text);
        }
    }
}


let table = document.querySelector("table");
let data = Object.keys(list[0]);
generateTableHead(table, data);
generateTable(table, list);