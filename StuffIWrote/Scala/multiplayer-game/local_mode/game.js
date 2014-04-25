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
    var self = this;
    this.scores = {};
    this.board = new Board(this);
    this.scoreboard = new Scoreboard(this);
    this.board.init(this.size);
    this.makeRandomCellRed();
    this.addPlayer('Computer');
    this.addPlayer('User');
    setInterval(function () {
        self.updateScore('Computer', 1)
    }, 2000);
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
    $('.redone').removeClass('redone').unbind('click');
};

Game.prototype.makeRandomCellRed = function () {
    this.makeRed(this.chooseRandomCell());
};

Game.prototype.redEventListener = function () {
    alert('NICE!');
    this.updateScore('User', 1);
    // choose new red square
    this.clearRed();
    this.makeRandomCellRed();
};

Game.prototype.updateScore = function (name, diff) {
    this.scores[name] += diff;
    this.scoreboard.render();
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
}

Scoreboard.prototype.addPlayer = function (name) {
    var id = 'player-score-' + name;
    if (document.getElementById(id)) {
        alert('A player with name '+name+' already exists.');
        return false;
    }
    this.render();
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

var game = new Game(4);
