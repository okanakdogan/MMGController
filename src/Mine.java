import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

/**
 * Created by Okan on 17.5.2016.
 */
public class Mine {

    private Image mineImage;
    private int mineType;
    private int value;
    private Point position;
    private float scaleRatio;
    private int imgHeight;
    private int imgWidth;

    public Mine(MineType type, Point pos){
        mineType = type.ordinal();
        try {
            if(type==MineType.MINE_COAL){
                value = 10;
                mineImage = new Image("resources/coal-s.png");
            }else if(type==MineType.MINE_SILVER){
                value = 30;
                mineImage = new Image("resources/silver-s.png");
            }else if(type == MineType.MINE_GOLD ){
                value = 50;
                mineImage = new Image("resources/gold-s.png");
            }else{
                //make it coal
            }
        } catch (SlickException e) {
            e.printStackTrace();
        }
        scaleRatio =0.2f;
        imgHeight =(int) (mineImage.getHeight()* scaleRatio);
        imgWidth=(int)(mineImage.getWidth()* scaleRatio);
        position= pos;
    }

    public Image getMineImage() {
        return mineImage;
    }

    public int getValue() {
        return value;
    }

    public Point getPosition() {
        return position;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public enum MineType{
        MINE_COAL ,MINE_SILVER,MINE_GOLD
    }
}
