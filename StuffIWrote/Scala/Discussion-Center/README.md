Future Work
===========
The whole thing. I used trying to start this thing as an opportunity to learn
how these things work. But it basically turned into me copy/pasting code from
the internet to solve all the problems. That's not so much fun. So I'm going
to move onto a new project that won't allow me to copy/paste the whole thing
off the internet. But I can use a lot of foundation-layer code from this
project in making the next project(s).

Learnt:

* How to get `Bootstrap 3` to work with `Play` and make it look real nice
* How to connect a `WebSocket` between the server's `(Iteratee, Enumerator)` and
  the client's `new WebSocket(address)` object and send messages back and forth

Client Side
===========

Well, for now there are to be ***no GUI effects***, because that is not the
primary goal here. The primary goal is to get a ***working infrastructure***.
If I actually finish that, the GUI can come next.

## Landing Page

### Layout

They type in their Username (and eventually Password) and Sign in.

### Implementation

The Sign-in button sends a `POST` (for eventual Password reasons) with
their credentials.

## Just-logged-in Page

### Layout

* A List of Discussions in which they are currently a Member.
* A Button to Create a New Discussion
* A news feed of recent changes to their discussions
* Any outstanding discussion invitations they can accept/deny

### Implementation

The WebSocket powers:

* The news feed
* Invitation response actions

`GET` requests are sent for

* Requesting an existing Discussion
* Creating a New Discussion


## New-discussion Page

### Layout

* A Form for adding members
* Start button (generates empty map and navigates you there)

### Implementation

The WebSocket or AJAX powers:

* A dropdown autocomplete with which member you want to join

A `POST` request is sent for

* Hitting the Start button

## Discussions Page

### Layout

* On the left is user's list of discussions
* In the middle is the currently-selected discussion
* On the bottom is the textarea where you submit a message
* On the right is a list of participants
    * People who haven't accepted the invitation to participate are noted too

The WebSocket powers:

* Submitting messages
* Downloading messages for *any* of the conversations you are a part of
* ds

Server Side
===========

Creating a Discussion
---------------------

Submitting a Message
--------------------

*Actors* in the Play
--------------------

Every discussion's messages are mapped to one stream,
who figures out where to stream the messages *to*.

#### DiscussionApp

**Handles the boot-up and boot-down of the App**

Messages it can Receive

* Load_Redis_From_Disk
* Put_Data_Where_It_Belongs
* Initialize_Other_Stuff

#### DiscussionActor

**Manages a particular Discussion within the App**

Messages it can Receive:

* Open
* Close
* New_Member
* Invite_Member
* Accept_Invite
* New_Message
* Member_Left
* Rename

#### ParticipantActor

**Connects a user with his associated discussions**

Messages it can Receive:

* Connect_To(discussion: String)
* Disconnect_From(discussion: String)
* Message_To(discussion: String, message: Message)


