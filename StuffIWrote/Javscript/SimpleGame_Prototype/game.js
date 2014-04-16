/**
 * Created by Ethan Petuchowski on 4/16/14.
 */

(function () {

    var body = document.getElementById('body');
    var table = document.createElement('table');
    var size = 4;
    for (var i = 0; i < size; i++) {
        var tableRow = document.createElement('tr');
        tableRow.id = 'table-row-'+i;
        tableRow.className = 'table-row table-row-'+i;
        for (var j = 0; j < size; j++) {
            var tableCell = document.createElement('td');
            tableCell.textContent = "mayhem";
            tableRow.appendChild(tableCell);
            tableCell.id = 'table-cell-'+i+'-'+j;
            tableCell.className = 'table-cell table-cell-row-'+i;
        }
        table.appendChild(tableRow);
    }
    body.appendChild(table);
    var scoreList = document.createElement('ul');
    scoreList.id = 'score-list';
    body.appendChild(scoreList);
    makeRandomCellRed();
    addPlayer('Computer');
    addPlayer('User');

    function makeRed(element) {
        element.className += ' redone';
        element.addEventListener('click', redEventListener)
    }

    function redEventListener() {
        alert('NICE!');
        makeRedNotRed();
        makeRandomCellRed();
    }

    function getRedSquare() {
        return document.querySelectorAll('.redone')[0];
    }

    function makeRedNotRed() {
        var redSquare = getRedSquare();
        redSquare.removeEventListener('click', redEventListener);
        redSquare.className = redSquare.className.replace(/\ ?redone/, '');
    }

    function chooseRandomCell() {
        var i = Math.floor(Math.random()*size);
        var j = Math.floor(Math.random()*size);
        return document.getElementById('table-cell-'+i+'-'+j);
    }

    function makeRandomCellRed() {
        makeRed(chooseRandomCell());
    }

    function addPlayer(name) {
        var id = 'player-score-' + name;
        if (document.getElementById(id)) {
            alert('A player with name '+name+' already exists.');
            return false;
        }
        var li = document.createElement('li');
        li.textContent = name+': 0';
        li.id = id;
        li.className = 'play-score '+name;
        scoreList.appendChild(li);
        return true;
    }
    var computerScore = 0;

    var computerPlayer = setInterval(function(){
        myTimer();
    });

    function myTimer() {

    }

})();
