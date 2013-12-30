iOS Assignment 4
================

Started 12/19/13

Evolving List of Next-Steps to Persue
-------------------------------------

1. Handle transition to `landscape` layout
    * Why do cards change shape-ratio on the switch to `landscape`?
        * Maybe because the Grid.cell's `aspectRatio` is switched when the layout
          changes for some reason.
            * If this *is* true it was clearly a terrible design decision.
    * maybe from the `BaseViewController` override `view{Will|Did}LayoutSubviews`?
    * Read [How View Controllers Participate in the View Layout Process](https://developer.apple.com/library/IOS/featuredarticles/ViewControllerPGforiPhoneOS/AdoptingaFull-ScreenLayout/AdoptingaFull-ScreenLayout.html#//apple_ref/doc/uid/TP40007457-CH13-SW1)
    * I have written in my notes what functions get called
    * Also there are some notes on this in the slides
1. Make one card deal after the other
    * Add cards to-be-added to a `queue`

```

    completion:^(BOOL c){
        if(c){
            [self.container animationCompleted:self];
            [self.container animateNextCardFromQueue];
    }}

```

1. Make cards fill screen again when there are only a few left
    * Ideally, they should "fly" to their new position
1. Implement `handlePinch:`

OVERALL
-------
### (don't edit this, it's not an as-you-go type of list)
1. Animate arrival and departure of cards **CHECK**

2. Remove matched cards from the UI **CHECK**

3. Animate Redeals **CHECK**
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

7. Both `ViewControllers` should *inherit* from a `BaseController` class **CHECK**
    * Subclasses should have less then 25 lines each

8. Instead of `UIButtons` to display cards, use a `MutableArray` of `CardView`s **CHECK**



SET CARDS
---------
1. **Draw them** using `UIBezierPath` and `Core Graphics` **CHECK**
    * No images or `attributedStrings`
    * Drawings must *scale* properly

2. *Attributes* **CHECK**
    * Squiggles, Diamonds, or Ovals
    * Solid, Striped (*not* shaded), or Unfilled
    * Green, Red, or Purple
    * 1, 2, or 3

3. Start with 12 cards, but have the option of dealing 3 more at any time
    * Handle the case when there are no cards left in the deck

4. The API must be generalized for the contents to be rendered in whatever way **CHECK**
   the view wants them to be
    * (e.g. Colors 1,2,3; Shapes 1,2,3; Fills 1,2,3)


PLAYING CARDS
-------------
1. Use `PlayingCardView` from the in-class demo **CHECK**

2. Animate "flipping" the cards over **CHECK**

3. Will need to add public API for
    * Putting new cards into play
    * Getting the number of cards in play
    * NO METHOD TO DELETE CARDS
        * Just hide them instead

Some Random Notes from Lecture
-----------------------
* self.hidden means:
    1. it still has its place in the view hierarchy,
    2. it still belongs to the superview,
    3. it still lives in its frame,
    4. it is not on screen,
    5. it does not handle events.
    * He said "you probably won't need this for the homework, but you could.

