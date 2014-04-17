/**
 * Saved from URL: http://webaudiodemos.appspot.com/AudioRecorder/index.html
 *
 * Modified 4/6/2014 Ethan Petuchowski
 */

/**
 * We are drawing the lower image on the site, which is
 * a simple zoomed-out plot of the audiofile's waveform
 */

// <canvas @width, @height, @context>, @data is an array
function drawBuffer( width, height, context, data ) {

    // each chunk of data evenly separates the width
    // of the <canvas> into one pixel per chunk
    var step = Math.ceil( data.length / width );

    // scale the drawing height of the waveform by
    // the height of the <canvas>
    var amp = height / 2;

    context.fillStyle = "silver";

    // for each pixel across the width of the <canvas>
    for(var i=0; i < width; i++) {
        var min = 1.0;
        var max = -1.0;

        // find the min and max of the next chunk of data
        for (j=0; j<step; j++) {
            var datum = data[(i*step)+j];
            if (datum < min)
                min = datum;
            if (datum > max)
                max = datum;
        }

        // params are location (x, y, width, height)
        // x=[i,i+1] because we are scanning across the screen drawing the waveform drawing
        //           the width of one pixel at a time
        context.fillRect(i, (1 + min) * amp, 1, Math.max(1, (max - min) * amp));
    }
}
