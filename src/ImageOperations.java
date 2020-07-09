import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageOperations {

    public BufferedImage readImage(File path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(path);
        } catch (IOException e) {
            System.out.println("Calea spre imagine si/sau numele imaginii este gresit!");
        }
        return image;
    }

    public List<BufferedImage> extractColorChannels(BufferedImage bufferedImage){
        List<BufferedImage> bufferedImageList=new ArrayList<BufferedImage>();
        //imaginile vor fi stocate in ordine red-green-blue
        for(int i=0;i<3;i++){
            bufferedImageList.add(new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),bufferedImage.getType()));
        }
        int red=0,green=0,blue=0,rgb=0;
        for(int i=0;i<bufferedImage.getWidth();i++){
            for(int j=0;j<bufferedImage.getHeight();j++){
                rgb = bufferedImage.getRGB(i,j);
                red = rgb >>16 & 0xff;
                green = rgb>>8 & 0xff;
                blue = rgb & 0xff;

                bufferedImageList.get(0).setRGB(i,j,red<<16); //red
                bufferedImageList.get(1).setRGB(i,j,green<<8); //green
                bufferedImageList.get(2).setRGB(i,j,blue); //blue
            }
        }
        return bufferedImageList;
    }

    public BufferedImage constructImageFromRGBChannels(BufferedImage redImage,
                                                       BufferedImage greenImage,
                                                       BufferedImage blueImage){
        int width=redImage.getWidth(),height=redImage.getHeight();
        BufferedImage outputImage=new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
        int red=0,green=0,blue=0;
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                red=redImage.getRGB(i,j);
                green=greenImage.getRGB(i,j);
                blue=blueImage.getRGB(i,j);
                outputImage.setRGB(i,j,red | green | blue);
            }
        }
        return outputImage;
    }

}
