# SharedX
## A Collaborative shared text editor

### Technologies
* Spring
* MongoDB
* Netty
* Vaadin
* Operational Transformation
* Thread-safe operation queue: condition variable

### Structure
#### Server
Back-end, exchange client local operations

#### Client
Editor, send local operations to server & receive other clients operations from server. Apply *operational transformation* between lcoal operations
with received ones & render merged text to editor

### Demo
Open two client editors. Input text on the left.

<img src="img/input-one-side.png" height="360">

Wait a second, right editor will be synchronized.

<img src="img/make-consistent.png" height="360">

Input something into left editor.

<img src="img/add-left.png" height="360">

Wait a second, right editor will be synchronized again.

<img src="img/sync-right.png" height="360">

Insert into arbitrary positon on right.

<img src="img/add-right.png" height="360">

Left editor will also be synchronized in a second.

<img src="img/sync-left.png" height="360">
