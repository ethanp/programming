var gridSize = 10;
var gameOver = false;
function randomLoc() {
    var row = Math.floor(Math.random() * gridSize);
    var col = Math.floor( Math.random() * gridSize);
    return [row, col];
}
function placeFood() {
    var loc = [];
    while (true) {
        loc = randomLoc();
        if (!locInSnake(loc)) {
            break;
        }
    }
    getLoc(loc).addClass('food');
}
function locInSnake(loc) {
    for (var i=0; i<snakeLen; i++) {
        if (snakeLocs[i][0] == loc[0] &&
            snakeLocs[i][1] == loc[1])
        {
            return true;
        }
    }
    return false;
}
function createGrid() {
    for (var i=0; i<gridSize; i++) {
        $('#t').append('<tr>');
    }
    $('tr').each(function(idx) {
        this.id = "tr-"+idx;
        $this = $(this);
        for (var i=0; i<gridSize; i++) {
            $this.append('<td>');
        }
        $this.children().each(function(idx2) {
            this.id = 'td-row-'+idx+'-col-'+idx2;
        })
    });
}
function getJQCell(row, col) {
    return $('#td-row-'+row+'-col-'+col);
}
function getLoc(loc) {
    return getJQCell(loc[0], loc[1]);
}
function OOB(loc) {
    return loc[0] < 0 || loc[1] < 0 ||
           loc[0] >= gridSize || loc[1] >= gridSize;
}
function moveSnake() {
    if (gameOver) return;
    nextLoc = newLoc(direction, newestLoc());
    if (OOB(nextLoc)) {
        alert("you died.");
        gameOver = true;
    }
    $nextLoc = getLoc(nextLoc);
    if (locInSnake(nextLoc)) {
        alert("you died.");
        gameOver = true;
    }
    $nextLoc.addClass('redone');
    snakeLocs.push(nextLoc);
    if ($nextLoc.hasClass('food')) {
        snakeLen += 1;
        $nextLoc.removeClass('food');
        placeFood();
    } else {
        movedOut = snakeLocs.shift();
        getLoc(movedOut).removeClass('redone');
    }
}
function newestLoc() {
    return snakeLocs[snakeLocs.length-1];
}
function newLoc(dir, prevLast) {
    switch (dir) {
        case UP:    return [prevLast[0]-1, prevLast[1]];
        case DOWN:  return [prevLast[0]+1, prevLast[1]];
        case LEFT:  return [prevLast[0], prevLast[1]-1];
        case RIGHT: return [prevLast[0], prevLast[1]+1];
        default: alert("runtime error! invalid dir");
    }
}
$('body').keyup(function(e) {
    var upKey=38, downKey=40, leftKey=37, rightKey=39;
    if (e.keyCode == upKey    && direction != DOWN)  direction = UP;
    if (e.keyCode == downKey  && direction != UP)    direction = DOWN;
    if (e.keyCode == leftKey  && direction != RIGHT) direction = LEFT;
    if (e.keyCode == rightKey && direction != LEFT)  direction = RIGHT;
})
createGrid();
var snakeLen = 3;
var snakeLocs = [];
var UP=0, DOWN=1, LEFT=2, RIGHT=3;
function initSnake() {
    for (var i=0; i<snakeLen; i++) {
        snakeLocs.push([0,i]);
        getJQCell(0, i).addClass('redone');
    }
    placeFood();
}
initSnake();
var direction = RIGHT;
setInterval(moveSnake, 800);
