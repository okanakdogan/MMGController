import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Okan on 24.3.2016.
 */
public class GameServer implements Runnable {

    private int PORT=7777;

    private int portRangeStart;
    private int portRangeEnd;
    private int portIndex;
    private boolean[] portStatus;

    private Thread mythread;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter c_out;
    private BufferedReader c_in;
    private ArrayList<PlayerServer> pServers;
    private MinerGame game;

    //experimental

    private ConcurrentLinkedQueue<String> events;


    /*
    public GameServer(){

        try {
            serverSocket=new ServerSocket(PORT);
            //System.out.println(serverSocket.getInetAddress().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public GameServer(int startPort, int endPort){

        portRangeStart=startPort;
        portRangeEnd = endPort;
        portIndex=0;

        portStatus = new boolean[endPort - startPort];

        try {
            serverSocket=new ServerSocket(PORT);
            //System.out.println(serverSocket.getInetAddress().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        pServers=new ArrayList<PlayerServer>();
        events=new ConcurrentLinkedQueue<>();
    }

    public void start(){

        new Thread(this,"Server").start();
    }

    @Override
    public void run() {

        String clientInput = "";
        while(true){
            if(clientSocket == null){
                try {
                    System.out.println("client waiting");
                    clientSocket=serverSocket.accept();
                    System.out.println("client connected");
                    c_out= new PrintWriter(clientSocket.getOutputStream(),true);
                    c_in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{

                try {
                    // wait for request
                    for(int i=0; i<3; i++) {

                        //System.out.println(c_in.readLine());
                        clientInput = c_in.readLine();
                        System.out.println("c input: "+clientInput);
                        if(clientInput.equalsIgnoreCase("addP")){
                            // get a new port
                            int dedicatedPort = giveAport();
                            if(dedicatedPort==0){
                                c_out.println("sfull");
                                break;
                            }

                            PlayerServer pServer=null;
                            //start a player server
                            try {
                                pServer= new PlayerServer(dedicatedPort,this);
                            }catch (IOException e ){
                                e.printStackTrace();
                                System.err.println("Player Server couldn't start");
                            }

                            if(pServer!=null){

                                pServer.start();
                                pServers.add(pServer);

                                //send succes and dedicated port
                                c_out.println("s"+dedicatedPort);
                                System.out.println("new port sent");
                                portIndex++;
                                break;
                            }else{
                                c_out.print("fail");
                                portIndex++;
                            }
                        }else{
                            break;
                        }
                    }
                    clientSocket.close();
                    clientSocket=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private int giveAport(){

        for(int i=0;i< portStatus.length; ++i){
            if(portStatus[i]==false){
                portStatus[i]=true;
                return portRangeStart+i;
            }
        }
        return 0;
    }

    public ConcurrentLinkedQueue<String> getEvents() {
        return events;
    }

    public void setGame(MinerGame game){
        this.game=game;
    }

    public MinerGame getGame() {
        return game;
    }

    public void removePlayerSever(PlayerServer ps){
        pServers.remove(ps);
        portStatus[ps.getPlayerPort()-portRangeStart]=false;
       // portIndex--;
    }
}
