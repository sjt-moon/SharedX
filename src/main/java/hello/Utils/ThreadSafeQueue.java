package hello.Utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * conditional variable way
 * @param <T> queue element type
 */
public class ThreadSafeQueue<T> {
    private final Lock mutex = new ReentrantLock();

    private final Condition isQueueNotFull = mutex.newCondition();

    private final Condition isQueueNotEmpty = mutex.newCondition();

    private final Queue<T> queue = new LinkedList<>();

    public void add(T t) {
        mutex.lock();

        try {
            while (queue.size() >= MetaUtils.MAX_RECV_QUEUE_SIZE) {
                isQueueNotFull.await();
            }
            queue.offer(t);
            isQueueNotEmpty.signal();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            mutex.unlock();
        }
    }

    public T poll() {
        mutex.lock();
        try {
            while (queue.isEmpty()) {
                isQueueNotEmpty.await();
            }
            T t = queue.poll();
            isQueueNotFull.signal();
            return t;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            mutex.unlock();
        }
        return null;
    }
}
