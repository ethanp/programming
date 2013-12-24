iOS Assignment 4
================

Started 12/19/13

Some Notes from Lecture
-----------------------
* Add a subview by sending `[containingView addSubview:view]`
* Remove a view by sending `[viewToRemove removeFromSuperView]`
* I should `hidden:YES` on `match` but `removeFromSuperView` on `redeal`
    * self.hidden means:
        1. it still has its place in the view hierarchy,
        2. it still belongs to the superview,
        3. it still lives in its frame,
        4. it is not on screen,
        5. it does not handle events.
     * He said "you probably won't need this for the homework, but you could.
* What is an animation?
    * You set a new value for a `property` of a `UIView`
    * But the *transition* to this new value occurs *over time*

Concrete Next Steps to Persue
-----------------------------
1. Cards should fly in from off the screen
    * `CardView`s should have an `@property CGRect homeFrame` which is where they belong in the `Grid`
    * Then they fly in from `self.superview.origin`? and `animateWithDuration` setting `self.frame = self.homeFrame`
    * To fly *out*, just set `self.frame = self.superview.origin` or something
    * `- (void)animateCardInsertion`
1. Cards should flip according to the underlying game functionality
    * **Plan:**
        * `[PlayingCardView handleTap:]` calls `[self.container cardWasChosen:]`
        * `[PlayinCardViewController cardWasChosen:]` runs the game logic:
            * updates `cardsInPlay`
                * create a method **in the ViewController** for cards that get *moved in/out* of `cardsInPlay`
                * call `[card animateCardFlip]` from within said method
                * call said method from `[ViewController updateUI]`
1. Cards from the last `Redeal` shouldn't be *showing behind the current set of cards!*
    * This might be taken care of when I fix `updateUI` for completing the point above
1. Draw the `SetCard`s with Bezier curves
1. May want to add the `UIPinchGestureRecognizer` to that `containerView`
    * See lecture 7 for info on this Recognizer

OVERALL
-------
1. Animate arrival and departure of cards

2. Remove matched cards from the UI

3. Animate Redeals
    * Don't just flip cards back over, actually take them out and redeal them

4. Adjust the UI layout when the number of cards onscreen changes
    * Use the provided `Grid` class
    * Use a generic `UIView` (not a subclass) in the storyboard solely as a
      boundary area for the cards

5. Make it work and look good in
    * Portrait *and* Landscape
        * Animate this transition
            * This should come for free because:
                * The UI is always appropriate for its bounds
                * All UI changes are animated


    * iPhone 4 *and* 5
    * Use **Autolayout** as much as possible for this
    * No code specific to any layout
        * I.e. no "magic numbers" or "`if (landscape) then`"

6. Create an animated "pinch" gesture
    * Pinching gathers the cards into a pile
    * Tapping the stack returns the cards unharmed back to their normal positions
    * Remember that an `attachment` behavior's `length` and `anchorPoint` can be
      modified at any time and the animation will instantly adapt

7. Both `ViewControllers` should *inherit* from a `BaseController` class
    * Subclasses should have less then 25 lines each

8. Instead of `UIButtons` to display cards, use a `MutableArray` of `CardView`s



SET CARDS
---------
1. **Draw them** using `UIBezierPath` and `Core Graphics`
    * No images or `attributedStrings`
    * Drawings must *scale* properly

2. **Attributes**
    * Squiggles, Diamonds, or Ovals
    * Solid, Striped (*not* shaded), or Unfilled
    * Green, Red, or Purple
    * 1, 2, or 3

3. Start with 12 cards, but have the option of dealing 3 more at any time
    * Handle the case when there are no cards left in the deck

4. The API must be generalized for the contents to be rendered in whatever way
   the view wants them to be
    * (e.g. Colors 1,2,3; Shapes 1,2,3; Fills 1,2,3)


PLAYING CARDS
-------------
1. Use `PlayingCardView` from the in-class demo

2. Animate "flipping" the cards over [check]

3. Will need to add public API for
    * Putting new cards into play
    * Getting the number of cards in play
    * NO METHOD TO DELETE CARDS
        * Just hide them instead

