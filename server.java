import java.io.*;
import java.net.*;
import java.util.logging.*;

class server implements Runnable{
    //Constructor
    public server(Socket clientSocket){
        this.clientSocket = clientSocket;
    }
    protected ServerSocket serverSocket     = null;
    protected static int port               = 23657;
    protected Socket clientSocket           = null;

    public static void main(String argv[]) throws Exception{
        ServerSocket serverSocket = new ServerSocket(port);

        while(true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connected Successfully.");
            (new Thread(new server(clientSocket))).start();
        }
    }

    public void run(){
        String input;
        int MAX = 1024;
        int fence = MAX/2;
        int number = 0;
        int increment = MAX/2;

        try{
            DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            dataOut.writeBytes("\nEnter y for yes, n for no, or q for quit.\n\n");
            dataOut.writeBytes("Pick a number between 1 and " + MAX + ".\n");
            while(increment > 0){
                dataOut.writeBytes("Is your number greater than " + fence + "?\n");
                input = dataIn.readLine();
                input = input.substring(0, 1);
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
            number++;
            dataOut.writeBytes("Your number is " + number + "\n");
            clientSocket.close();
        } catch (IOException e){
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
