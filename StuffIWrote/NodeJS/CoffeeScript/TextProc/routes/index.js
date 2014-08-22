var express = require('express');
var router = express.Router();
var http = require('http');

/* GET home page. */
router.get('/', function(req, res) {
  res.render('index', { title: 'Express' });
});

router.get('/alice', function(req, res) {

  var url = 'http://www.gutenberg.org/cache/epub/11/pg11.txt';

  var send_book_to_client = function(text) {
    res.send(text);
  }

  http.get(url, function(res) {
    var body = '';

    res.on('data', function(chunk) {
      body += chunk;
    });

    res.on('end', function() {
      send_book_to_client(body);
    })

  });
});

module.exports = router;
