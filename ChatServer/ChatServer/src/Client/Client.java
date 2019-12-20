package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
    private static DataOutputStream dos;
    private static DataInputStream dis;
    final static int ServerPort = 8080;
    Socket s = null;

    public Client(int ServerPort)
    {
        Scanner scn = new Scanner(System.in);

        // getting localhost ip
        try {
            InetAddress ip = InetAddress.getByName("127.0.0.1");


            // establish the connection
            s = new Socket(ip, ServerPort);

            // obtaining input and out streams
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            if(dis != null){
                readMessage();
                sendMessage(scn);
            }

        } catch (Exception e) {
            System.err.println("wrong port");
            //e.printStackTrace();
        }


    }

    public void sendMessage(Scanner scn){
        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
                    String msg = scn.nextLine();
                    try {
                        // write on the output stream
                        dos.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sendMessage.start();
    }
    public void readMessage(){
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (true) {
                    try {
                        // read the message sent to this client
                        if(dis.available()>0 && dis!=null){
                            String msg = dis.readUTF();

                            if(!msg.equals("Update List")){
                                System.out.println(msg);
                            }
                            if(msg.equals("QUIT")){
                                s.close();
                                dis.close();
                                dos.close();
                                System.exit(0);
                                //break;
                            }
                        }

                    } catch (IOException e) {

                        //System.err.println(e);
                        e.printStackTrace();
                    }
                }
            }
        });
        readMessage.start();
    }


    public static void main(String args[])
    {
        Client client = new Client(ServerPort);
    }
}
