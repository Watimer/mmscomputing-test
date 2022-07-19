package uk.co.mmscomputing.device;

import org.springframework.stereotype.Component;
import uk.co.mmscomputing.device.twain.TwainScanner;

import javax.annotation.PostConstruct;

@Component
public class InitWork {
    @PostConstruct
    public void init() {
        //开启扫描仪服务
        TwainScanner twainScanner = null;
        WorkThread wt = new WorkThread(twainScanner);
        wt.start();
    }
}
