/**
 * Ethan Petuchowski
 * 8/22/14
 * Parse book-text and obtain indexes by word and by line
 * And add interactivity to play with the indexes
 */

var lines, wordToLines = {};

var $textbox = $('#textbox')

var returnInTextbox = function(evt) {
  $('#line-list').remove();                // remove current list
  var value = $('#textbox').val();         // get user-request
  var idxs = wordToLines[value];           // find lines containing request
  if (idxs === undefined) return;          // if there were any lines
  $ol = $('<ol>').attr('id', 'line-list'); // add the list to the dom
  $.each(idxs, function(idx_no, idx) {
    $ol.append($('<li>').text(lines[idx]));
  });
  $('#lines').append($ol);
}

// install handler on return key
$textbox.keyup(function(e) {
    if(e.keyCode == 13) {
      returnInTextbox(e);
    }
});

var putBookOnScreen = function(text) {
  $('#datStuff').html(text);
}

// split, and remove empties
var cleanSplit = function(text, splitCond) {
  return text.split(splitCond).filter(function(e){ return e !== "" });
}

var breakIntoWords = function(text) {
  return text.split(/\W/);
}

$('#datStuff').text('LOADING...');
$.ajax('/alice', {
  success: function (data) {
    // putBookOnScreen(data);
    $('#datStuff').text('READY.')

    var i, l, wordsInLines = [];

    lines = cleanSplit(data, '\n');

    // create `wordsInLines`
    //    [ ["a","b"], ["c","d","e"] ]
    for (i = 0; i < lines.length; i++) {
      l = cleanSplit(lines[i], /\W/);
      wordsInLines.push(l);
    }

    // create `wordToLines`
    //    { "a": [1], "b": [1, 4, 12], "c": [2], ... }
    $.each(wordsInLines, function(line_no, wordsList) {
      $.each(wordsList, function(word_no, word) {
        word = word.toLowerCase();
        if (wordToLines[word] === undefined) {
          wordToLines[word] = [line_no];
        } else {
          wordToLines[word].push(line_no);
        }
      })
    });
  }
});
