Links
-----

Apple's [Core Data Basics](https://developer.apple.com/library/ios/documentation/Cocoa/Conceptual/CoreData/Articles/cdBasics.html)

Ray Wenderlich's [Core Data Tutorials](http://www.raywenderlich.com/tutorials#attachment_951)

Techotopia's [Chapter on Core Data](http://www.techotopia.com/index.php/Working_with_iOS_7_Databases_using_Core_Data)

Notes
=====

Overview
--------

##### Core Data gives you an "Object-Oriented Database"

* The best way to persist data on the iPhone for non-trivial data storage.
* Reduce the memory overhead of your app
* Increase responsiveness
* Save you from what would be even *more* boilerplate code
* Heavy learning curve

### High Level

* You can't retrieve only certain attributes of an object,
  you *must* retrieve the whole thing
* You can view the schema/data via [SQLite DB Browser](http://sourceforge.net/projects/sqlitebrowser/)
* Each fetch can only return from a single entity table

#### Going through Changes

* When you run your app after changing your database schema ("[Managed Object
  Model](#managedobjectmodel)") and there is already a persistent store created, it doesn’t know how
  to read it, and it crashes. So you can:
    * Delete the app from your device/simulator (*much* easier!)
        * On both, this involves click-and-hold an app, and hit the little (x)
    * Perform a model upgrade (i.e. migration, which NSHipster [says](http://nshipster.com/nscoding/)
      is "Automatic")

### Other cool stuff you can do

#### Import Existing Data
Ray's [2nd tutorial](http://www.raywenderlich.com/12170/core-data-tutorial-how-to-preloadimport-existing-data-updated)
shows you how to use a simple command-line app to

1. take a schema from *another app*
1. take *existing* data from any number of JSON, XML, another DB, CSV,
   a spreadsheet, the Internet, etc.
1. import that into a database
1. copy that imported data back into the original app
1. provide that data for use from the DB on startup in the original app

It's not *that* complicated, but still, I bet there's an more straightforward
way to do it elsewhere.  In any case, I don't need to do that at this time.

Common Programming Functions/Patterns
---------------------------

### Add methods you want to the model objects using categories

new-file --> Objective-C category

### Retrieve a UIImage from a Binary Data attribute

      UIImage *image = [UIImage imageWithData:self.data];

### Insert object into the database
Consider using a **constructor** in the object's category that knows how to
traverse the relationship-graph as necessary.

    insertNewObject:forEntityForName:inManagedObjectContext:

### Deleting objects from the database.
Don't keep strong pointers to deleted objects!  Consider also having a
**destructor** in the object's category.

    [document.managedobjectcontext deleteObject:result];

### Querying the Database

Create an **`NSFetchRequest`**

1. **Entity** to fetch (required) (i.e. table to fetch from)
2. **Number** to fetch (optional, default: all)
3. `NSSortDescriptors` to **sort** array of fetched objects (optional, default?)
4. `NSPredicate` to **filter** which results are retrived (optional, default: all)

**Each fetch can only return from a *single* entity table**

#### Example:

Tell it the type of entity to fetch

    NSFetchRequest *request = [NSFetchRequest fetchRequestWithEntityName:@"Diag"];

Return 20 at a time, even if there are more

    request.fetchBatchSize = 20;

Stop after fetching 100

    request.fetchLimit = 100;

Some `@selectors` can be performed on the database side (faster).

If you have multiple `sortDescriptors` in the `@[array]`, you first sort by the
first, then sort within those by the second, etc.

    NSSortDescriptor *sortDescriptor = [NSSortDescriptor
                                        sortDescriptorWithKey:@"name"
                                        ascending:YES
                                        selector:@selector(localizedStandardCompare:)];

    request.sortDescriptors = @[sortDescriptor];

See the documentation for [NSPredicate](https://developer.apple.com/library/mac/documentation/Cocoa/Reference/Foundation/Classes/NSPredicate_Class/Reference/NSPredicate.html)
for many more options. [Some predicates](https://developer.apple.com/library/ios/documentation/cocoa/conceptual/KeyValueCoding/Articles/CollectionOperators.html),
like `results.@count` are done in the database.

    NSPredicate *predicate1 = [NSPredicate predicateWithFormat:@name contains[c] %@", (NSString *)];
    NSPredicate *predicate2 = [NSPredicate predicateWithFormat:@"timestamp > %@", (NSDate *)];

    NSPredicate *predicate3OR4 = [NSPredicate predicateWithFormat:@"group.name = %@
                                OR any results.@count > 5", (NSString *)];

    NSPredicate *pred1AND2 = [NSCompoundPredicate andPredicateWithSubpredicates:@[predicate1, predicate2]];

[Note](https://developer.apple.com/library/ios/documentation/cocoa/conceptual/KeyValueCoding/Articles/CollectionOperators.html)
that given *any* Array, Dictionary, Set, you can do

    [myCollection valueForKeyPath:@"@avg.title.length"];

Back to the `request`, we may set the properties to fetch

    [request setPropertiesToFetch:@[@"name", @"passed"]];

Now we must ***execute* the request**

    NSManagedObjectContext *context = document.managedObjectContext;
    NSError *error;
    NSArray *photographers = [context executeFetchRequest:request error:&error];

* Empty array means no results
* `nil` array means error (check `error` for details)

### Hooking your Core Data into a UITableView

#### NSFetchedResultsController

[Ray Wenderlich's Tutorial, Core Data 3 for iOS 5](http://www.raywenderlich.com/999/core-data-tutorial-for-ios-how-to-use-nsfetchedresultscontroller)

* Much faster because it can be set to retrieve only a subset of the data at a time
* Make it an `@property`
* Can be used to hook an `NSFetchRequest` up to a `UITableViewController`

###[Saving Images to Core Data](http://stackoverflow.com/questions/10311271/saving-image-to-core-data)

#### To save:

    NSData *imageData = UIImagePNGRepresentation(myUIImage);
    [newManagedObject setValue:imageData forKey:@"imageKey"];

#### To Retrieve Image:

    NSManagedObject *selectedObject = [[self fetchedResultsController] objectAtIndexPath:indexPath];
    UIImage *image = [UIImage imageWithData:[selectedObject valueForKey:@"imageKey"]];
    [[newCustomer yourImageView] setImage:image];

### Getting a Managed Object Context, best practices

According to the [docs](https://developer.apple.com/library/ios/documentation/DataManagement/Conceptual/CoreDataSnippets/Articles/stack.html)
and various SOQs

* **a view controller typically shouldn’t retrieve the context from a global
  object such as the application delegate** because that makes the application
  architecture too rigid.
* **Neither should a view controller create a context for its own use** (unless
  it’s a nested context).
    * This may mean that operations performed using the
      controller’s context aren’t registered with other contexts, so different view
      controllers will have different perspectives on the data.
* *By convention*, you get a context from a view controller.
* When you implement a view controller that integrates with Core Data, you add
  an `@prop NSManagedObjectContext moc`.
* **When you create a view controller, you pass it the context it should use.**
* You pass an existing context, or (in a situation where you want the new
  controller to manage a discrete set of edits) a new context that you create
  for it.
* The **application delegate creates the context to pass to the first view
  controller** that’s displayed.

Important Classes
-----------------

### Managed Objects

* Like a **database row**
* `NSManagedObject`
* Object you create to store data
* Maintained and managed by the [managed object context](#managedobjectcontext)
* You make one to store data, and you get one when you retrieve data

<a id="managedobjectcontext"></a>
### Managed Object Context

* Like an **intelligent scratch pad** for objects from the database
    * You tell the context a bunch of things to do, then you tell it to execute them
* It's what we use the most
* Used to get/insert/delete objects

##### More
1. You fetch objects from a persistent store, bringing temporary copies onto the scratch pad
2. They form an object graph (or a collection of object graphs).
3. If modify those objects without *saving* the changes, the persistent store
   doesn't change.
4. When you access a @property for the first time on an object you have fetched
   Core Data "faults" and pulls in the data right as you need it.

<a id="managedobjectmodel"></a>
### Managed Object Model

* Defines the **Database Schema**
* I.e. defines [entities](#entity) and how they relate to eachother
* Edit it with the visual editor *or* with code

[Note](http://stackoverflow.com/questions/298739/what-is-the-difference-between-a-schema-and-a-table-and-a-database)
`schema : database : table :: floor plan : house : room`

<a id="entity"></a>
### Entity

* A **Table** and a **Class**
* Contains `@properties`:
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

### Persistent Store Coordinator

* The **Database Connection**
* Coordinates access to (could be multiple) persistent object stores
* A programmer needn't care much about this thing
* E.g. this is what the [Managed Object Model](#managedobjectmodel) is telling
  to save stuff

### Persistent Object Store

* Regardless of type, your code looks the same
* Disk-Based
    * SQLite (**default**)
    * XML ([not on iOS](https://developer.apple.com/library/mac/documentation/cocoa/conceptual/coredata/articles/cdPersistentStores.html))
    * Binary
* Memory-Based
    * There aren't multiple options

#### Defining an Entity Description

* I'ma read this when I'm actually creating one of these,
  I think I'ma pass on that for now though.

<a id="uimanageddocument"></a>
### UIManagedDocument

* "Think of `UIManagedDocument` as a container for your Core Data database"
* There's a decent amount of boilerplate in getting the thing going, though
  perhaps less than using Core Data itself
* `UIManagedDocument`s **autosave** themselves (asynchronously)
* The document automatically closes if there are no more strong pointers to it
* If you have multiple `UIManagedDocument`s, they each have their own MOC
    * So if one saves changes to "the" database, the others will need to "refetch"
      those changes
* UIManagedDocument performs all the basic set-up you need for Core Data, and
  in some cases you may use instances of UIManagedDocument directly (without a
  need to subclass).

So now I can always retrieve the MOC via

    AppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    appDelegate.document.managedObjectContext

#### From the Notes:

How would you watch a document’s managedObjectContext?

    - (void)viewDidAppear:(BOOL)animated
    {
        [super viewDidAppear:animated];
        [center addObserver:self
                selector:@selector(contextChanged:)
                    name:NSManagedObjectContextDidSaveNotification
    object:document.managedObjectContext]; //don’tpassnilhere!
    }
    - (void)viewWillDisappear:(BOOL)animated
    {
        [center removeObserver:self
                        name:NSManagedObjectContextDidSaveNotification
                        object:document.managedObjectContext];
        [super viewWillDisappear:animated];
    }


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

Adding Core Data to existing project
====================================

#### Use [UIManagedDocument](#uimanageddocuement)

I got the idea From Stanford Lecture 12 on XCode 5

[Collen Donnell](http://collindonnell.com/2013/10/16/uimanageddocument-for-core-data-apps/)
has much experience with Core Data, and as of 10/16/13 has this to say:

> Because none of the apps I was writing were document based (they don’t have
  to be), I never gave [`UIManagedDocument`] a second thought. Now that I have,
  I can’t see any compelling reason to not use it for all of the apps I’m
  writing.

Others agree.

1. You go `cmd+n` --> `Core Data` --> `Data Model` to create your `Model.xcdatamodeld`
1. You get the `moc` via a `UIManagedDocument`

#### From [SO for XCode 4](http://stackoverflow.com/questions/6821719/adding-core-data-to-existing-project-in-xcode-4)

1. Create a new *Empty Project* with Core Data support
1. Copy over the relevant new code to your original project and compile
1. Add `#import <CoreData/CoreData.h>` to the `.pch` file or to the relevant `.m` files
1. Add the CoreData Framework via
    * Project --> Targets --> Summary --> `+` in Linked Frameworks and Libraries

Getting the managedObjectContext out of the AppDelegate
=======================================================

    FocusTestAppDelegate *appDel = (FocusTestAppDelegate *)[UIApplication sharedApplication].delegate;
    NSManagedObjectContext moc = appDel.managedObjectContext;

