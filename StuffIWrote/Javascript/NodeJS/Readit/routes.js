var parse = require('co-body');
var render = require('./lib/render');
var monk = require('monk');
var wrap = require('co-monk');

// connect to mongo
var db = monk('localhost/koaBlog');

// enable generators with monk (e.g. `yield posts.insert(object)`)
var posts = wrap(db.get('posts'));


/**
 * Show tree of notes.
 */
module.exports.list = function *list() {
    var postList = yield posts.find({});
    this.body = yield render('list', { posts: postList });
};

/**
 * Show creation form.
 */
module.exports.add = function *add() {
    this.body = yield render('new');
};

/**
 * Show post :id.
 */
module.exports.show = function *show(id) {
    var post = yield posts.findOne({ _id: id });
    if (!post) this.throw(404, 'invalid post id');
    this.body = yield render('show', { post: post });
};

/**
 * Create a post.
 */
module.exports.create = function *create() {
    var post = yield parse(this);
    post.created_at = new Date;

    yield posts.insert(post);
    this.redirect('/');
};

/**
 * Show edit form
 */
module.exports.edit = function *edit(id) {
    var post = yield posts.findOne({ _id: id });
    this.body = yield render('edit', { post: post });
};

/**
 * Update post
 */
module.exports.update = function *update(id) {
    var post = yield parse(this);
    yield posts.updateById(id, post);
    this.redirect('/post/' + id);
};

/**
 * Remove post
 */
module.exports.remove = function *remove(id) {
    yield posts.remove({ _id: id });
    this.redirect('/');
};

/**
 * Show reply form
 */
module.exports.addReply = function *addReply(parentID) {
    var parent = yield posts.findOne({ _id: parentID });
    this.body = yield render('reply', { parent: parent });
};

/**
 * Attach the reply to the post-graph
 */
module.exports.createReply = function *createReply(parentID) {
    var newPost = yield parse(this);
    newPost.created_at = new Date;

    var parentPost = yield

    // parallel updates
    yield [
        posts.updateById('TODO: parentID', {}, fn);
        posts.insert(newPost);
    ];
    this.redirect('/');
};
