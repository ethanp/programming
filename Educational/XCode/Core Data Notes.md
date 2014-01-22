Links
-----

Apple's [Core Data Basics](https://developer.apple.com/library/ios/documentation/Cocoa/Conceptual/CoreData/Articles/cdBasics.html)

Ray Wenderlich's [Core Data Tutorials](http://www.raywenderlich.com/tutorials#attachment_951)

Techotopia's [Chapter on Core Data](http://www.techotopia.com/index.php/Working_with_iOS_7_Databases_using_Core_Data)

Notes
=====

Overview
--------

##### Core Data is a wrapper around a database so you only see objects, not DB.

* The best way to persist data on the iPhone for non-trivial data storage.
* Reduce the memory overhead of your app
* Increase responsiveness
* Save you from writing a lot of boilerplate code
* Heavy learning curve

### High Level

* You can't retrieve only certain attributes of an object,
  you *must* retrieve the whole thing
* You can view the schema/data via [SQLite DB Browser](http://sourceforge.net/projects/sqlitebrowser/)

#### Going through Changes

* When you run your app after changing your storage model (Managed Object
  Model) and there is already a persistent store created, it doesn’t know how
  to read it, and crashes. So you can:
    * Delete the app from your device/simulator (*much* easier!)
    * Perform a model upgrade

### Other cool stuff you can do

#### Import Existing Data
[This](http://www.raywenderlich.com/12170/core-data-tutorial-how-to-preloadimport-existing-data-updated)
tutorial shows you how to use a simple command-line app to

1. take a schema from *another app*
1. take *existing* data from any number of JSON, XML, another DB, CSV, a spreadsheet,
  the Internet, etc.
1. import that into a database
1. copy that imported data back into the original app
1. provide that data for use from the DB on startup in the original app

It's not all that complicated, but still, I bet there's an easier way to do it
elsewhere.  In any case, I don't need to do that at this time.

Managed Objects
---------------

* Like a **database row**
* `NSManagedObject`
* Object you create to store data
* Maintained and managed by the [managed object context](#managedobjectcontext)
* You make one to store data, and you get one when you retrieve data

<a id="managedobjectcontext"></a>
Managed Object Context
----------------------

* Like an **intelligent scratch pad** for objects from the database
    * You tell the context a bunch of things to do, then you tell it to execute them
* It's what we use the most
* Used to get/insert/delete objects

##### More
1. You fetch objects from a persistent store, bringing temporary copies onto the scratch pad
2. They form an object graph (or a collection of object graphs).
3. If modify those objects without *saving* the changes, the persistent store
   doesn't change.
4. If you access data that you *haven't* from a property you *have* pulled in
  (e.g. an object via a relationship), Core Data "faults" and pulls in the data
  right as you need it.

<a id="managedobjectmodel"></a>
Managed Object Model
--------------------

* Defines the **Database Schema**
* I.e. defines [entities](#entity) and how they relate to eachother
* Edit it with the visual editor *or* with code

[Note](http://stackoverflow.com/questions/298739/what-is-the-difference-between-a-schema-and-a-table-and-a-database)
`schema : database : table :: floor plan : house : room`

<a id="entity"></a>
Entity
------

* A **Table**
* Can contain:
    * Attributes
        * E.g. name, phone number, etc.
    * Relationships
        * one-to-one
        * one-to-many
        * many-to-many
    * Fetched property
        * Weak, one-way relationships (??, no me importa)
    * Fetch request
        * Predefined query
        * Referenced to retrieve objects corresponding to the query

Persistent Store Coordinator
----------------------------

* The **Database Connection**
* Coordinates access to (could be multiple) persistent object stores
* A programmer needn't care much about this thing
* E.g. this is what the [Managed Object Model](#managedobjectmodel) is telling
  to save stuff

Persistent Object Store
-----------------------

* Regardless of type, your code looks the same
* Disk-Based
    * SQLite (**default**)
    * XML
    * Binary
* Memory-Based
    * There aren't multiple options

### Defining an Entity Description

* I'ma read this when I'm actually creating one of these,
  I think I'ma pass on that for now though.

Database Theory
---------------

### [View](http://en.wikipedia.org/wiki/View_(SQL))
**The result set of a stored query on the data, which the database
users can query just as they would in a persistent database collection object.**
Changes applied to the data in a relevant underlying table are reflected in the
data shown in subsequent invocations of the view.

This pre-established query command is kept in the database dictionary. Unlike
ordinary base tables in a relational database, a view does not form part of the
physical schema: as a result set, it is a virtual table computed or collated
from data in the database, dynamically when access to that view is requested.

[Adding Core Data to existing project in XCode 4](http://stackoverflow.com/questions/6821719/adding-core-data-to-existing-project-in-xcode-4)
=================================================


1. Create a new *Empty Project* with Core Data support
1. Copy over the relevant new code to your original project and compile
1. Add `#import <CoreData/CoreData.h>` to the `.pch` file or to the relevant `.m` files
1. Add the CoreData Framework via
    * Project --> Targets --> Summary --> `+` in Linked Frameworks and Libraries

Getting the managedObjectContext out of the AppDelegate
=======================================================

    FocusTestAppDelegate *appDel = (FocusTestAppDelegate *)[UIApplication sharedApplication].delegate;
    NSManagedObjectContext moc = appDel.managedObjectContext;

[Saving Images to Core Data](http://stackoverflow.com/questions/10311271/saving-image-to-core-data)
============================

### To save:

    NSData *imageData = UIImagePNGRepresentation(myUIImage);
    [newManagedObject setValue:imageData forKey:@"imageKey"];

### To Retrieve Image:

    NSManagedObject *selectedObject = [[self fetchedResultsController] objectAtIndexPath:indexPath];
    UIImage *image = [UIImage imageWithData:[selectedObject valueForKey:@"imageKey"]];
    [[newCustomer yourImageView] setImage:image];


