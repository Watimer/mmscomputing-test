package uk.co.mmscomputing.device;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class WorkQueue {
    public final static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(200);
}
