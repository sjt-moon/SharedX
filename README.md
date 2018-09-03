# SharedX
## A Collaborative shared text editor

### Technologies
* Spring
* MongoDB
* Netty
* Vaadin
* Operational Transformation

### Structure
#### Server
Back-end, exchange client local operations

#### Client
Editor, send local operations to server & receive other clients operations from server. Apply *operational transformation* between lcoal operations
with received ones & render merged text to editor
