/**
 * Created by Ethan Petuchowski on 4/16/14.
 */

(function () {
    var body = document.getElementById("body");
    var table = document.createElement("table");
    var size = 4;
    for (var i = 0; i < size; i++) {
        var tableRow = document.createElement("tr");
        for (var j = 0; j < size; j++) {
            var tableCell = document.createElement("td");
            tableCell.textContent = "mayhem";
            tableRow.appendChild(tableCell);
        }
        table.appendChild(tableRow);
    }
    body.appendChild(table);
})();