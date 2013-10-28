
This was to be an actor based RESTful API server for message allocations

It was built using Akka, Spray and Scala.  It deploys on Heraku.  There is a simple test written in Python.

The major issues encountered in this exercise was not the solution to the problem, it was finding versions of Spray and
Akka that were compatible and didn't obsolete each others features.

Strictly as an excersise Python might have been a better proof of concept if unacceptable production solution

The actual algorithm to fit the messages into as few buckets as possible appears
to be a variant on  create change using the fewest number coins.

The agorithm here is:

    While I have messages and buckets
        find the bucket size mesgs that completely fit into the largest bucket, repeat on remaining messages
        if there are fills
            create a response object for each bucket
            remove these messages from the message list
        repeat with next smallest size bucket.

There are four buckets in this problem which we iterate over twice - i broke the fill from the choosing for debugging.
Regardless there is a constant C number of buckets with 2 iterations.  O(2C) = O(C)

Moving the messages into the correct size send buckets requires moving each receiver once, (N).   However this could be
reduced to (C) by using Scala's Views.  Where we don't copy the reciever numbers we just point into the inbound array.












