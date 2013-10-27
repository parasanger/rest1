
This was to be an actor based RESTful API server for the problem

It was built using Akka, Spray and Scala.  It was to be deployed on Heraku.

This was  a poor choice of technologies.    The technologies are sound, but the
versions of each of the components are not clear which class in which jar
are compatible with each other.   80% of the time was spent tracking down
version incompatabilities via google.

It builds but won't run as after all the other versions were worked out
Akka 2.2 doesn't work with Scala 2.10.

I don't know Python, but would consider starting over and doing it in Python
if I had a do over.

The actual algorithm to fit the messages into as few buckets as possible appears
to be a variant on  create change using the fewest number coins.

The agorithm here is:

    While I have messages and buckets
        find the bucket size mesgs that completely fit into the largest bucket
        if there are fills
            create a response object for each bucket
            remove these messages from the message list
        repeat with next smallest size bucket.










