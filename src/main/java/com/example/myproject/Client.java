package com.example.myproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client extends Thread {
    private int port;
    private String ip;
    private int timeout;
    private Socket clientSocket; //the client socket
    private DataOutputStream out; // sends output to the socket
    private BufferedReader in; // // takes input from terminal
    private boolean isFinished;
    private String response;
    private User user;
    private byte[] data;


    public Client(User user) {
        this.port = 1000;
        this.ip = "192.168.1.110";
        this.timeout = 5000;
        this.isFinished = false;
        this.response = "";
        this.user = user;
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(this.ip, this.port), this.timeout); // create connection
            out = new DataOutputStream(clientSocket.getOutputStream()); // output what we send
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // input what we recive
            out.writeUTF(user.getUsername());
            out.flush();
            out.writeUTF(user.getPassword());
            out.flush();
            this.response = in.readLine();
            Log.d("server", "server response: " + this.response);
            this.isFinished = true;
            clientSocket.close();
            in.close();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        super.run();
    }


    public boolean isFinished() {
        return this.isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}

