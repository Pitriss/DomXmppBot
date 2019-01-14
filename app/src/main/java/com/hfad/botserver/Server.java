package com.hfad.botserver;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Server {
    //MainActivity activity;
    ServerSocket serverSocket;
    String message = "";
    static final int socketServerPORT = 30666;
    private static Server ptr_tcp = null;
    private Context appContext;
    private TakePhoto photo;

    private MyOpenHelper db = null;

    /*private Server(MainActivity activity) {
        //this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }*/

    private Server() {

        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public void initDataBase(Context context, Activity activity)
    {
        appContext = context;
        db = new MyOpenHelper(context);
        photo = new TakePhoto(activity);
    }

    public String takePhoto()
    {
        Thread photoThread = new Thread(new takePicture());
        photoThread.start();
        try {
            photoThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return photo.getFileName();
    }

    private class takePicture extends Thread {
        @Override
        public void run() {
            photo.takePicture();

            while(photo.isPictureReady() == false)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void updateNodoDBInfo(Nodo nodo)
    {
        db.actualizar(nodo.getName(),nodo.getSensorName(),nodo.getActuadorName(),nodo.getMac());
    }

    public static Server getInstance()
    {
        if(ptr_tcp == null)
        {
            ptr_tcp = new Server();
        }
        return ptr_tcp;
    }

    public static void setInstance(Server instance)
    {
        ptr_tcp = instance;
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(socketServerPORT);

                while (true) {
                    Socket socket = serverSocket.accept();
                    count++;
                    message += "#" + count + " from "
                            + socket.getInetAddress() + ":"
                            + socket.getPort() + "\n";

                    SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(
                            socket, count);
                    socketServerReplyThread.start();

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        int cnt;

        SocketServerReplyThread(Socket socket, int c) {
            hostThreadSocket = socket;
            cnt = c;
        }

        @Override
        public void run() {
            OutputStream outputStream;
            InputStream inputStream;
            BufferedReader in;
            String msgReply = "Hello from Server, you are #" + cnt;

            try {
                outputStream = hostThreadSocket.getOutputStream();
                inputStream = hostThreadSocket.getInputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.print(msgReply);

                int bytesRead;
                String salida;
                byte[] buffer = new byte[1024];
                Nodo nodo = null;

                while ((bytesRead = inputStream.read(buffer)) != -1)
                {
                    buffer[bytesRead] = 0;
                    salida = new String(buffer,0,bytesRead);
                    String[] split = salida.split(" ");
                    if((split[0].equalsIgnoreCase("/hola\n") || (split[0].equalsIgnoreCase("/hola")) && (nodo == null)))
                    {
                        String mac = String.valueOf(cnt);
                        if(split.length != 1){
                            mac = split[1];
                        }

                        if(db != null) {
                            nodo = db.getNodoFromMac(mac, (PrintStream) printStream);
                        }

                        if(nodo == null) {
                            nodo = new Nodo("Nodo" + String.valueOf(cnt),mac, (PrintStream) printStream);
                            db.insertar(nodo.getName(),nodo.getSensorName(),nodo.getActuadorName(),mac);
                        }

                        if(Nodos.getInstance().saveNodo(cnt,nodo))
                        {
                            //printStream.print("Nodo salvado");
                        }
                        else
                        {
                            nodo = Nodos.getInstance().getNodo(cnt);
                            //printStream.print("Nodo ya existe");
                        }
                    }
                    else if(nodo != null)
                    {
                        nodo.processData(salida);
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(Nodos.getInstance().deleteNodo(cnt))
                {
                    System.out.println("Nodo borrado!");
                }
                else
                {
                    System.out.println("Nodo error!");
                }
                System.out.println("ESTOY AFUERA, ESPERAME!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
}