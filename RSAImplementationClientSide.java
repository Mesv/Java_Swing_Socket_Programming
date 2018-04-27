/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsaimplementationclientside;

/**
 *
 * @author MehmetSavasci
 */

import java.net.*;

import java.io.*;

import java.io.IOException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import java.util.Random;

import java.util.Scanner;
 
public class RSAImplementationClientSide
{
    // initialize socket and input output streams
    private Socket socket            = null;    
    private DataOutputStream out     = null;
    private Scanner in_ip            = null;
    private Scanner in_port          = null;
    private Scanner in_plainText     = null;
    
    // initialize RSA parameters
    private BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;
    private int bitlength = 1024;
    private Random r;     
    private String sending_message;
 
    // constructor
    public RSAImplementationClientSide()
    {
        r = new Random();

        p = BigInteger.probablePrime(bitlength, r);

        q = BigInteger.probablePrime(bitlength, r);

        N = p.multiply(q);

        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        e = BigInteger.probablePrime(bitlength / 2, r);

        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {

            e.add(BigInteger.ONE);
        }   
        
        d = e.modInverse(phi);
    }
 
    public static void main(String args[])
    {
        RSAImplementationClientSide client = new RSAImplementationClientSide();
        
        // Using Scanner for Getting Input from User
        /*client.in_ip = new Scanner(System.in);
        
        System.out.println("Enter the ip address: ");
        String ip = client.in_ip.nextLine();
        System.out.println("You entered ip: " + ip);*/
        
        /*client.in_port = new Scanner(System.in);
        
        System.out.println("Enter the port number: ");
        int port = client.in_port.nextInt();
        System.out.println("You entered port: " + port);*/
        
        int length = args.length;                                                            
        
        // establish a connection
        try
        {
            if (length > 0) {                        
                client.socket = new Socket(args[0], Integer.valueOf(args[1]));
                System.out.println("Connected");
 
                System.out.println("Enter the plain text:");
            
                // takes input from terminal            
                client.in_plainText = new Scanner(System.in);
 
                // sends output to the socket
                client.out = new DataOutputStream(client.socket.getOutputStream());
            }                         
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
        
        // string to read message from input
        String line = "";
 
        // keep reading until "Over" is input
        while (!line.equals("Over"))
        {
            try
            {
                line = client.in_plainText.nextLine();
                
                System.out.println("Encrypting String: " + line);               

                // encrypt

                String encrypted = client.encrypt(line.getBytes(StandardCharsets.UTF_8));               
                
                //client.out.writeUTF(bytesToString(encrypted));
                client.out.writeUTF(encrypted);
                //client.out.writeBytes(line);                                
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        }
 
        // close the connection
        try
        {
            client.in_ip.close();
            client.in_port.close();
            client.in_plainText.close();
            client.out.close();
            client.socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }                                
    }
        
    // Encrypt message

    public String encrypt(byte[] message)

    {               
        byte[] encrypted_data = (new BigInteger(message)).modPow(e, N).toByteArray();
        
        System.out.println("Sending encrypted message: " + encrypted_data);
        System.out.println("Length of the encrypted message: " + encrypted_data.length);         
        
        String encrypted_data_string = Base64.getEncoder().encodeToString(encrypted_data);
        
        String private_key = String.valueOf(d);                                        
                
        System.out.println("Private key length (in terms # bit): " + d.bitCount());    
        
        StringBuilder result = new StringBuilder();
        result.append(encrypted_data_string).append(String.valueOf(N)).append(private_key).append(String.valueOf(private_key.length()));
        
        //sending_message = encrypted_data_string + String.valueOf(N) + private_key + String.valueOf(private_key.length());                      
        sending_message = result.toString();
        
        return sending_message;                

    }
}
