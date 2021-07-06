package com.erivalaxl.server;

import java.io.*;
import java.net.Socket;

public class UserThread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }
    public void run(){
        try{
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader((input)));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);
            
            String serverMessage = "New user connected: "+userName;
            server.broadcast(serverMessage, this);
            
            String clientMessage ;
            do{
                clientMessage = reader.readLine();
                serverMessage = "["+userName+"]: "+clientMessage;
                server.broadcast(serverMessage, this);
                
            }while (!clientMessage.equals("bye"));
            
        }catch(IOException e){
            System.out.println("Error in user thread: "+e.getMessage());
            e.printStackTrace();
        }
    }

    public void printUsers() {
        if(server.hasUsers()){
            writer.println("Connected users "+server.getUserNames());
        } else{
            writer.println("No other users connected");
        }
    }
    
    public  void sendMessage(String message){
        writer. println(message);
    }

    public void start() {
    }
}
