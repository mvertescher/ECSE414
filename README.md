ECSE414
=======


For simulation, add your AVDs to the Configuration file and some ports that are available on your host machine.

Launch your AVDs and the server (there's a main method in Server.java). Then 

telnet localhost <avdconsoleport>
redir add tcp:<the port you put in the config>:5001

Then launch the application on the avds.


