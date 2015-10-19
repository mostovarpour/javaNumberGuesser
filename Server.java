import java.io.*;
import java.net.*;
import java.util.logging.*;

public class Server implements Runnable{
    
	// Set the port to listen on
	protected static int port = 23657;
	
	// Each instance (thread) needs a reference 
	// to the client to talk to (the accepted socket) 
	protected Socket clientSocket;
	
	
	// IS THIS REALLY NEEDED??
	//protected ServerSocket serverSocket;
    
	
	
	// ADD IN THE SHUTDOWN HOOK CODE TO HANDLE POSSIBLE LEAK??????
	
	
	
    	
	// Constructor
    public Server(Socket clientSocket){
        
    	// Copy the socket object to the thread's reference
    	this.clientSocket = clientSocket;
    }
        
    // The main server runs this code
    public static void main(String argv[]) throws Exception{
        
    	// 
    	ServerSocket serverSocket = new ServerSocket(port);

    	// Keep accepting connections
        while(true){
        	
        	Socket clientSocket = serverSocket.accept();
            
            // DIAGNOSTIC MESSAGE >>>REMOVE ME<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            System.out.println("Connected Successfully.");
            
            // Desirable to create multiple threads in a safe way.
            // Each thread needs to get the reference to the socket object
            // before the server replaces the passed in object reference.
            
            // The Server class implements runnable, which makes it a runnable object.
            // This allows a thread to be created with the Server object.
            // First the Server class constructor copies the reference to the socket.
            // Once the thread is created, calling start() causes the thread to begin executing.
            (new Thread(new Server(clientSocket))).start();
        }
    }

    // When the thread is created, the .start() runs this code.
    public void run(){
    	
    	// Will be used to temporarily store the client's response
        String input = null;
        
        // Maximum size of number that can be chosen
        int MAX = 1024;
        
        // Initial conditions
        int fence = MAX/2;
        int number = 0;
        int increment = MAX/2;

        // This code could throw an IOException, so using the try/catch block 
        try{
            DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            dataOut.writeBytes("\nEnter y for yes, n for no, or q for quit.\n\n");
            
            dataOut.writeBytes("Pick a number between 1 and " + MAX + ".\n");
            
            while(increment > 0){
                dataOut.writeBytes("Is your number greater than " + fence + "?\n");
                
                input = dataIn.readLine();
                
                // If user presses Enter key with no input, input string is length zero
                // Math.max() here allows to grab the first char or null string
                input = input.substring(0, Math.min(1, input.length()));
                
                // Assume that user is following instructions
                // Input of q requests to quit.
                if ("q".equals(input)){
                    break;
                }
                else if ("y".equals(input)){
                    number += increment;
                    increment /= 2;
                    fence += increment;
                }
                else{
                    increment /= 2;
                    fence -= increment;
                }
            }
            
            // Last iteration ends up with number being short by one
            // due to initial restriction that the number must be at least one.
            // Worst case: number is one. Keep moving fence to the left,
            // number variable is never increased, number = 0, so need to increment by one.
            number++;
            
            // Final write to the client with the guessed number.
            dataOut.writeBytes("Your number is " + number + "\n");
                        
            // Close the socket before exiting
            clientSocket.shutdownOutput();
            clientSocket.close();
        } 
        catch (IOException e){
        	Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }    
    }
}