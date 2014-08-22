$('p').click(function(){
    alert('clicked!');
});
var aliceTextUrl = 'http://www.gutenberg.org/cache/epub/11/pg11.txt';

$('button').click(function(){
    console.log('clicked')
    $.get(aliceTextUrl, function (data) {
        var textNode = $('<p>').text(data);
        $('body').append(textNode);
    });
    var textNode = $('<p>').class('asdf').text('data');
    $('body').append(textNode);
});
