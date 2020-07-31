package main.java;

import javax.imageio.ImageIO;
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
                //fac operatiile urmatoare pentru a eliminta componenta alfa,deoarece nu vreau sa lucrez cu transparenta.
                rgb=rgb & 0x00ffffff;
                red = rgb >>16 & 0xff;
                green = rgb>>8 & 0xff;
                blue = rgb & 0xff;

                bufferedImageList.get(0).setRGB(i,j,(red<<16) ); //red
                bufferedImageList.get(1).setRGB(i,j,green<<8); //green
                bufferedImageList.get(2).setRGB(i,j,blue); //blue
            }
        }
        return bufferedImageList;
    }

    public BufferedImage constructImageFromRGBChannels(BufferedImage firstImage, BufferedImage secondImage, BufferedImage thirdImage){
        int width=firstImage.getWidth(),height=firstImage.getHeight();
        BufferedImage outputImage=new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        int color1=0,color2=0,color3=0;
        System.out.println();
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                color1=firstImage.getRGB(i,j);
                color2=secondImage.getRGB(i,j);
                color3=thirdImage.getRGB(i,j);
                int rgb=color1 | color2 | color3;
                outputImage.setRGB(i,j,rgb & 0x00ffffff);
            }
        }
        return outputImage;
    }

}
