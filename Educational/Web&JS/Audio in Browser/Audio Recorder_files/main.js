/* Copyright 2013 Chris Wilson

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Modified and annotated 4/6/2014 by Ethan Petuchowski
 */

window.AudioContext = window.AudioContext || window.webkitAudioContext;

// https://developer.mozilla.org/en-US/docs/Web/API/AudioContext
// "This is an experimental technology"
// "This technology's specification has not stabilized"
//
// http://www.w3.org/TR/webaudio/
// "This specification describes a high-level JavaScript API for processing
// and synthesizing audio in web applications. The primary paradigm is of an
// audio routing graph, where a number of AudioNode objects are connected
// together to define the overall audio rendering. The actual processing will
// primarily take place in the underlying implementation (typically optimized
// Assembly / C / C++ code), but direct JavaScript processing and synthesis is
// also supported."
//
// See more notes in my "Web Programming Notes.md"
var audioContext = new AudioContext();

var audioInput = null,
    realAudioInput = null,
    inputPoint = null,
    audioRecorder = null;

var rafID = null;

var analyserContext = null;

var canvasWidth, canvasHeight;

var recIndex = 0;

// TODO this never gets called! but this is what I need to call to save the audio...
function saveAudio() {
    audioRecorder.exportWAV(doneEncoding);
    // could get mono instead by saying
    // audioRecorder.exportMonoWAV( doneEncoding );
}

function gotBuffers(buffers) {

    // The lower canvas box, displaying the recorded wave-form
    var canvas = document.getElementById("wavedisplay");

    // render the lower image (zoomed-out image of the waveform)
    drawBuffer(canvas.width, canvas.height, canvas.getContext('2d'), buffers[0]);

    // Enable the HDD button to download the audiofile
    audioRecorder.exportWAV(doneEncoding);
}

function doneEncoding(blob) {
    // Params: (audio data as "Blob", desired filename)
    // Enable's HDD button to download audiofile by setting its attributes
    Recorder.setupDownload(blob, "myRecording" + (recIndex < 10 ? "0":"")+recIndex+".wav");
    recIndex++;
}

// Called onClick of the Record button
// "e" is the "img HTML element" that you click on
function toggleRecording(e) {
    console.log(e);
    if (e.classList.contains("recording")) {
        // stop recording
        audioRecorder.stop();
        e.classList.remove("recording");
        audioRecorder.getBuffers(gotBuffers);
    } else {
        // start recording
        if (!audioRecorder) return;
        e.classList.add("recording");

        // this is the Recorder() from "recorder.js"
        //
        audioRecorder.clear();
        audioRecorder.record();
    }
}

function convertToMono(input) {
    var splitter = audioContext.createChannelSplitter(2);
    var merger = audioContext.createChannelMerger(2);

    input.connect(splitter);
    splitter.connect(merger, 0, 0);
    splitter.connect(merger, 0, 1);
    return merger;
}

function cancelAnalyserUpdates() {
    window.cancelAnimationFrame(rafID);
    rafID = null;
}

// Called after gotStream() below,
// which creates a silenced AudioContext graph.
// Also calls itself in callback from window.requestAnimationFrame() at the
// bottom of this method.
function updateAnalysers(time) {
    if (!analyserContext) {
        var canvas = document.getElementById("analyser");
        canvasWidth = canvas.width;
        canvasHeight = canvas.height;

        // Returns an object that provides methods and properties for drawing
        // on the canvas.
        analyserContext = canvas.getContext('2d');
    }

    /** QUESTION:
     *    Why is this block of code surrounded by its own braces?
     */

    // analyzer draw code here
    {
        // Configure some FFT display layout
        var SPACING = 3;        // each FFT bar starts 3 pixels apart
        var BAR_WIDTH = 1;      // each FFT bar is 1 pixel wide (so there's 2 px btn them)
        var numBars = Math.round(canvasWidth / SPACING);

        // Acquire the data from the AudioContext's Analyser FFT node
        var freqByteData = new Uint8Array(analyserNode.frequencyBinCount);
        analyserNode.getByteFrequencyData(freqByteData);

        // Clear the whole thing
        analyserContext.clearRect(0, 0, canvasWidth, canvasHeight);

        // Fill it with an orangey color
        analyserContext.fillStyle = '#F6D565';

        // When drawing a line, its ends shall be round
        analyserContext.lineCap = 'round';

        // Figure the number of FFT bins per bar-chart bar
        var multiplier = analyserNode.frequencyBinCount / numBars;

        // Draw rectangle for each frequency bin.
        for (var i = 0; i < numBars; ++i) {

            var magnitude = 0;
            var offset = Math.floor(i * multiplier);

            // Average the bar over its constituent bins
            for (var j = 0; j < multiplier; j++)
                magnitude += freqByteData[offset + j];
            magnitude = magnitude / multiplier;

            // Set color based on sliding around the hue value by using
            // Hue-Saturation-Lightness based coloring
            analyserContext.fillStyle = "hsl( " + Math.round((i * 360) / numBars) + ", 100%, 50%)";

            // Draw a filled rectangle with (x,y, xOff, yOff) coordinates
            analyserContext.fillRect(i * SPACING, canvasHeight, BAR_WIDTH, -magnitude);
        }
    }

    // "Note: Your callback routine must itself call requestAnimationFrame()
    //  if you want to animate another frame at the next repaint."
    rafID = window.requestAnimationFrame(updateAnalysers);
}

function toggleMono() {
    if (audioInput != realAudioInput) {
        audioInput.disconnect();
        realAudioInput.disconnect();
        audioInput = realAudioInput;
    } else {
        realAudioInput.disconnect();
        audioInput = convertToMono(realAudioInput);
    }

    audioInput.connect(inputPoint);
}


// successCallback from requesting audio in initAudio->getUserMedia() below
function gotStream(stream) {

    // Creates a GainNode in the AudioContext graph
    // See notes in "Web Programming Notes.md"
    inputPoint = audioContext.createGain();

    // "Create a MediaStreamAudioSourceNode associated with a WebRTC
    // MediaStream representing an audio stream, that may come from the local
    // computer microphone or other sources."
    realAudioInput = audioContext.createMediaStreamSource(stream);
    audioInput = realAudioInput;

    // Connect the microphone's MediaStreamAudioSourceNode stream
    // to the GainNode's stream
    audioInput.connect(inputPoint);

    //    audioInput = convertToMono( input );

    // Create an AnalyserNode
    // a node able to provide real-time frequency and time-domain
    // analysis information
    analyserNode = audioContext.createAnalyser();

    // Represents the size of the Fast Fourier Transform to be used to
    // determine the frequency domain. 2048 is both the default and the
    // biggest allowed value
    analyserNode.fftSize = 2048;

    // Connect the GainNode into the FFT node
    inputPoint.connect(analyserNode);

    // Use Matt Diamond's "recorder.js" (also in this dir)
    // https://github.com/mattdiamond/Recorderjs
    // to create a recorder object that will buffer the raw audio.
    // We are not passing in any (optional) configuration settings.
    audioRecorder = new Recorder(inputPoint);

    // Create another GainNode to silence the graph's output
    zeroGain = audioContext.createGain();
    zeroGain.gain.value = 0.0;
    inputPoint.connect(zeroGain);
    zeroGain.connect(audioContext.destination);   // plug it into the final
    // destination of all audio
    // in the context
    updateAnalysers();
}

// on page load, request access to the user's audio-stream,
// in a browser-generic manner
function initAudio() {
    var n = navigator;  // with luck, adding this didn't break everything

    // Prompts the user for permission to use a media device such as a camera or microphone.
    // If the user provides permission, the successCallback is invoked on the calling
    // application with a LocalMediaStream object as its argument.
    n.getUserMedia = n.getUserMedia || n.webkitGetUserMedia || n.mozGetUserMedia; // set it to whichever version exists

    // Cancels an animation frame request previously scheduled through a call to window.requestAnimationFrame().
    n.cancelAnimationFrame = n.cancelAnimationFrame || n.webkitCancelAnimationFrame || n.mozCancelAnimationFrame;

    // Requests that the browser call a specified function to update an animation before the next repaint.
    // The method takes as an argument a callback to be invoked before the repaint.
    n.requestAnimationFrame = n.requestAnimationFrame || n.webkitRequestAnimationFrame || n.mozRequestAnimationFrame;

    // Parameters: ( constraints, successCallback, errorCallback );
    n.getUserMedia({audio: true}, gotStream, function (e) {
        alert('Error getting audio');
        console.log(e);
    });
}

// on page-load, call initAudio
window.addEventListener('load', initAudio);
