var keys = ["Rank", "Name", "Games Played", "Wins", "Draws", "Losses", "GF", "GA", "GD", "Points"];
var list = {"Gold League":[{"Games Played":0,"Points":0,"Losses":0,"Rank":1,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 11","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":2,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 12","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":3,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 13","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":4,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 14","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":5,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 15","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":6,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 16","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":7,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 17","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":8,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 18","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":9,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 19","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":10,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 20","GF":0}],"Blue League":[{"Games Played":0,"Points":0,"Losses":0,"Rank":1,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 1","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":2,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 2","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":3,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 3","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":4,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 4","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":5,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 5","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":6,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 6","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":7,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 7","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":8,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 8","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":9,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 9","GF":0},{"Games Played":0,"Points":0,"Losses":0,"Rank":10,"GA":0,"Draws":0,"GD":0,"Wins":0,"Name":"Team 10","GF":0}]};
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

for (let key in list)
{
    let table = document.querySelector("table");
    let data = Object.keys(list[key][0]);
    generateTableHead(table, data);
    generateTable(table, list[key]);
}


