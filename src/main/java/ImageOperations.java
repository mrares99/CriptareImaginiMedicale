package main.java;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ColorProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageOperations {

    ViewImage viewImage=new ViewImage();

    public BufferedImage readImage(File path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(path);
        } catch (IOException e) {
            System.out.println("Calea spre imagine si/sau numele imaginii este gresit!");
        }
        return image;
    }


    public List<BufferedImage> extractColorChannels(BufferedImage bufferedImage) throws IOException {
        List<BufferedImage> bufferedImageList=new ArrayList<BufferedImage>();
        int width=bufferedImage.getWidth(),height=bufferedImage.getHeight();
        //imaginile vor fi stocate in ordine red-green-blue
        BufferedImage redChannel=new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        BufferedImage greenChannel=new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        BufferedImage blueChannel=new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        int rgb=0;
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                rgb = bufferedImage.getRGB(i,j);
                redChannel.setRGB(i,j,(rgb & 0x00ff0000) ); //red
                greenChannel.setRGB(i,j,rgb & 0x0000ff00); //green
                blueChannel.setRGB(i,j,rgb & 0x000000ff); //blue
            }
        }
        bufferedImageList.add(redChannel);
        bufferedImageList.add(greenChannel);
        bufferedImageList.add(blueChannel);
        return bufferedImageList;
    }

    public BufferedImage constructImageFromRGBChannels(BufferedImage firstImage, BufferedImage secondImage, BufferedImage thirdImage){
        int width=firstImage.getWidth(),height=firstImage.getHeight();
        BufferedImage outputImage=new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        System.out.println();
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                outputImage.setRGB(i,j,firstImage.getRGB(i,j) | secondImage.getRGB(i,j) | thirdImage.getRGB(i,j));
            }
        }
        return outputImage;
    }

}
