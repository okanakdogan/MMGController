import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

import java.util.ArrayList;

/**
 * Created by Okan on 17.5.2016.
 */
public class Mine {

    private Image mineImage;

    private int value;
    private Point position;
    private float scaleRatio;
    private int imgHeight;
    private int imgWidth;
    private MinerGame game;
    private ArrayList<Image> mineImages;

    public Mine(MineType type, Point pos, ArrayList<Image> images){

        //this.game=game;
        mineImages=images;
        //mineType = type.ordinal();
        mineImage = findMine(type);

        if(type==MineType.MINE_COAL){
            value = 10;
        }else if(type==MineType.MINE_SILVER){
            value = 30;
        }else if(type == MineType.MINE_GOLD ){
            value = 50;
        }else{
            //make it coal
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

    private Image findMine(MineType type){
        String name=null;

        if(type==MineType.MINE_COAL){
            name="coal";
        }else if(type==MineType.MINE_SILVER){
            name="silver";
        }else if(type==MineType.MINE_GOLD){
            name="gold";
        }else
            name=null;


        for (Image i: mineImages){
            if(i.getName().equalsIgnoreCase(name))
                return i;
        }
        return null;
    }
}
