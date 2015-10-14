import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.*;

public class server implements Runnable{
    protected ServerSocket  serverSocket    = null;
    protected static int    port            = 23657;
    protected boolean       isStopped       = false;
    protected Thread        runningThread   = null;

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(port);
         while(true){
             Socket clientSocket = serverSocket.accept();
             System.out.println("Connected Succesfully.");
         }
    }
    
    public void run() {
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isStopped()){
            Socket clientSocket = null;
            try{
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()){
                    System.out.println("The server has stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(new WorkerRunnable(clientSocket, "Multithreaded Server")).start();
        }
        System.out.println("The server has stopped.");
    }

    private synchronized boolean isStopped(){
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try{
            this.serverSocket.close();
        } catch (IOException e){
            throw new RuntimeException("Error closing the server", e);
        }
    }
     private void openServerSocket(){
         try{
             this.serverSocket = new ServerSocket();
         } catch (IOException e){
             throw new RuntimeException("Cannot open the port.", e);
         }
     }
}
