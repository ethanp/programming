
| Question   | Answer                                                |
| :--------: | ----------------------------------------------------- |
| **What?**  | Chat Room Play! 2 app with a multi-chatroom model     |
| **How?**   | Written from scratch, with help from examples online  |
| **Tools?** | *Akka*, *WebSockets*, and *Redis*.                    |

Client Side
===========

Well, for now there are to be ***no GUI effects***, because that is not
the primary goal here. The primary goal is to get a ***working infrastructure***.
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

The other stuff is done using normal HTTP


## New-discussion Page

* A Form for adding members
* Invite button (generates empty map and navigates you there)

## Discussions Page

### Layout

* On the left is user's list of discussions
* In the middle is the currently-selected discussion
* On the bottom is the textarea where you submit a message
* On the right is a list of participants
    * People who haven't accepted the invitation to participate are noted too


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


