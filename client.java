import java.io.*;
import java.net.*;
import java.util.logging.*;

class client{
    protected static int port   = 23657;

    public static void main(String argv[]) throws Exception{
        int counter       = 10;
        String input;
        Socket clientSocket = new Socket("localhost", port);
        DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader dataIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        while(counter > 0){
            input = dataIn.readLine();
            System.out.println(input);
        }
        clientSocket.close();
    }
}
