Video Comment Analyzer
======================

* Use the [YouTube API](https://developers.google.com/youtube/) to
  **download comments and titles for videos**
    * You [have to](https://developers.google.com/youtube/2.0/developers_guide_protocol_comments#Retrieve_comments)
      use [API v2](https://developers.google.com/youtube/2.0/developers_guide_java)
      even though they're on v3
* Do **Sentiment Analysis**
    * Use [Stanford's NLP Deep-Learned model](http://www-nlp.stanford.edu/sentiment/code.html)
        * An evaluation tool is included with the distribution:
          `java edu.stanford.nlp.sentiment.Evaluate`
        * One downloads the thing [here](http://nlp.stanford.edu/software/corenlp.shtml)
    * If it comes to training my own, there's:
        * [UC-Berklee Student's old-ish Scala Sentiment Learner](https://github.com/rxin/mining-sentiment)
        * [A shitty-ish-looking Java Learner that uses Hadoop/HBase](https://github.com/fads-io/SentimentAnalysis)
        * Tons of Data saved in Chrome/Data
        * But please don't resort to this. Just call `NLTK` if need-really-be.
* Learn what **types of videos** get good/bad sentiment?
* **Label spam**
* What videos get a lot/high-**proportion of spam**?
* Categorize **types of spam**
* Categorize **types of legitimate comments**
* Find comments that are a **response** to other comments
    * Even if they're not @tagged
* **Make it searchable** using Solr
* **Store the data** in Mongo
* **Make a web-app** in Play! 2
    * Eventually, enable the [Typesafe Console](http://typesafe.com/platform/runtime/console)
* Have it **graph the results** from the above analyses

