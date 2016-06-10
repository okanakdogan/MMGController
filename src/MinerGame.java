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

public class MinerGame extends BasicGame
{
    private GameServer gs;
    Image background;
    private ArrayList<Player> players;
    private Mine[] mines;
    private Mine.MineType[] mine_types;
    private Timer mineTimer;
    private ArrayList<Image> playerImages;


    private ArrayList<Image> mineImages;

    public static int screen_width=1024;
    public static int screen_height=640;
    public static int MINE_LIMIT =50;
    private Random random;
    private int imgIndex=0;

    public MinerGame(String gamename)
    {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {


        playerImages= new ArrayList<Image>();
        mineImages = new ArrayList<Image>();

        background = new Image("resources/miner-background.png");
        Image temp = new Image("resources/navigation-icon.png");
/*
        Image coal = new Image("resources/coal-s.png");
        Image silver = new Image("resources/silver-s.png");
        Image gold = new Image("resources/gold-s.png");
*/
        mineImages.add(new Image("resources/coal-s.png"));
        mineImages.get(mineImages.size()-1).setName("coal");
        mineImages.add(new Image("resources/silver-s.png"));
        mineImages.get(mineImages.size()-1).setName("silver");
        mineImages.add(new Image("resources/gold-s.png"));
        mineImages.get(mineImages.size()-1).setName("gold");

        playerImages.add(new Image("resources/burak-miner.png"));
        playerImages.add(new Image("resources/nurdan-miner.png"));
        playerImages.add(new Image("resources/yusuf-miner.png"));
        playerImages.add(new Image("resources/berkay-miner.png"));
        System.out.println( playerImages.size());

        mines = new Mine[MINE_LIMIT];
        mine_types =Mine.MineType.values();
        mineTimer = new Timer();
        mineTimer.schedule(new minerCreateTask(),5000);


        players = new ArrayList<Player>();
        random= new Random();
        gs=new GameServer(7778,7797);
        gs.setGame(this);
        gs.start();


    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {

        //other threads handles update

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
                g.drawString(p.getName(),pos.getX()+20,pos.getY()-15);

                p.getPlayerImage().draw(pos.getX(),pos.getY(),
                        p.getImgWidth(),p.getImgHeight());
                //draw score
                g.drawString(String.valueOf(p.getPoints()),pos.getX()+35,pos.getY()+p.getImgHeight()-10);
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

        String hostaddress= "";

        if(current_addr!=null)
            hostaddress=current_addr.toString();

        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new MinerGame("Miner Game "+hostaddress));
            appgc.setDisplayMode(screen_width, screen_height, false);
            appgc.start();

        }
        catch (SlickException ex)
        {
            Logger.getLogger(MinerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Player addPlayer(int uniqeid){


        Player p = new Player("p-"+uniqeid,this);
        p.setPosition(giveRandomPositionFromScreen());

        players.add(p);
        return p;
    }

    public void removePlayer(String name){

        for(int i=0; i<players.size(); ++i){
            if(players.get(i).getName().equalsIgnoreCase(name))
                players.remove(i);
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
        //random.setSeed(System.currentTimeMillis());
        return new Point(random.nextInt((screen_width*3)/4),random.nextInt((screen_height*3)/4));
    }


    private class minerCreateTask extends TimerTask{

        @Override
        public void run() {
            synchronized (mines){
                for (int m=0; m<MINE_LIMIT; ++m ){
                    if(mines[m]==null){
                        mines[m]= new Mine( mine_types[random.nextInt(mine_types.length)],giveRandomPositionFromScreen(),mineImages);
                        //System.out.println("Mine added");
                        break;
                    }
                }
            }
            mineTimer.schedule(new minerCreateTask(),500);
        }
    }

    public ArrayList<Image> getPlayerImages() {
        return playerImages;
    }
    public ArrayList<Image> getMineImages() {
        return mineImages;
    }

    public int giveNextImageIndex(){

        if(imgIndex<playerImages.size()-1){
            imgIndex++;
        }else{
            imgIndex=0;
        }
        return imgIndex;
    }

}