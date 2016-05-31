import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

import java.util.Random;

/**
 * Created by Okan on 8.4.2016.
 */
public class Player implements inputHandler{



    private int plyid;  //Player id
    private String name;    //Player name
    private Image pImage;   //Player Character Image
    private int imgHeight;
    private int imgWidth;
    private float scaleRatio;

    private float orient_x=0;   // x component of direction vectro which comes from controller
    private float orient_y=0;   // y comp.

    private Point position;     //character position
    private boolean holdStatus; // hold status for character to take mines
    private int points;
    private MinerGame game;

    /**
     * Constructer of Player
     */
    public Player( String playerName,MinerGame game){
        //random player image load

        //pImage = new Image("resources/mineryusuf.png");
        //pImage = loadPlayerImage(new Random().nextInt(3));
        name=playerName;
        this.game=game;
        pImage=game.getPlayerImages().get(new Random().nextInt(3));
        scaleRatio =0.5f;
        imgHeight =(int) (pImage.getHeight()* scaleRatio);
        imgWidth=(int)(pImage.getWidth()* scaleRatio);
        points=0;

        position=new Point(80,80);
    }



    /**
     * name getter
     * @return name of player
     */
    public String getName() {
        return name;
    }

    /**
     * gets Player Image
     * @return Image
     */
    public Image getPlayerImage(){
        return pImage;
    }

    /**
     * Rotates player Image
     */
    public void updateImageOrient(){
        //updates image orient with orient vals
        pImage.rotate(pImage.getRotation()-orient_x);
    }

    /**
     * Getter Player position
     * @return
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Setter Player position
     * @param position
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Setter Player position
     * @param x value for x axis
     * @param y valur for y axis
     */
    public void setPosition(float x, float y){

        this.position.setX(x);
        this.position.setY(y);
    }

    /**
     * prints message which comes from mobile controller
     * @param in
     */
    public void printAction(String in){

        System.out.println(name + " player message - "+in);
    }


    public void pickUpMine(){

        //check is there mine
        float radius=50;
        Point center = new Point(position.getX()+imgWidth/2, position.getY()+imgHeight/2);
        synchronized (game.getMines()){
            Mine[] mlist= game.getMines();
            for(int m=0; m< mlist.length;++m){
                if(mlist[m]==null)
                    continue;

                Point minePos = mlist[m].getPosition();
                float dist = (float)Math.sqrt(Math.pow( minePos.getX()-center.getX(),2) + Math.pow(minePos.getY()-center.getY(),2));
                if( dist<radius){
                    //take it
                    points += mlist[m].getValue();
                    //remove mine
                    mlist[m] = null;
                    break;
                }

            }
        }

    }

    /**
     *
     * Takes action based on message which comes from Mobile Controller
     * @param input
     */
    @Override
    public void handleInput(String input) {
        String[] vals = input.split("/");
        float sensivity=0.4f;

        // if message contains direction vector
        if(vals[0].equalsIgnoreCase("d")){
            orient_x= Float.parseFloat(vals[1])*sensivity;
            orient_y= Float.parseFloat(vals[2])*sensivity;

            //setPosition
            float  X=getPosition().getX()-orient_x;
            float Y=getPosition().getY()-orient_y;
            if (X >=0 && X< MinerGame.screen_width-imgWidth)
                getPosition().setX(X);
            if(Y >=0 && Y< MinerGame.screen_height- imgHeight)
                getPosition().setY(Y);

        }
        // message contains button pressed information
        else if(vals[0].equalsIgnoreCase("bp")){
            //button pressed
            //(vals[1]);
            holdStatus=true;
        }
        //Message contains button released information
        else if(vals[0].equalsIgnoreCase("br")) {
            //button released
            //printAction(vals[1]);
            holdStatus=false;
            //call action
            if(vals[1].equals("1"))
                pickUpMine();
        }
        //Message contains setname
        else if(vals[0].equalsIgnoreCase("setN")){
            name= vals[1];
        }

        //updateImageOrient();
    }

    /**
     * gets Player ımage height
     * @return
     */
    public int getImgHeight() {
        return imgHeight;
    }

    /**
     *
     * @return
     */
    public int getImgWidth() {
        return imgWidth;
    }

    public void setGame(MinerGame game) {
        this.game = game;
    }

    private Image loadPlayerImage(int index) throws SlickException {
        if(index>=3)
            index=0;
        if(index==0){
            return new Image("resources/berkay-miner.png");
        }else if(index==1){
            return new Image("resources/burak-miner.png");
        }else if(index==2){
            return new Image("resources/yusuf-miner.png");
        }else
            return null;
    }

    public int getPoints() {
        return points;
    }
}
