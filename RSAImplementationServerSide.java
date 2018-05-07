/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsaimplementationserverside;

/**
 *
 * @author MehmetSavasci
 */

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Scanner;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
 
public class RSAImplementationServerSide
{
    //initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream in       = null;   
    
    private JEditorPane jep          = null;
    private FileWriter fw            = null;
    private File file                = null;
    private JPanel panel             = null;
    private JFrame f                 = null;  
    private Dimension screenSize     = null;
    private URL url                  = null;
    private JScrollPane jsp           = null;
            
    // constructor with port
    public RSAImplementationServerSide(int port)
    {               
        // starts server and waits for a connection
        try
        {                              
            server = new ServerSocket(port);
            System.out.println("Server started");
 
            System.out.println("Waiting for a client ...");
 
            socket = server.accept();
            System.out.println("Client accepted");
            //WriteToTheFile("Client accepted");
 
            // takes input from the client socket
            in = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));
 
            String line = "";            
 
            // reads message from client until "Over" is sent
            while (!line.equals("Over"))
            {
                try
                {
                    line = in.readUTF();
                    //System.out.println(line);                    
                    
                    byte[] decrypted = decrypt(line);
                    
                    String decrypted_message = new String(decrypted);
                    
                    System.out.println("Decrypted Message: " + decrypted_message);
                    //WriteToTheFile(decrypted_message);  
                         
                    jep = new JEditorPane();                                        
                    jep.setEditable(false);
                    
                    f = new JFrame(decrypted_message);
                    //panel = new JPanel();                                             
                    jsp = new JScrollPane(jep);
                    //f.setContentPane(panel);
                    f.getContentPane().add(jsp);
                    screenSize = Toolkit.getDefaultToolkit().getScreenSize();                    
                    //f.setPreferredSize(dimension);
                    f.setSize(screenSize);
                    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                    
                    f.setVisible(true);   
                    
                    //String tmp = toURL(decrypted_message.trim());
                    String tmp = decrypted_message.trim();

                    /*if (tmp == null) {
                        tmp = toURL("http://" + decrypted_message.trim());
                    }
                    
                    try {
                        jep.setPage(tmp);
                    }
                    catch (IOException e) {
                        jep.setContentType("text/html");
                        jep.setText("<html>Could not load " + line + "</html>");
                    }                                     
                                                                                                                     
                }
                catch(IOException i)
                {                    
                    System.out.println(i);
                }
            }
            
            System.out.println("Closing connection");
 
            // close connection
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {        
        //System.out.println("Enter the port number: ");
        //Scanner in_port = null;
        //int port = in_port.nextInt();
        
        int length = args.length;   
        RSAImplementationServerSide server = null;
        
        try {
            if (length > 0) {                        
                server = new RSAImplementationServerSide(Integer.valueOf(args[0])); 
            } 
        } catch(NumberFormatException e) {
            System.out.println(e);
        }                   
        
        //RSAImplementationServerSide server = new RSAImplementationServerSide(8020); 
    }
    
    private void WriteToTheFile(String coming) {
        String path = "C:\\Users\\MehmetSavasci\\Desktop\\log.txt";
        //String path1 = "C:\\Users\\Duygu_Mehmet\\Desktop\\socket.txt";
        file = new File(path);
        try {
            if (file.exists()) {
                fw = new FileWriter(file, false);
                BufferedWriter wrt = new BufferedWriter(fw);
                wrt.write(coming);
                wrt.newLine();                
                wrt.flush();
                wrt.close();
            } else {
                file.createNewFile();
                fw = new FileWriter(file, false);
                BufferedWriter wrt = new BufferedWriter(fw);
                wrt.write(coming);
                wrt.newLine();                
                wrt.flush();
                wrt.close();
            }
        } catch (IOException error) {
            System.out.println("The operation of writing to the file couldn't be done.");
        }
    }
    
    private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
            return null;
        }
    }  
    
    // Decrypt message

    public byte[] decrypt(String arriving_message)

    {                                                                   
        String coming_message = arriving_message.substring(0, 344);
        
        String N_info = arriving_message.substring(344, 961);
        
        String d_private_key_length = arriving_message.substring(arriving_message.length()-3, arriving_message.length());
                        
        String d_private_key = arriving_message.substring(arriving_message.length()-Integer.valueOf(d_private_key_length)-3, arriving_message.length()-3);
        
        //String coming_message = arriving_message.substring(0, arriving_message.length()-Integer.valueOf(d_private_key_length)-3);                                
        
        byte[] message = Base64.getDecoder().decode(coming_message);
        
        System.out.println("Receiving encrypted message: " + message);   
        System.out.println("Length of the coming encrypted message: " + message.length);
        
        System.out.println("Private key length (in terms # bit): " + new BigInteger(d_private_key).bitCount());
        
        //return (new BigInteger(message)).modPow(d, N).toByteArray();
        return (new BigInteger(message)).modPow(new BigInteger(d_private_key), new BigInteger(N_info)).toByteArray();
    }
}
