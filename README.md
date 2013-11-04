
This was to be an actor based RESTful API server for message allocations

It was built using Akka, Spray and Scala.  It deploys on Heraku.  There is a simple test written in Python.


Goal
Build an HTTP REST API endpoint that “routes” messages optimizing for the number of network requests.
Background Info
You work for a company that sends large amounts of messages on its infrastructure. For the sake of this challenge, your job is to make sure those messages get sent using the smallest number of requests possible. For backend resources, you have at your disposal 4 different categories of message relays, each with its own rate of throughput. Here is a table of their specifications:
￼￼￼￼￼Category Name
￼￼￼relay IP address subnets
￼￼￼Throughput
￼￼Cost Per Request
￼￼￼￼￼Small
￼￼￼￼10.0.1.0/24
￼￼￼￼1 msgs/request
￼￼￼$0.01
￼￼￼￼￼Medium
￼￼￼￼10.0.2.0/24
￼￼￼￼5 msgs/request
￼￼￼$0.05
￼￼￼￼￼Large
￼￼￼￼10.0.3.0/24
￼￼￼￼10 msgs/request
￼￼￼$0.10
￼Super
￼￼￼￼10.0.4.0/24
￼￼￼￼￼25 msgs/request
￼￼￼￼$0.25
￼Requirements
1. Functionality a. API
i. Inputs: The message and a list of unique, 10­digit phone numbers to which the message is to be sent. Note: There could be up to five thousand phone numbers in any given request.
ii. Output: A routing of all the numbers to an appropriate IP Address within the given subnet for the desired relay category, optimizing for the constraints mentioned above. NOTE: you don’t have to actually “send” any messages, just return the routing.
iii. See the examples at the bottom of the document for example inputs and outputs. 2. Constraints
a. You are not allowed to underutilize a request. For example, you should not route a list of 20 contacts to the “super” sized relay as it would be inefficient because it would waste 5 messages.
3. Design
a. The REST API design should follow best practices when it comes to designating uniform
resource identifier, the HTTP verbs used to interact with the API (GET/POST/PUT/DELETE etc).
b. The representation used to interact with the API should be JSON
￼￼
￼c. Error handling is important. Provide a consistent RESTful approach to your error handling techniques.
4. Algorithm
a. Complexity
i. Provide an analysis of the computational complexity of the algorithm you used to solve the problem.
ii. What is its complexity?
iii. Can you categorize this problem into the same category of other well known problems? iv. Is it possible to optimally solve this problem in polynomial time? What about with other
throughput values?
5. Language and Tools
a. You’re free to write your solution in Python, Java, Scala, Go, PHP, or Ruby. You are free to use
any tools or libraries that you please. If you’d like to use a different language, please let us know.
6. Nuts and Bolts a. Testing
i. Automated Testing is not only a good way to develop, but also an easy way to prove that your code works as expected.
b. Hosting
i. We suggest using a free Tier on AWS or Heroku to host your code. Let us know if you
have any issues with this or if you have already used up your free account limit.
Delivery
1. Please get as far along as you can in the allotted time. If there are things you did not get to or further improvements you would make, please describe and prioritize them with a rationalization for their rank in priority.
2. Code and a working demo link to your server should be delivered by email.
3. You must provide working tests to prove that your code works.
￼
￼Examples: Input:
{
“message”:“30Rocks”, “recipients”:[“+15555555556”,“+15555555555”,“+15555555554”,“+15555555553”,
“+15555555552”,“+15555555551”] }
Possible Output:
{
“message”:“30Rocks”, “routes”:[
￼] }
{
“ip”:“10.0.1.1”,
“recipients”:[“+15555555556”] },
{
“ip”:”10.0.2.1”,
“recipients”:[“+15555555555”,“+15555555554”,“+15555555553”, “+15555555552”,“+15555555551”]
},



