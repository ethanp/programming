Navigator
---------

[W3 Schools](http://www.w3schools.com/js/js_window_navigator.asp)

* The `window.navigator` object contains information about the visitor's browser.
* The `window` prefix is not required.

E.g. (from `http://webaudiodemos.appspot.com/AudioRecorder/index.html`, `./Audio Recorder_files/main.js`, function initAudio, [currently] lines 162-173)

    if (!navigator.getUserMedia)
        navigator.getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia;

Using their example, you can get all sorts of other possibly-useful stuff as well:

    Browser Code-Name:   navigator.appCodeName
    Browser Name:        navigator.appName
    Browser Version:     navigator.appVersion
    Cookies Enabled:     navigator.cookieEnabled
    Platform:            navigator.platform
    User-agent header:   navigator.userAgent
    User-agent language: navigator.systemLanguage

