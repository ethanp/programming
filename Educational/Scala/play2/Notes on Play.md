Notes on Playframework 2
========================

Architecture
------------

A few perspectives from which one might like to consider how
to architect the website:

1. Data Model -- what are the tables, schemas, DBs, and APIs?
1. HTTP Interface -- what will the resulting `Routes` file look like?
    * Note that shorter URLs are better because they're more usable 
1. User Interface -- what will the resulting website *feel* like?

Important
---------

### 2.3.2. Form Object (@15%)

##### Listing 2.27

    private val productForm: Form[Product] = Form(      mapping(        "ean" -> longNumber.verifying(          "validation.ean.duplicate", Product.findByEan(_).isEmpty),        "name" -> nonEmptyText,        "description" -> nonEmptyText      )(Product.apply)(Product.unapply)    )

> A form consists of a mapping together with two functions
> that the form can use to map between itself and an instance
> of our Product model class. 

> The first part of the mapping 
> specifies the fields and how to validate them. There are 
> several different validations, and you can easily add your 
> own. 

> The second and third parts of the mapping are the 
> functions the form will use to create a Product model 
> instance from the contents of the form and fill it from
> an existing Product, respectively.

> Our form’s fields map
> directly to the Product class’s fields, so we simply use 
> the `apply` and `unapply` methods that the Scala compiler 
> generates for case classes. If you’re not using case 
> classes or there’s no one-to-one mapping between the case 
> class and the form, you’ll have to supply your own 
> functions here.

Conventions
-----------

Put small or frequently used templates in a `app/views/tags/` package

Other
-----

### Flash Scope

* Like session scope, keeps data related to a client, 
  outside the context of a single request.
* Difference is that it is removed after the next request
* Implemented in Play as a cookie cleared on every response
    * So is Session info 
    * This is so different servers can handle the same client's
      request, and still be able to deal properly with both flash
      and session information.
          * This allows you use a cluster of servers in a round-robin.
