package uk.co.mmscomputing.device;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mmscomputing.MmscomputingApplication;
import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;
import uk.co.mmscomputing.device.twain.TwainIOException;
import uk.co.mmscomputing.device.twain.TwainScanner;
import uk.co.mmscomputing.device.twain.TwainSource;
import uk.co.mmscomputing.device.twain.jtwain;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

import static uk.co.mmscomputing.device.twain.TwainConstants.CAP_PRINTERENABLED;


@RestController
public class TestController implements ScannerListener {

    int index=0;
    static String filename= "TEST";

    Scanner scanner = Scanner.getDevice();
    TwainSource twainSource;

    {
        try {
            twainSource = jtwain.getSource();
        } catch (TwainIOException e) {
            throw new RuntimeException(e);
        }
    }

    TwainScanner twainScanner = new TwainScanner();

    public TwainScanner before() throws ScannerIOException {
        index = 0;
        twainScanner.addListener(this);
//        twainScanner.select("KODAK Scanner: i2000");
        twainScanner.select();

        twainSource = jtwain.getSource();

        System.out.println(""+jtwain.isInstalled());
        if(!jtwain.isInstalled()){
            jtwain.setScanner(twainScanner);
        }
        twainSource.setShowUI(false);
        return twainScanner;
    }

    @GetMapping("/test")
    public void testTwain_Source(String[] var1) throws ScannerIOException {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName());
            twainScanner.addListener(this::update);
            jtwain.setScanner(twainScanner);
            twainSource.setShowUI(false);
            jtwain.acquire(twainScanner);
        }
        Thread.currentThread().destroy();
    }

    private void execute(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @GetMapping("/stop")
    public void stop() {
        try {
            jtwain.setCancel(twainScanner,true);
            System.out.println("暂停扫描。。。");

        } catch (TwainIOException e) {
            throw new RuntimeException(e);
        }

    }

    public void adf() throws ScannerIOException {
        this.scanner.addListener(this);

        this.scanner.acquire();
    }

    public void update(ScannerIOMetadata.Type var1, ScannerIOMetadata var2) {
        if (var1.equals(ScannerIOMetadata.ACQUIRED)) {
            System.out.println("ACQUIRED");
            BufferedImage var3 = var2.getImage();
            index = index+1;
            try {
                ImageIO.write(var3, "png", new File(filename+index+"-ACQUIRED-"+ UUID.randomUUID() +".png"));
                System.out.println("创建+=="+index);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        } else if (var1.equals(ScannerIOMetadata.NEGOTIATE)) {
            System.err.println("NEGOTIATE-" + var2.getStateStr());
        } else if (var1.equals(ScannerIOMetadata.STATECHANGE)) {
            if (var2.isFinished()) {
                System.out.println("扫描完成了。。。");
//                restart();

            }
        } else if (var1.equals(ScannerIOMetadata.EXCEPTION)) {
            System.out.println("EXCEPTION");
            var2.getException().printStackTrace();
        } else {
            System.out.println("不知道啥");
        }

    }

    public void restart(){
        MmscomputingApplication.restart();
    }
}
