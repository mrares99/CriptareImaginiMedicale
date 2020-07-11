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



        Files.write(Paths.get("TimpRulare.txt"),("Width imagine="+inputBufferedImage.getWidth()+" Height imagine="+inputBufferedImage.getHeight()+"\n").getBytes(), StandardOpenOption.APPEND);

        //criptare
        long startTime=System.currentTimeMillis();
        List<BufferedImage> extractedChannelsList=imageOperations.extractColorChannels(inputBufferedImage);

//        viewImage.displayImage(extractedChannelsList.get(0),"extractedChannelsListOriginal0", extractedChannelsList.get(0).getWidth(),extractedChannelsList.get(0).getHeight());
//        viewImage.displayImage(extractedChannelsList.get(1),"extractedChannelsListOriginal1", extractedChannelsList.get(0).getWidth(),extractedChannelsList.get(0).getHeight());
//        viewImage.displayImage(extractedChannelsList.get(2),"extractedChannelsListOriginal2", extractedChannelsList.get(0).getWidth(),extractedChannelsList.get(0).getHeight());

        long seed=12345;
        List<int[][]> randomSequenceMatrixForChannel=encryption.generateRandomSequenceForChannels(seed, inputBufferedImage.getHeight(),inputBufferedImage.getWidth());

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


//        BufferedImage imgCriptata0=encryption.doEncryption(seed,randomSequenceMatrixForChannel.get(0),extractedChannelsList.get(0),"red");
//        BufferedImage imgCriptata1=encryption.doEncryption(seed,randomSequenceMatrixForChannel.get(1),extractedChannelsList.get(1),"green");
//        BufferedImage imgCriptata2=encryption.doEncryption(seed,randomSequenceMatrixForChannel.get(2),extractedChannelsList.get(2),"blue");


        executorService.shutdown();
        executorService.awaitTermination(10,TimeUnit.MINUTES);
        long endTime=System.currentTimeMillis();
        NumberFormat formatter=new DecimalFormat("#0.00000");
        Files.write(Paths.get("TimpRulare.txt"),("Timpul total pentru criptare="+formatter.format((endTime-startTime)/1000d)+" secunde\n").getBytes(), StandardOpenOption.APPEND);

        List<BufferedImage> outputEncryptedImages=parallelEncryption.getOutputEncryptedImageList();

        BufferedImage finalEncryptedImage = imageOperations.constructImageFromRGBChannels(outputEncryptedImages.get(0), outputEncryptedImages.get(1),outputEncryptedImages.get(2));
        //BufferedImage finalEncryptedImage = imageOperations.constructImageFromRGBChannels(imgCriptata0, imgCriptata1,imgCriptata2);
        viewImage.displayImage(finalEncryptedImage,"finalEncryptedImage", finalEncryptedImage.getWidth(),finalEncryptedImage.getHeight());

        //terminare criptare

        outputEncryptedImages=imageOperations.extractColorChannels(finalEncryptedImage);

        viewImage.displayImage(outputEncryptedImages.get(0),"extractedChannelsListDinImgCriptata0", outputEncryptedImages.get(0).getWidth(),outputEncryptedImages.get(0).getHeight());
        viewImage.displayImage(outputEncryptedImages.get(1),"extractedChannelsListDinImgCriptata1", outputEncryptedImages.get(0).getWidth(),outputEncryptedImages.get(0).getHeight());
        viewImage.displayImage(outputEncryptedImages.get(2),"extractedChannelsListDinImgCriptata2", outputEncryptedImages.get(0).getWidth(),outputEncryptedImages.get(0).getHeight());


        //decriptare

        Decryption decryption=new Decryption();

        extractedChannelsList=imageOperations.extractColorChannels(finalEncryptedImage);
        randomSequenceMatrixForChannel=encryption.generateRandomSequenceForChannels(seed, finalEncryptedImage.getHeight(),finalEncryptedImage.getWidth());

        BufferedImage decrypt1=decryption.doDecryption(seed,randomSequenceMatrixForChannel.get(0), extractedChannelsList.get(0),"red");
        BufferedImage decrypt2=decryption.doDecryption(seed,randomSequenceMatrixForChannel.get(1), extractedChannelsList.get(1),"green");
        BufferedImage decrypt3=decryption.doDecryption(seed,randomSequenceMatrixForChannel.get(2), extractedChannelsList.get(2),"blue");

        viewImage.displayImage(decrypt1,"canalDecriptat0", decrypt1.getWidth(),decrypt1.getHeight());
        viewImage.displayImage(decrypt2,"canalDecriptat1", decrypt1.getWidth(),decrypt1.getHeight());
        viewImage.displayImage(decrypt3,"canalDecriptat2", decrypt1.getWidth(),decrypt1.getHeight());

//        viewImage.displayImage(extractedChannelsList.get(0),"DecriptextractedChannelsList.get(0)", extractedChannelsList.get(0).getWidth(),extractedChannelsList.get(0).getHeight());
//        viewImage.displayImage(extractedChannelsList.get(1),"DecriptextractedChannelsList.get(1)", extractedChannelsList.get(0).getWidth(),extractedChannelsList.get(0).getHeight());
//        viewImage.displayImage(extractedChannelsList.get(2),"DecriptextractedChannelsList.get(2)", extractedChannelsList.get(0).getWidth(),extractedChannelsList.get(0).getHeight());

        BufferedImage fin= imageOperations.constructImageFromRGBChannels(decrypt1,decrypt2,decrypt3);
        viewImage.displayImage(fin,"finalDecryptedImage", fin.getWidth(),fin.getHeight());

        //terminare decriptare


//        Main main=new Main();
//        byte rez= main.circularRightShift((byte) 183,9);
//        System.out.println(rez);

    }



}
