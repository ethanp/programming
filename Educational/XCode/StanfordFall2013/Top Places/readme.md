iOS Assignment 6
================

Started 12/30/13

Evolving List of Next-Steps to Persue
-------------------------------------

1. Get a `UIScrollView` working to actually view the photos

Notes
-----

1. Cells in the BrowseTab are called `@"BrowseTab Cell"`s
1. Cells in a PhotoList are called `@"PhotoList Cell"`s

OVERALL
-------
### (don't edit this, it's not an as-you-go type of list)

1. Dictionary keys are `#define`d in `FlickerFetcher.h`

1. Main Thread should never be *blocked*

1. Must work for
    * Both *portrait and landscape*
    * On *iPhone and iPad*
        * Using platform specific idioms
            * "Don’t let your iPad version look like a gigantic iPhone screen"

1. The list of recent photos should be persistent across launchings of the app
    * So save it in `NSUserDefaults`

1. Make it work on a real iOS device (if they let me...lol)

BROWSE TAB
----------

1. List of places
    * Divided into sections by country
        * Country is the Section Title
    * Alphabetical within each section
    * City Name is the Title
    * Rest of the name (state, province, etc.) is the Subtitle

### Photo List

1. Clicking on a place queries Flickr to get (an array of) 50 photos from there
    * Display them in a list
    * Use `[FlickrFetcher URLforPhotosInPlace:maxResults:]`

1. The list of photos should have
    * Photo Title as Title
    * Description of Photo as Subtitle
    * If no photo title, use Description as Title
    * If no description either, use "Unknown" as Title

Showing the Photo
-----------------

1. On choosing a photo, show it inside a `UIScrollView`
    * Get the photo with `[FlickrFetcher URLForPhoto:format:]`

1. Put the photo's title somewhere on the screen

1. Must allow *pan and zoom*
    * Should appear automatically zoomed "to show as much of the photo as possible
      with no extra, unused space."

HISTORY TAB
-----------

1. 20 most recently viewed photos (no duplicates)
1. In chronological order

Some Random Notes from Lecture
-----------------------

