import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException, StringException, InterruptedException {
        //cheia este folosita ca si seed pentru generatorul de numere pseudoaleatoare
        //primul nr random va fi folosit drept 'd'=intial shift
        ImageOperations imageOperations=new ImageOperations();
        Encryption encryption=new Encryption();
        ViewImage viewImage=new ViewImage();
        //Flower;Flower2;Flower3;PinkFlower;Daisy;Lenna;Owl;Roses;Smoke;Umbrellas;testHeight;testWidth;testHeightScurt;testHeightScurtUmbrellas
        BufferedImage inputBufferedImage = imageOperations.readImage(new File("D:/An4/Licenta/TestImages/Smoke.png"));

        int width=inputBufferedImage.getWidth(), height=inputBufferedImage.getHeight();
        viewImage.displayImage(inputBufferedImage,"Original",width,height);

        long startTime=System.currentTimeMillis();

        List<BufferedImage> extractedChannelsList=imageOperations.extractColorChannels(inputBufferedImage);
        //canalele extrase sunt corecte;red-green-blue;am verificat

        long seed=12345;

        List<int[][]> randomSequenceMatrixForChannel=encryption.generateRandomSequenceForChannels(seed, inputBufferedImage.getHeight(),inputBufferedImage.getWidth());
        //am facut cateva afisari sa verific daca valorile sunt <8 si daca exista

        Files.write(Paths.get("TimpRulare.txt"),("Width imagine="+inputBufferedImage.getWidth()+" Height imagine="+inputBufferedImage.getHeight()+"\n").getBytes(), StandardOpenOption.APPEND);

        //criptare

        ParallelEncryption parallelEncryption=new ParallelEncryption();
        ExecutorService executorService= Executors.newFixedThreadPool(randomSequenceMatrixForChannel.size());

        parallelEncryption.setSeedKey(seed);
        parallelEncryption.setRandomSequenceMatrix(randomSequenceMatrixForChannel.get(0));
        parallelEncryption.setColorChannel(extractedChannelsList.get(0));
        parallelEncryption.setStringChannel("red");

        executorService.execute(parallelEncryption);

        parallelEncryption=new ParallelEncryption();
        parallelEncryption.setSeedKey(seed);
        parallelEncryption.setRandomSequenceMatrix(randomSequenceMatrixForChannel.get(1));
        parallelEncryption.setColorChannel(extractedChannelsList.get(1));
        parallelEncryption.setStringChannel("green");

        executorService.execute(parallelEncryption);

        parallelEncryption=new ParallelEncryption();
        parallelEncryption.setSeedKey(seed);
        parallelEncryption.setRandomSequenceMatrix(randomSequenceMatrixForChannel.get(2));
        parallelEncryption.setColorChannel(extractedChannelsList.get(2));
        parallelEncryption.setStringChannel("blue");

        executorService.execute(parallelEncryption);

        executorService.shutdown();
        executorService.awaitTermination(10,TimeUnit.MINUTES);
        long endTime=System.currentTimeMillis();
        NumberFormat formatter=new DecimalFormat("#0.00000");
        Files.write(Paths.get("TimpRulare.txt"),("Timpul total pentru criptare="+formatter.format((endTime-startTime)/1000d)+" secunde\n").getBytes(), StandardOpenOption.APPEND);

        List<BufferedImage> outputEncryptedImages=parallelEncryption.getOutputEncryptedImageList();

        //terminare criptare

        BufferedImage finalEncryptedImage = imageOperations.constructImageFromRGBChannels(outputEncryptedImages.get(0), outputEncryptedImages.get(1),outputEncryptedImages.get(2));
        viewImage.displayImage(finalEncryptedImage,"finalEncryptedImage", finalEncryptedImage.getWidth(),finalEncryptedImage.getHeight());

        //decriptare

        Decryption decryption=new Decryption();

        extractedChannelsList=imageOperations.extractColorChannels(finalEncryptedImage);
        randomSequenceMatrixForChannel=encryption.generateRandomSequenceForChannels(seed, finalEncryptedImage.getHeight(),finalEncryptedImage.getWidth());

//        viewImage.displayImage(extractedChannelsList.get(0),"extractedCrypt1", extractedChannelsList.get(0).getWidth(),extractedChannelsList.get(0).getHeight());
//        viewImage.displayImage(extractedChannelsList.get(1),"extractedCrypt2", extractedChannelsList.get(0).getWidth(),extractedChannelsList.get(0).getHeight());
//        viewImage.displayImage(extractedChannelsList.get(2),"extractedCrypt3", extractedChannelsList.get(0).getWidth(),extractedChannelsList.get(0).getHeight());


        BufferedImage img1=decryption.doDecryption(seed,randomSequenceMatrixForChannel.get(0), extractedChannelsList.get(0),"red");
        BufferedImage img2=decryption.doDecryption(seed,randomSequenceMatrixForChannel.get(1), extractedChannelsList.get(1),"green");
        BufferedImage img3=decryption.doDecryption(seed,randomSequenceMatrixForChannel.get(2), extractedChannelsList.get(2),"blue");

        viewImage.displayImage(img1,"img1", img1.getWidth(),img1.getHeight());
        viewImage.displayImage(img2,"img2", img1.getWidth(),img1.getHeight());
        viewImage.displayImage(img3,"img3", img1.getWidth(),img1.getHeight());

        BufferedImage fin= imageOperations.constructImageFromRGBChannels(img1,img2,img3);
//        viewImage.displayImage(fin,"fin", fin.getWidth(),fin.getHeight());

        //terminare decriptare

    }

}
