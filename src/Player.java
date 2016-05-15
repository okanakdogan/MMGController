import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

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

    /**
     * Constructer of Player
     */
    public Player(){
        try {
            //random player image load

            //pImage = new Image("resources/player_img"+(new Random().nextInt(2)+1)+".png");
            pImage = new Image("resources/yusufps.png");
            scaleRatio =0.2f;
            imgHeight =(int) (pImage.getHeight()* scaleRatio);
            imgWidth=(int)(pImage.getWidth()* scaleRatio);
        } catch (SlickException e) {
            e.printStackTrace();
            pImage=null;
        }
        position=new Point(80,80);
    }

    /**
     * Constructor with name parameter
     * @param playerName
     */
    public Player(String playerName){
        this();
        name=playerName;
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

    /**
     *
     * Takes action based on message which comes from Mobile Controller
     * @param input
     */
    @Override
    public void handleInput(String input) {
        String[] vals = input.split("/");
        float sensivity=0.5f;

        // if message contains direction vector
        if(vals[0].equalsIgnoreCase("d")){
            orient_x= Float.parseFloat(vals[1])*sensivity;
            orient_y= Float.parseFloat(vals[2])*sensivity;

            //setPosition
            float  X=getPosition().getX()-orient_x;
            float Y=getPosition().getY()-orient_y;
            if (X >=0 && X<SimpleSlickGame.screen_width-imgWidth)
                getPosition().setX(X);
            if(Y >=0 && Y<SimpleSlickGame.screen_height- imgHeight)
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
            holdStatus=true;
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
}
