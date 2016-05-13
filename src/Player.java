import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

/**
 * Created by Okan on 8.4.2016.
 */
public class Player implements inputHandler{

    public String getName() {
        return name;
    }

    private int plyid;
    private String name;
    private Image pImage;
    private int imgHeigth;
    private int imgWidth;
    private float scaleRatio;

    private float orient_x=0;
    private float orient_y=0;

    private Point position;
    private boolean holdStatus;

    public Player(){
        try {
            //random player image load

            //pImage = new Image("resources/player_img"+(new Random().nextInt(2)+1)+".png");
            pImage = new Image("resources/yusufps.png");
            scaleRatio =0.2f;
            imgHeigth=(int) (pImage.getHeight()* scaleRatio);
            imgWidth=(int)(pImage.getWidth()* scaleRatio);
        } catch (SlickException e) {
            e.printStackTrace();
            pImage=null;
        }
        position=new Point(80,80);
    }
    public Player(String playerName){
        this();
        name=playerName;
    }

    public Image getPlayerImage(){
        return pImage;
    }

    public void updateImageOrient(){
        //updates image orient with orient vals

        pImage.rotate(pImage.getRotation()-orient_x);

    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setPosition(float x, float y){

        this.position.setX(x);
        this.position.setY(y);
    }

    public void printAction(String in){

        System.out.println(name + " player message - "+in);
    }

    public void parseInput(String in){
        String[] vals = in.split("/");
        float sensivity=0.5f;


        if(vals[0].equalsIgnoreCase("d")){
            orient_x= Float.parseFloat(vals[1])*sensivity;
            orient_y= Float.parseFloat(vals[2])*sensivity;

            //setPosition
            float  X=getPosition().getX()-orient_x;
            float Y=getPosition().getY()-orient_y;
            if (X >=0 && X<SimpleSlickGame.screen_width-imgWidth)
                getPosition().setX(X);
            if(Y >=0 && Y<SimpleSlickGame.screen_height-imgHeigth)
                getPosition().setY(Y);

        }
        else if(vals[0].equalsIgnoreCase("bp")){
            //button pressed
            //(vals[1]);
            holdStatus=true;
        }else if(vals[0].equalsIgnoreCase("br")) {
            //button released
            //printAction(vals[1]);
            holdStatus=true;
        }

        //updateImageOrient();
    }
    @Override
    public void handleInput(String input) {
        parseInput(input);
    }

    public int getImgHeigth() {
        return imgHeigth;
    }

    public int getImgWidth() {
        return imgWidth;
    }
}
