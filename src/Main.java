import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
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
        BufferedImage inputBufferedImage = imageOperations.readImage(new File("D:/An4/Licenta/TestImages/Flower.png"));
        int width=inputBufferedImage.getWidth(), height=inputBufferedImage.getHeight();
        viewImage.displayImage(inputBufferedImage,"Original",width,height);
        Files.write(Paths.get("TimpRulare.txt"),("Width imagine="+inputBufferedImage.getWidth()+" Height imagine="+inputBufferedImage.getHeight()+"\n").getBytes(), StandardOpenOption.APPEND);

        //criptare
        long startTime=System.currentTimeMillis();
        List<BufferedImage> extractedChannelsList=imageOperations.extractColorChannels(inputBufferedImage);

        long seed=2157562;
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

        executorService.shutdown();
        executorService.awaitTermination(10,TimeUnit.MINUTES);
        long endTime=System.currentTimeMillis();
        NumberFormat formatter=new DecimalFormat("#0.00000");
        Files.write(Paths.get("TimpRulare.txt"),("Timpul total pentru criptare="+formatter.format((endTime-startTime)/1000d)+" secunde\n").getBytes(), StandardOpenOption.APPEND);

        List<BufferedImage> outputEncryptedImages=parallelEncryption.getOutputEncryptedImageList();
        BufferedImage finalEncryptedImage = imageOperations.constructImageFromRGBChannels(outputEncryptedImages.get(0), outputEncryptedImages.get(1),outputEncryptedImages.get(2));
        viewImage.displayImage(finalEncryptedImage,"finalEncryptedImage", finalEncryptedImage.getWidth(),finalEncryptedImage.getHeight());

        //terminare criptare




        //decriptare

        startTime=System.currentTimeMillis();
        Decryption decryption=new Decryption();
        ParallelDecryption parallelDecryption=new ParallelDecryption();
        executorService= Executors.newFixedThreadPool(randomSequenceMatrixForChannel.size());

        extractedChannelsList=imageOperations.extractColorChannels(finalEncryptedImage);
        randomSequenceMatrixForChannel=encryption.generateRandomSequenceForChannels(seed, finalEncryptedImage.getHeight(),finalEncryptedImage.getWidth());

        parallelDecryption.setSeedKey(seed);
        parallelDecryption.setRandomSequenceMatrix(randomSequenceMatrixForChannel.get(0));
        parallelDecryption.setColorChannel(extractedChannelsList.get(0));
        parallelDecryption.setStringChannel("red");

        executorService.execute(parallelDecryption);

        parallelDecryption=new ParallelDecryption();
        parallelDecryption.setSeedKey(seed);
        parallelDecryption.setRandomSequenceMatrix(randomSequenceMatrixForChannel.get(1));
        parallelDecryption.setColorChannel(extractedChannelsList.get(1));
        parallelDecryption.setStringChannel("green");

        executorService.execute(parallelDecryption);

        parallelDecryption=new ParallelDecryption();
        parallelDecryption.setSeedKey(seed);
        parallelDecryption.setRandomSequenceMatrix(randomSequenceMatrixForChannel.get(2));
        parallelDecryption.setColorChannel(extractedChannelsList.get(2));
        parallelDecryption.setStringChannel("blue");

        executorService.execute(parallelDecryption);


        executorService.shutdown();
        executorService.awaitTermination(10,TimeUnit.MINUTES);

        List<BufferedImage> outputDecryptedImageList=parallelDecryption.getOutputDecryptedImageList();
        BufferedImage fin= imageOperations.constructImageFromRGBChannels(outputDecryptedImageList.get(0),outputDecryptedImageList.get(1),outputDecryptedImageList.get(2));
        viewImage.displayImage(fin,"finalDecryptedImage", fin.getWidth(),fin.getHeight());


        endTime=System.currentTimeMillis();
        formatter=new DecimalFormat("#0.00000");
        Files.write(Paths.get("TimpRulare.txt"),("Timpul total pentru decriptare="+formatter.format((endTime-startTime)/1000d)+" secunde\n\n").getBytes(), StandardOpenOption.APPEND);

        //terminare decriptare


    }

}
