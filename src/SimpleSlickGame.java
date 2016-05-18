/**
 * Created by Okan on 8.3.2016.
 */
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Point;

public class SimpleSlickGame extends BasicGame
{
    private GameServer gs;
    Image background;
    Image[] playerImg;
    private ArrayList<Player> players;
    private Mine[] mines;
    private Mine.MineType[] mine_types;
    private Timer mineTimer;


    public static int screen_width=1024;
    public static int screen_height=640;
    public static int MINE_LIMIT =50;
    private Random random;

    private Image yusufImage;

    public SimpleSlickGame(String gamename)
    {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {

        int playerImgSize=3;
        background = new Image("resources/miner-background.png");
        Image temp = new Image("resources/navigation-icon.png");
        temp = new Image("resources/coal-s.png");
        temp = new Image("resources/silver-s.png");
        temp = new Image("resources/gold-s.png");
        temp = new Image("resources/burak-miner.png");
        yusufImage = new Image("resources/mineryusuf.png");
        playerImg= new Image[playerImgSize];

        mines = new Mine[MINE_LIMIT];
        mine_types =Mine.MineType.values();
        mineTimer = new Timer();
        mineTimer.schedule(new minerCreateTask(),5000);
/*
        for (int i=0;i< playerImgSize;++i){
            playerImg[i]= new Image("resources/player_img"+(i+1)+".png");
        }*/

        players = new ArrayList<Player>();
        random= new Random();
        gs=new GameServer(7778,7797);
        gs.setGame(this);
        gs.start();


    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {


        //random rock generate


        //calculate scores

    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        //g.drawString("Howdy!", 20, 20);
        //compass_nav.draw(80,80);
        //background
        //background.draw();
        background.draw(0,0,screen_width,screen_height);
        //yusufImage.draw(450,300,0.2f);

        g.drawString("Oyuncu Sayisi: "+ players.size(),10,30);

        synchronized (mines){
            for (Mine m: mines){
                if(m!=null){
                    Point pos = m.getPosition();
                    m.getMineImage().draw(pos.getX(),pos.getY(),
                            m.getImgWidth(),m.getImgHeight());
                }
            }
        }

        for (Player p : players){
            if(p!=null){
                //position
                Point pos = p.getPosition();
                //draw name
                g.drawString(p.getName(),pos.getX()+20,pos.getY()-5);

                p.getPlayerImage().draw(pos.getX(),pos.getY(),
                        p.getImgWidth(),p.getImgHeight());
            }
        }
    }

    public static void main(String[] args)
    {
        //find your local ip
        NetworkInterface current=null;
        Enumeration<NetworkInterface> interfaces = null;
        InetAddress current_addr=null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                current = interfaces.nextElement();
                //System.out.println(current);
                if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
                Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements()){
                    current_addr = addresses.nextElement();
                    if (current_addr.isLoopbackAddress()) continue;
                    System.out.println(current_addr.getHostAddress());
                    if(current_addr.getHostAddress().toString().contains(".")){
                        System.out.println("local ip" +current_addr.getHostAddress());
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new SimpleSlickGame("Miner Game "+(current_addr.getHostAddress())));
            appgc.setDisplayMode(screen_width, screen_height, false);
            appgc.start();

        }
        catch (SlickException ex)
        {
            Logger.getLogger(SimpleSlickGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Player addPlayer(int uniqeid){


        Player p = new Player("p-"+uniqeid);
        p.setPosition(giveRandomPositionFromScreen());
        p.setGame(this);
        players.add(p);
        return p;
    }

    public void removePlayer(String name){

        for(int i=0; i<players.size(); ++i){
            if(players.get(i).getName().equalsIgnoreCase(name))
                players.remove(i);
        }

    }

    public void handleMessage(String e){

        //parse
        String[] parts= e.split("-");
        // which player-command
        String pname=parts[0];
        String plyid=parts[1];
        String command=parts[2];

        //find player
        Player p = findPlayerByName(players,pname+"-"+plyid);

        if(p!=null){
            //handle command
            if (command.charAt(0)=='d'){
                //get vector comp.
                String[] comps = command.split("/");
                Point pnt= p.getPosition();
                p.setPosition(pnt.getX()+ Float.parseFloat(comps[0]),
                        pnt.getY()+ Float.parseFloat(comps[1]));
            }else if(command.charAt(0)=='b'){
                int bindex = Character.getNumericValue(command.charAt(1));

            }
        }

    }

    private Player findPlayerByName(ArrayList<Player> plist,String name){

        for (Player p:plist){

            if(p.getName().equalsIgnoreCase(name))
                return p;
        }
        return null;
    }

    public Mine[] getMines() {
        return mines;
    }

    private Point giveRandomPositionFromScreen(){
        random.setSeed(System.currentTimeMillis());
        return new Point(random.nextInt(screen_width-50),random.nextInt(screen_height-50));
    }


    private class minerCreateTask extends TimerTask{

        @Override
        public void run() {
            synchronized (mines){
                for (int m=0; m<MINE_LIMIT; ++m ){
                    if(mines[m]==null){
                        mines[m]= new Mine( mine_types[random.nextInt(mine_types.length)],giveRandomPositionFromScreen());
                        //System.out.println("Mine added");
                        break;
                    }
                }
            }
            mineTimer.schedule(new minerCreateTask(),3000);
        }
    }

}