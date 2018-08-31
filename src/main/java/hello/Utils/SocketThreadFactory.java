package hello.Utils;

import java.util.concurrent.ThreadFactory;

public class SocketThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}
