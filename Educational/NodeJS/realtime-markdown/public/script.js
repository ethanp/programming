window.onload = function() {
    // showdown is a Markdown->HTML renderer that can be used client or
    // server-side. Here we use it client side which is made possible because
    // we loaded it via cdn-link in a script-tag in the main html.body.
    var converter = new showdown.Converter();
    var pad = document.getElementById('pad');
    var markdownArea = document.getElementById('markdown');

    var previousMarkdownValue;

    var convertTextAreaToMarkdown = function() {
        var markdownText = pad.value;
        html = converter.makeHtml(markdownText);
        markdownArea.innerHTML = html;
    };

    var didChangeOccur = function() {
        return previousMarkdownValue != pad.value;
    };

    // sharejs does NOT automatically load other users' changes until
    // 'this' user modifies their textarea. We want it to load other users'
    // changes every (currently, 1 second) even if 'this' user is busy doing
    // their nails.
    setInterval(function() {
        if (didChangeOccur()) {
            convertTextAreaToMarkdown();
        }
    }, 1000);

    // The DOM `input` event is fired synchronously when the value of an
    // <input> or <textarea> element is CHANGED
    pad.addEventListener('input', convertTextAreaToMarkdown);

    // open sharejs connection for collaborating at the path name of the
    // current URL, e.g. "/thepath/forthepage.html". The replace() is bc
    // sharejs doesn't seem to allow "/" in document names.
    sharejs.open(document.location.pathname.replace('/', '_'), 'text', function(error, doc) {
    // sharejs.open('home', 'text', function(error, doc) {
        // tell it which textarea we want to keep in-sync across users
        if (error) { console.error(); }
        doc.attach_textarea(pad);
        convertTextAreaToMarkdown();
    });
};
