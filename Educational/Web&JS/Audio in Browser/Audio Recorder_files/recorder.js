/*License (MIT)

    Copyright © 2013 Matt Diamond

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
    documentation files (the "Software"), to deal in the Software without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
    to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of
    the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
    THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
    CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
    DEALINGS IN THE SOFTWARE.

    Modified 4/6/2014 Ethan Petuchowski
*/

(function(window){

    var WORKER_PATH = 'js/recorderjs/recorderWorker.js';

    // "source" is the "inputPoint", i.e. the GainNode
    // We're not passing in a "cfg"
    var Recorder = function(source, cfg){
        var config = cfg || {};
        var bufferLen = config.bufferLen || 4096;

        // Retrieve the AudioContext graph object
        this.context = source.context;

        // Check if the AudioContext has the method "createScriptProcessor".
        // It renamed the old "createJavaScriptNode" method, but we must make
        // sure the new version it exists first.
        if (!this.context.createScriptProcessor) {
            this.node = this.context.createJavaScriptNode(bufferLen, 2, 2);
        } else {

            // Receives (buffersize, # input channels, # output channels).
            // Creates a "ScriptProcessorNode", an audio-processing module
            // whose "onaudioprocess" function gets called whenever the
            // "buffersize" frames have been filled (~20 times per second).
            this.node = this.context.createScriptProcessor(bufferLen, 2, 2);
        }

        // I'm not sure where this is coming from. The WORKER_PATH doesn't exist
        // in this directory, and the config is an empty object in this case.
        var worker = new Worker(config.workerPath || WORKER_PATH);

        // Initialize the worker by setting its sampleRate attribute
        worker.postMessage({
            command: 'init',
            config: {
                sampleRate: this.context.sampleRate
            }
        });
        var recording = false,
        currCallback;

        // Called whenever the audio data buffer fills up
        this.node.onaudioprocess = function(e){
            if (!recording) return;

            // The Worker has L & R arrays building up the entire audio file
            // We're just passing new buffers to be appended to them
            worker.postMessage({
                command: 'record',
                buffer: [

                    // Retrieve Float32Array of audio data for channels 0 and 1 (stereo)
                    e.inputBuffer.getChannelData(0),
                    e.inputBuffer.getChannelData(1)
                ]
            });
        }

        this.configure = function(cfg){
            for (var prop in cfg){
                if (cfg.hasOwnProperty(prop)){
                    config[prop] = cfg[prop];
                }
            }
        }

        this.record = function(){
            recording = true;
        }

        this.stop = function(){
            recording = false;
        }

        this.clear = function(){
            worker.postMessage({ command: 'clear' });
        }

        this.getBuffers = function(cb){
            currCallback = cb || config.callback;
            worker.postMessage({ command: 'getBuffers' })
        }

        this.exportWAV = function(cb, type){
            currCallback = cb || config.callback;
            type = type || config.type || 'audio/wav';
            if (!currCallback) throw new Error('Callback not set');
            worker.postMessage({
                command: 'exportWAV',
                type: type
            });
        }

        this.exportMonoWAV = function(cb, type){
            currCallback = cb || config.callback;
            type = type || config.type || 'audio/wav';
            if (!currCallback) throw new Error('Callback not set');
            worker.postMessage({
                command: 'exportMonoWAV',
                type: type
            });
        }

        worker.onmessage = function(e){
            var blob = e.data;
            currCallback(blob);
        }

        source.connect(this.node);
        // if the script node is not connected to an output
        // the "onaudioprocess" event is not triggered in chrome.
        this.node.connect(this.context.destination);
    };

    Recorder.setupDownload = function(blob, filename){
        var url = (window.URL || window.webkitURL).createObjectURL(blob);
        var link = document.getElementById("save");
        link.href = url;
        link.download = filename || 'output.wav';
    }

    window.Recorder = Recorder;

})(window);
