let keys = ["Rank", "Name", "Games Played", "Wins", "Draws", "Losses", "GF", "GA", "GD", "Points"];
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
    for (let element of data) //each team set
    {
        let row = table.insertRow();
        for (let key of keys) //each data part of each team
        {
            let cell = row.insertCell();
            let text = document.createTextNode(element[key]);
            cell.appendChild(text);
        }
    }
}

for (let key in this.list)
{
    let table = document.querySelector("table");
    let data = Object.keys(this.list[key][0]);
    generateTableHead(table, data);
    generateTable(table, this.list[key]);
}


