Tasks = new Mongo.Collection("tasks");

if (Meteor.isClient) {
  Template.body.helpers({
    tasks: function () {
      return Tasks.find({}, {sort: {createdAt: -1}});
    }
  })

  Template.body.events({
    /* this right here is a dictionary
     in which all keys are events to listen for
     and values are their event handlers */

    // listen for 'submit' event on anything
    //  matching the .new-task selector
    "submit .new-task": function (event) {
      var text = event.target.text.value;
      Tasks.insert({
        text: text,
        createdAt: new Date()
      });
      event.target.text.value = "";

      // prevent default form submit
      return false;
    }
  })

  Template.task.events({
    "click .toggle-checked": function () {
      // `this` refers to an individual `Task` "document"
      // `_id` is its 'primary key'
      // update takes two params
      //  1. which subset of collection to update
      //  2. an update action to perform on that subset
      Tasks.update(this._id, {$set: {checked: !this.checked}});
    },
    "click .delete": function () {
      // remove's param is which subset of collection to delete
      Tasks.remove(this._id);
    }
  })
}

if (Meteor.isServer) {
  Meteor.startup(function () {
    // code to run on server at startup
  });
}
