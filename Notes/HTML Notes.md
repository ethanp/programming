latex input:    mmd-article-header
Title:				  HTML Notes
Author:			    Ethan C. Petuchowski
Base Header Level:		1
latex mode:     memoir
Keywords:			  HTML, programming language, syntax, fundamentals, web programming
CSS:				    http://fletcherpenney.net/css/document.css
xhtml header:		<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
copyright:		  2014 Ethan Petuchowski
latex input:		mmd-natbib-plain
latex input:		mmd-article-begin-doc
latex footer:		mmd-memoir-footer

## Useful Tags

### \<small>

	<p> normal sized text </p>
	<p><small> slightly smaller sized text </small></p>


### Link in a CSS stylesheet

In the `<head>`, put

    <link href="/mycss.css" rel="stylesheet">
    
`rel="stylesheet"` means it is CSS

### \<button>
Literally just puts a button on the page.  
You can use Javascript to make it your bitch.

    <button>Like</button>

## Useful snippets

### `POST` from `<form>`

Create a **form** that **POST**s the text in a **text box** to the URL `/form`

    <form action="/locationToPostTo" method="post">
      <input type="text" name="label for the textbox" placeholder="grey default text in text box">
      <input type="checkbox" name="label text" "checked">
      <input type="submit" value="text for submit button">
    </form>

### Faking `PUT` with `POST` from `<form>`

The second line makes it so that what is actually sent as a POST
by the browser, is received as a PUT by the router (because browsers
don't support PUT requests. This works for at least Sinatra & Rails.

    <form action="/place" method="post" id="edit">
        <input type="hidden" name="_method" value="put">
        <textarea name="content">The content</textarea>
        <input type="checkbox" name="complete" "checked">
        <input type="submit">
    </form>
