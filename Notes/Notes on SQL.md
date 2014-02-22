### Foreign Keys

Sample (PostgreSQL) Code for creating a Foreign Key

    videos_id varchar CONSTRAINT FK_videos_id
    references videos(id)
    match simple
    on update cascade
    on delete cascade

or simply

    ALTER TABLE bar add FOREIGN KEY (id) references foo(id);

According to the [Docs](http://www.postgresql.org/docs/8.3/static/ddl-constraints.html#DDL-CONSTRAINTS-FK)
(which look very well-done in general):

* They are by default
    * Allowed to be NULL
    * `MATCH SIMPLE` --- allows some foreign key columns (of a multiple 
      column FK) to be null while other parts of the foreign key are
      not null. 
    * `on (UPDATE|DELETE) no action` ---  if any referencing rows still
      exist when the constraint is checked, an error is raised

Though you can also do

* `MATCH FULL` --- doesn't allow one column of a multicolumn foreign key to be 
  null unless all foreign key columns are null.
* `on (UPDATE|DELETE)` --- action possibilities are the same for both
    * `restrict` --- prevent deletion of a referenced row
        * The essential difference between these `no action` and `restrict`
          is that `NO ACTION` allows the check to be deferred until later
          in the transaction, whereas `RESTRICT` does not.
    * `cascade` --- when a referenced row is deleted, row(s)
      referencing it should be automatically deleted as well.
    * `set (NULL|DEFAULT)` --- update value to this value when referenced
      row is deleted
        * Note that these do not excuse you from observing any
          `constraints`. For example, if an action specifies `SET DEFAULT`
          but the `default` value would not satisfy the `foreign key`,
          the operation will fail.

### NULL

* By default, values are allowed to be `NULL`
* You can prevent this by saying

        price numeric NOT NULL CHECK (price > 0)

* You may also want to specify that it *can* be `NULL` by saying

        price numeric NULL


> *Tip:* In most database designs *the majority of columns should
> be marked* `NOT NULL`.
