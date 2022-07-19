package uk.co.mmscomputing.device;

import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;
import uk.co.mmscomputing.device.twain.TwainScanner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

public class WorkThread extends Thread implements ScannerListener {
    // 扫描仪状态
    private int status;
    // 扫描仪实例
    private TwainScanner twainScanner;
    int index = 0;
    static String filename = "TEST";

    public WorkThread(TwainScanner twainScanner) {
        this.twainScanner = twainScanner;
    }

    @Override
    public void run() {
        while (true) {
            Integer take = null;
            try {
                // 阻塞拉取任务当没有任务时 线程阻塞
                take = WorkQueue.queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (take != null) {
                // 开启扫描仪命令
                if (1 == take) {

                } else if (2 == take) {
                    // 结束扫描仪命令

                }
            }
        }
    }

    @Override
    public void update(ScannerIOMetadata.Type var1, ScannerIOMetadata var2) {
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
