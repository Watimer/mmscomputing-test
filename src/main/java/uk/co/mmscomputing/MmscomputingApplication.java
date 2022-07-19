package uk.co.mmscomputing;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MmscomputingApplication {

    private static String[] args;

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        MmscomputingApplication.args = args;
        MmscomputingApplication.context =
                SpringApplication.run(MmscomputingApplication.class, args);
    }

    public static void restart(){
        context.close();
        main(args);
//        MmscomputingApplication.context =
//                SpringApplication.run(MmscomputingApplication.class, args);
    }

}
