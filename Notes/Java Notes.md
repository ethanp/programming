Java Notes
==========

Inheritance
-----------

### Private fields

**5/9/14**

* `private` fields are not inherited by subclasses.
* Both `protected` *and* `public` fields *are* inherited by subclasses,
  *no matter* whether they are in the same `package`.
* Instances of subclasses of course *do **contain*** the private fields
  of their superclasses, they just have no access to them.
  
### Useful chart

**5/9/14**

| Modifier    | Class | Package | Subclass | World|
|:-----------:|:-----:|:-------:|:--------:|:----:|
| public      |   ✔   |    ✔    |    ✔     |   ✔  |
| protected   |   ✔   |    ✔    |    ✔     |   ✘  |
| no modifier |   ✔   |    ✔    |    ✘     |   ✘  |
| private     |   ✔   |    ✘    |    ✘     |   ✘  |


#### Notes on chart

`no modifier` is even stricter than `protected`, and what it *doesn't*
allow is `subclass` access, even though it retains `package` access. I
find this counterintuitive, personally.

### instanceof

**5/9/14**

`child instanceof Parent` will return **`true`**


Equals and HashCode
-------------------

[SO](http://stackoverflow.com/questions/27581)

### Equals

Must be 

* reflexive -- `a.equals(a) == true`
* symmetric -- `a.equals(b) iff b.equals(a)`
* transitive -- `a.equals(b) && b.equals(c) => a.equals(c)`
* consistent  -- always returns the same value for the same [unchanged] object

If you *don't* `@Override public boolean equals(Object o){}`, each instance is
*equal only to itself*. If this is what you want: *don't override* `equals` (e.g. `Thread`).


### Hash Code

Rules

* `a.equals(b) => a.hashCode() == b.hashCode()`


Comparable vs. Comparator
-------------------------

* [SO](http://stackoverflow.com/questions/4108604)
* [digizol](http://www.digizol.com/2008/07/java-sorting-comparator-vs-comparable.html)

Both are `interfaces` you can `implement`

### Comparator

* **Says "I can compare two *other* objects with each other"**
* Allows *you* to define comparison logic for types you don't control.
* E.g. you could write a new way to compare strings by `extending
  Comparator`.

#### Signature
  
	java.lang.Comparator: int compare(Object o1, Object o2) {
		
		case o1 > o2 => x > 0;
		case o1 = o2 => x = 0;
		case o1 < o2 => x < 0;
	
	}

#### How to declare

	public class MyClassSortByField implements Comparator<MyClass> {
	
		public int compare(MyClass o1, MyClass o2) {
			o1.getField().compareTo(o2.getField());
		}
	
	}
	
#### How to use

Make a method like this


		public static Comparator<Fruit> FruitNameComparator 
                          = new Comparator<Fruit>() {
 
		    public int compare(Fruit fruit1, Fruit fruit2) {
		
		      String fruitName1 = fruit1.getFruitName().toUpperCase();
		      String fruitName2 = fruit2.getFruitName().toUpperCase();
		
		      //ascending order
		      return fruitName1.compareTo(fruitName2);
		
		      //descending order
		      //return fruitName2.compareTo(fruitName1);
		    }
 
		};
		
And then do this
	
	import java.util.Arrays;
	
	Fruit[] fruits = new Fruit[4];
 
	Fruit pineappale = new Fruit("Pineapple", "Pineapple description",70); 
	Fruit apple = new Fruit("Apple", "Apple description",100); 
	Fruit orange = new Fruit("Orange", "Orange description",80); 
	Fruit banana = new Fruit("Banana", "Banana description",90); 

	fruits[0] = pineappale;
	fruits[1] = apple;
	fruits[2] = orange;
	fruits[3] = banana;

	Arrays.sort(fruits); // ClassCastException
	
	Arrays.sort(fruits, Fruit.FruitNameComparator);  // works
	
### Comparable

* **Says "I can compare *myself* with another object"**
* Allows you to define comparison logic for *your own* types.

#### Signature
  
	java.lang.Comparable: int compareTo(Object o1) {
		
		case this > o2 => x > 0;
		case this = o2 => x = 0;
		case this < o2 => x < 0;
	
	}
	
#### How to declare

	public class MyClass implements Comparable<MyClass> {
	
		public int compareTo(MyClass o) {
			return this.field - o.field;
		}
	
	}

#### How to use

Simply declare it as in the "How to declare" section above.

### Upshot

**Use `comparable` if it's your class, otw use `comparator`**