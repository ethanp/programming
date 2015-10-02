### But what does it DO?

You can navigate your local file tree, and look at files.

Life begins at the root of this directory. I.e. in the directory that this `readme` sits in.
From there you can navigate around.


#### Sample usage

1. Open browser
2. Navigate to `localhost:8080`
    * Now you should see a listing of the files in _this_ directory
3. Click on a file
    * If it's a directory, it shall list that directory
    * If it's doesn't exist, you shall be told so
    * If it's a file, that file shall be rendered to the browser as plain text


### TODO

1. Render the text files in such a way that the new-lines are preserved. I have a feeling this
   just requires a particular mime-type. But I'm on a plane so I can't google it.
2. Send over the favicon.png on requests for `/favicon.ico`
