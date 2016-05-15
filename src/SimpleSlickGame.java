/**
 * Created by Okan on 8.3.2016.
 */
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Point;

public class SimpleSlickGame extends BasicGame
{
    private GameServer gs;
    Image background;
    Image[] playerImg;
    private ArrayList<Player> players;

    public static int screen_width=1024;
    public static int screen_height=640;
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
        yusufImage = new Image("resources/yusufps.png");
        playerImg= new Image[playerImgSize];

        for (int i=0;i< playerImgSize;++i){
            playerImg[i]= new Image("resources/player_img"+(i+1)+".png");
        }

        players = new ArrayList<Player>();
        random= new Random();
        gs=new GameServer(7778,7797);
        gs.setGame(this);
        gs.start();


    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {

        int handleNum=30;
        String e=null;
        //handle events
        /*
        for (;handleNum>0;--handleNum){
            //TODO write handler for Messages
            e=gs.getEvents().poll();

            if(e!=null){
               handleMessage(e);
            }
        }*/
        gs.getEvents().poll();
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
        yusufImage.draw(50,50,0.2f);
        for (Player p : players){
            if(p!=null){

                p.getPlayerImage().draw(p.getPosition().getX(),p.getPosition().getY(),
                        p.getImgWidth(),p.getImgHeight());
            }
        }
    }

    public static void main(String[] args)
    {
        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new SimpleSlickGame("Simple Slick Game"));
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
        p.setPosition(new Point(random.nextInt(screen_width-30),random.nextInt(screen_height-30)));
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

    /*
    public Player playerRequest( int request){
        if(request == 0){
            //create
            return addPlayer();

        }else if(request == 1){
            //delete
            return null;
        }
        return null;
    }*/
}