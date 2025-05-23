# Replication-based-Fault-Tolerant-Server-against-Arbitrary-Failures
This repo contains Java codes [Server.java], [FaultyServer.java], [FaultTolerantServer.java] and [Client.java], which are used to implement fault-tolerance based on replication, for a simple client-server protocol. The fault-tolerant server can tolerate arbitrary failures occurring at 1 server replica. 

In the client-server protocol, the server sends an OK message periodically (every 30 seconds) to the client. The client counts the number of OK messages received from the server. [Client.java] and [Server.java] implement this protocol. [FaultyServer.java] sends an OK message at an arbitrary time. [FaultTolerantServer.java] has multiple server threads one of which being faulty, and it tolerates the faulty thread by utilizing the other threads that are working properly.

Details of the algorithm can be found in [Fault Tolerant Server Algorithm Explanation.pdf]. 
