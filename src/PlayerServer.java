import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Okan on 7.4.2016.
 */
public class PlayerServer implements Runnable {



    private int playerPort; //Dedicated port for mobile controller
    private ServerSocket serverSocket; //connection listeber server socket
    private Socket clientSocket;
    private PrintWriter c_out;
    private BufferedReader c_in;
    private GameServer gServer;
    private Player player;
    private ConcurrentLinkedQueue<String> events;

    public PlayerServer( int port , GameServer gs) throws IOException {
        playerPort = port;
        this.gServer = gs;

        serverSocket=new ServerSocket(playerPort);
        serverSocket.setSoTimeout(10000);
        events=gs.getEvents();
        //System.out.println(serverSocket.getInetAddress().toString());

    }

    public void start(){

        new Thread(this,"PlayerServer-"+playerPort).start();
    }

    @Override
    public void run() {

        String clientInput="";

        while(true){
            if(clientSocket == null ){
                try {

                    //Listen Port
                    System.out.println("client waiting");
                    clientSocket=serverSocket.accept();

                    //get reader writer
                    System.out.println("player client connected");
                    c_out= new PrintWriter(clientSocket.getOutputStream(),true);
                    c_in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    //request for player instance from game
                    player = gServer.getGame().addPlayer(playerPort);

                    serverSocket.close();
                    serverSocket=null;

                }catch (SocketTimeoutException se){
                    //player not connected
                    //se.printStackTrace();
                    //tell game to remove player
                    if(player!=null)
                        gServer.getGame().removePlayer(player.getName());
                    //tell gameServer to remove me
                    gServer.removePlayerSever(this);
                    System.err.println("p removed");

                    break;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{

                try {
                    //take information
                    //System.out.println(c_in.readLine());
                    clientInput=c_in.readLine();

                    if(clientInput==null){
                        clientSocket.close();
                        clientSocket = null;
                        break;
                    }

                    if(clientInput != null && events!=null){

                        //add evetqueue
                        //events.offer(clientInput);
                       player.handleInput(clientInput);
                        //TIME TEST
                        if (clientInput.equalsIgnoreCase("testtime")){
                           // System.out.println("testback");
                            c_out.println("testback");}
                        //inputandler.handleInput(clientInput);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    if(player!=null)
                        gServer.getGame().removePlayer(player.getName());
                    //tell gameServer to remove me
                    gServer.removePlayerSever(this);
                    System.err.println("p removed");
                    break;

                }
            }
        }

        try {
            if(serverSocket!=null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocket=null;
    }
    public int getPlayerPort() {
        return playerPort;
    }
}
