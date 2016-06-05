var views = require('co-views');

/* setup views mapping .html to the swig template engine */

/**
 * This file can be `require`d and then _called_ to render a view (i.e.
 * generate real HTML from a template) like so
 *
 * var render = require('./lib/render');
 * var renderedPage = yield render('htmlFileName', {templateVariable: 'value'});
 *
 * aside: __dirname refers to the currently executing file's directory,
 *         explicitly _not_ the originally-invoked file's directory.
 */
module.exports = views(__dirname + '/../views', {
    // map extension names to engine names
    // i.e. for `html` files, we'd like to use the engine 'swig'
    map: { html: 'swig' }
});
