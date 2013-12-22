iOS Assignment 4
================

Started 12/19/13

Concrete Next Steps to Persue
-----------------------------
1. Cards should flip according to the underlying game functionality
    * **Plan:**
        * `[PlayingCardView handleTap:]` calls `[self.container cardWasFlipped:]`
        * `[PlayinCardViewController cardWasFlipped:]` runs the game logic:
            * updates `cardsInPlay`
                * whichever card is *moved in/out* of `cardsInPlay` is *phyisically* **flipped**
                    * this **animation** needs to be extracted from `handleTap:`
            * updates `scoreLabel`
1. *Where-the-Heck* are the faces on the facecards at these days?
    * They just randomly stopped showing up?
1. Make `addCard()` method for BaseViewController
1. Draw the `SetCard`s with Bezier curves

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

