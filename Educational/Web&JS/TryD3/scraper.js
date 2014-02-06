$(function(){

    var valsAsText = $(".value").map(function() {
        return $(this).text()
    }).get();

    // this turned out to be unnecessary
    var valsAsInts = valsAsText.map(function(txt) {
        return parseInt(txt, 10);
    });

    // "Let's Make a Bar Chart" [1]
    // The indentation is meant to reveal the composition of the selection at hand [2]
    // [1] : http://bost.ocks.org/mike/bar
    // [2] : http://bost.ocks.org/mike/d3/workshop/#34
    // [3] : http://bost.ocks.org/mike/join
    // [4] : https://github.com/mbostock/d3/wiki/Selections#wiki-data
    // [5] : http://mbostock.github.io/d3/tutorial/circle.html
    $("button").click(function() {
        d3.select("#graph")             // select element to contain the chart; like $("#graph") in jQuery
            .selectAll("div")           // declare the (as-yet non-existant) elements to which we will bind the data [3]
                .data(valsAsText)       // use selection.data to join the "div" element-class to each data-point [4]
            .enter().append("div")      // append divs to parent node for-each data-point not bound to an available div [5]
                .style("width", function(d){ return d * 10 + "px"; })  // set width of each bar proportnl to its value
                .text(function(d) { return d; });   // set the bar's text label to be its data-point's value
    });                                 // "you’ll probably want to reselect after adding or removing elements" [5]
});
