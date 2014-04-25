/**
 * 4/24/14
 * Ethan Petuchowski
 * Make it work here, then put it in the web app.
 */

function Game(size) {
    this.size = size;
    this.init();
}

Game.prototype.init = function () {
    this.scores = {};
    this.board = new Board(this);
    this.scoreboard = new Scoreboard(this);
    this.board.init(this.size);
    this.makeRandomCellRed();
    this.addPlayer('Computer');
    this.addPlayer('User');
};

Game.prototype.addPlayer = function (name) {
    this.scores[name] = 0;
    this.scoreboard.addPlayer(name);
};

Game.prototype.makeRed = function ($element) {
    $element
        .addClass('redone')
        .click(this.redEventListener.bind(this));
};

Game.prototype.clearRed = function () {
    var $redone = $('.redone').removeClass('redone');
    $('body').off('click', $redone, this.redEventListener);
};

Game.prototype.makeRandomCellRed = function () {
    this.makeRed(this.chooseRandomCell());
};

Game.prototype.redEventListener = function () {
    alert('NICE!');
    this.scores['User']++;
    this.scoreboard.render();
    // choose new red square
    this.clearRed();
    this.makeRandomCellRed();
};

Game.prototype.chooseRandomCell = function () {
    var i = Math.floor(Math.random()*this.size);
    var j = Math.floor(Math.random()*this.size);
    return $('#table-cell-'+i+'-'+j);
};

function Board(game) {
    this.game = game;
}

Board.prototype.init = function (size) {
    var $table = $('<table>');
    for (var i = 0; i < size; i++) {
        var $tableRow = $('<tr>')
            .attr('id', 'table-row-'+i)
            .addClass('table-row table-row-'+i);
        for (var j = 0; j < size; j++) {
            $tableRow.append(
                $('<td>')
                    .attr('id', 'table-cell-'+i+'-'+j)
                    .addClass('table-cell table-cell-row-'+i)
                    .text('mayhem'));
        }
        $table.append($tableRow);
    }
    $('#gameboard').append($table);
};

function Scoreboard(game) {
    this.game = game;
    this.scoreList = document.getElementById('scorelist');
}

Scoreboard.prototype.addPlayer = function (name) {
    var id = 'player-score-' + name;
    if (document.getElementById(id)) {
        alert('A player with name '+name+' already exists.');
        return false;
    }
    this.render();
//    this.scoreList.appendChild(li);
};

Scoreboard.prototype.render = function () {
    var scores = this.game.scores;
    var $scoreboard = $('#scoreboard');
    $scoreboard.empty();
    for (var player in scores) {
        $scoreboard.append(
            $('<div>')
                .addClass('panel panel-default')
                .append(
                    $('<h3>')
                        .addClass('panel-heading panel-title')
                        .text(player))
                .append(
                    $('<h4>')
                        .addClass('panel-body')
                        .text(scores[player])));
    }
};

var computerScore = 0;

var computerPlayer = setInterval(function () {
    myTimer();
});

function myTimer() {

}
var game = new Game(4);
