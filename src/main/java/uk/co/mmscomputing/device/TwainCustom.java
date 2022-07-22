package uk.co.mmscomputing.device;

import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerIOException;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

public class TwainCustom implements ScannerListener {
    private static Scanner scanner = Scanner.getDevice();
    private static ScannerListener prevListener;

    static String filename = "TEST";

    public TwainCustom() throws ScannerIOException {
        if (prevListener != null) scanner.removeListener(prevListener);
        scanner.addListener(this);
        prevListener = this;
        scanner.acquire();
    }

    @Override
    public void update(ScannerIOMetadata.Type var1, ScannerIOMetadata var2) {
        int index = 0;
        if (var1.equals(ScannerIOMetadata.ACQUIRED)) {
            System.out.println("ACQUIRED");
            BufferedImage var3 = var2.getImage();
            index = index + 1;
            try {
                ImageIO.write(var3, "png", new File(filename + index + "-ACQUIRED-" + UUID.randomUUID() + ".png"));
                System.out.println("创建+==" + index);
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
}
