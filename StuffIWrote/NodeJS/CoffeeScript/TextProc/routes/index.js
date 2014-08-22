var express = require('express');
var router = express.Router();
var http = require('http');

var send_book_to_client = function(text) {
  console.log(text);
}

/* GET home page. */
router.get('/', function(req, res) {
  var url = 'http://www.gutenberg.org/cache/epub/11/pg11.txt';
  http.get(url, function(res) {
    var body = '';
    res.on('data', function(chunk){ body += chunk });
    res.on('end', function(){ send_book_to_client(body) })

    // TODO use AJAX to fire this back to the client
    // then have some client-side JS to do the functionality I want.

  });
  res.render('index', { title: 'Express' });
});

module.exports = router;
