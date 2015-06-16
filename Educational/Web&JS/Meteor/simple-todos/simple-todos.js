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

}
if (Meteor.isServer) {
  Meteor.startup(function () {
    // code to run on server at startup
  });
}
