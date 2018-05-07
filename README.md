# Java_Swing_Socket_Programming

This application encrypts your sending website address by using RSA cryptography algorithm on client side and encrypted message is decrypted on server side and decrypted message is showed on GUI. Your sending website address format should be like

https://github.com/

To run the codes, you need to have jre (http://www.oracle.com/technetwork/java/javase/downloads/jre10-downloads-4417026.html).

You must run Server Application firstly. To run it, if you use Windows OS, go to the cmd and change your current folder to the folder where Server Aplication is. Then write command format type

java -jar RSAImplementationServerSide port_number

After you run Server Application, you can run Client Application. To run it, 

java -jar RSAImplementationClientSide ip_address_of_the_machine_where_server_application_is port_number

Note that you have to use same port number both the server and the client application.
