import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws IOException, StringException {
        //cheia este folosita ca si seed pentru generatorul de numere pseudoaleatoare
        //primul nr random va fi folosit drept 'd'=intial shift
        ImageOperations imageOperations=new ImageOperations();
        Encryption encryption=new Encryption();
        ViewImage viewImage=new ViewImage();
        //Flower;Flower2;Flower3;PinkFlower;Daisy;Lenna;Owl;Roses;Smoke;Umbrellas;testHeight;testWidth;testHeightScurt;testHeightScurtUmbrellas
        BufferedImage bufferedImage = imageOperations.readImage(new File("D:/An4/Licenta/TestImages/Umbrellas.png"));
        //System.out.println("Initial width="+bufferedImage.getWidth()+" height="+bufferedImage.getHeight());
        List<BufferedImage> bufferedImageList=imageOperations.extractColorChannels(bufferedImage);
        int width=bufferedImageList.get(0).getWidth(), height=bufferedImageList.get(0).getHeight();
        viewImage.displayImage(bufferedImage,"Original",width,height);

        viewImage.displayImage(bufferedImageList.get(0),"red",width,height);
        viewImage.displayImage(bufferedImageList.get(1),"green",width,height);
        viewImage.displayImage(bufferedImageList.get(2),"blue",width,height);

        List<int[][]> randomSequence=encryption.generateRandomSequenceForChannels(100,
                bufferedImage.getHeight(),bufferedImage.getWidth());

        //System.out.println(randomSequence.get(0)[320][320]);


        BufferedImage resultRed=encryption.doEncryption(968475836,
                randomSequence.get(0),bufferedImageList.get(0),"red");

        BufferedImage resultGreen=encryption.doEncryption(968475836,
                randomSequence.get(1),bufferedImageList.get(1),"red");

        BufferedImage resultBlue=encryption.doEncryption(968475836,
                randomSequence.get(2),bufferedImageList.get(2),"red");

        BufferedImage result = imageOperations.constructImageFromRGBChannels(resultRed,
                resultGreen,resultBlue);

        viewImage.displayImage(result,"criptare",
                result.getWidth(),result.getHeight());

    }

}
