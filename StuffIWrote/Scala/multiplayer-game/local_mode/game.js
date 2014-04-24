/**
 * Created by Ethan Petuchowski on 4/24/14.
 * Make it work here, then put it in the web app.
 */


function Game(size) {
    this.size = size;
    this.init(size);
}

Game.prototype.init = function (size) {
    var boardDiv = document.getElementById('gameboard');
    var table = document.createElement('table');
    this.scores = {};
    for (var i = 0; i < size; i++) {
        var tableRow = document.createElement('tr');
        tableRow.id = 'table-row-' + i;
        tableRow.className = 'table-row table-row-' + i;
        for (var j = 0; j < size; j++) {
            var tableCell = document.createElement('td');
            tableCell.textContent = "mayhem";
            tableRow.appendChild(tableCell);
            tableCell.id = 'table-cell-' + i + '-' + j;
            tableCell.className = 'table-cell table-cell-row-' + i;
        }
        table.appendChild(tableRow);
    }
    boardDiv.appendChild(table);
    var scoreList = document.getElementById('scorelist');
    this.makeRandomCellRed();
    this.addPlayer('Computer');
    this.addPlayer('User');
};

Game.prototype.addPlayer = function (name) {
    if (document.getElementById(id)) {
        alert('A player with name '+name+' already exists.');
        return false;
    }
    var id = 'player-score-' + name;
    this.scores[name] = 0;
    var li = document.createElement('li');
    li.textContent = name+': 0';
    li.id = id;
    li.className = 'play-score '+name;
//    this.scoreList.appendChild(li);
};

Game.prototype.makeRed = function (element) {
    element.className += ' redone';

    // TODO use bind() so `this` in the event listener is the Game rather than the <td> table-cell
    element.addEventListener('click', this.redEventListener);
};

Game.prototype.makeRedNotRed = function () {
    var redSquare = this.getRedSquare();
    redSquare.removeEventListener('click', this.redEventListener);
    redSquare.className = redSquare.className.replace(/\ ?redone/, '');
};

Game.prototype.makeRandomCellRed = function () {
    this.makeRed(this.chooseRandomCell());
};

Game.prototype.redEventListener = function () {
    alert('NICE!');
    this.makeRedNotRed();
    this.makeRandomCellRed();
};

Game.prototype.getRedSquare = function () {
    return document.querySelectorAll('.redone')[0];
};

Game.prototype.chooseRandomCell = function () {
    var i = Math.floor(Math.random()*this.size);
    var j = Math.floor(Math.random()*this.size);
    return document.getElementById('table-cell-'+i+'-'+j);
};

Game.prototype.redrawScoreboard = function () {

};

var computerScore = 0;

var computerPlayer = setInterval(function(){
    myTimer();
});

function myTimer() {

}
var game = new Game(4);
