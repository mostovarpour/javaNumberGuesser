import java.io.*;
import java.net.*;

public class Client{
    protected static int port = 23657;

    public static void main(String argv[]) throws Exception{
        String input;
        String[] inputSplit;
        int fence=0;
        
        // Create the socket to connect to server
        Socket clientSocket = new Socket("localhost", port);
        
        // Create the read and write streams
        DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader dataIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()), 10000);
        
        while(true){
            input = dataIn.readLine();
            System.out.println(input);
            
            if(input.length() > 14 && input.substring(0, 14).equals("Your number is")){
            	break;
            }
            
            // Extract the fence from the server message
            inputSplit = input.split("Is your number greater than ");
            
            if(inputSplit.length > 1){
            	//fence = Integer.parseInt(inputSplit[1]);
            	System.out.println(inputSplit[1]);
            }
            //System.out.println(inputSplit.length);
            
            //System.out.println(fence);
            
            Thread.sleep(100);
            dataOut.writeBytes("y\n");
        }
        
        clientSocket.close();
        
        
    }
}
