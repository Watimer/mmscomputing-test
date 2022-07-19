package uk.co.mmscomputing.device;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;
import uk.co.mmscomputing.device.twain.TwainIOException;
import uk.co.mmscomputing.device.twain.jtwain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

import static uk.co.mmscomputing.device.twain.TwainConstants.CAP_PRINTERENABLED;

@RestController
public class TestController2 implements ScannerListener {

    static TestController2 test2;
    Scanner scanner = Scanner.getDevice();

    static String filename= "TEST";

    int index = 0;

    @GetMapping("/testTwainADF")
    public void testTwainADF(String[] arg) throws ScannerIOException {
        this.scanner.addListener(this);

        this.scanner.acquire();
    }

    @Override
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
//            DG_CONTROL / DAT_USERINTERFACE / MSG_DISABLEDS
        } else if (var1.equals(ScannerIOMetadata.NEGOTIATE)) {
            System.out.println("NEGOTIATE");
            System.err.println("NEGOTIATE" + var2.getStateStr());
            try {
                jtwain.getSource().setShowUI(false);
            } catch (TwainIOException e) {
                throw new RuntimeException(e);
            }
        } else if (var1.equals(ScannerIOMetadata.STATECHANGE)) {
//            System.out.println("STATECHANGE");

            if (var2.isFinished()) {
                if(this.index < 1){
                    try {
                        this.scanner.acquire();
                    } catch (ScannerIOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        this.scanner.setCancel(true);
                        jtwain.getSource().getCapability(CAP_PRINTERENABLED);
                    } catch (ScannerIOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else if (var1.equals(ScannerIOMetadata.EXCEPTION)) {
            System.out.println("EXCEPTION");
            var2.getException().printStackTrace();
        } else {
            System.out.println("不知道啥");
        }

    }
}
