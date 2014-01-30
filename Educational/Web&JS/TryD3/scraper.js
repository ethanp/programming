$(function(){

    var valsAsText = $(".value").map(function() {
        return $(this).text()
    }).get();

    // this turned out to be unnecessary
    var valsAsInts = valsAsText.map(function(txt) {
        return parseInt(txt, 10);
    });

    // "Let's Make a Bar Chart" [1]
    // [1] : http://bost.ocks.org/mike/bar
    // [2] : http://bost.ocks.org/mike/join
    // [3] : https://github.com/mbostock/d3/wiki/Selections#wiki-data
    $("button").click(function() {
        d3.select("#graph")                         // find the div to contain the chart
            .selectAll("div")                       // declare the elements you *want* to exist [2]
                .data(valsAsText)                   // use selection.data to join the data [3]
            .enter().append("div")                  // instantiate missing elements by *append*ing to the *enter* selection
                .style("width", function(d){return d*10+"px";})  // set width of each bar proportnl to its value
                .text(function(d) { return d; });   // set the bar's text label
    });
});
