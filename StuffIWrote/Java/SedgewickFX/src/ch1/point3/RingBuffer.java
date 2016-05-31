package ch1.point3;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Ethan Petuchowski 5/27/16
 *
 * uses an array representation (with circular wrap-around)
 *
 * Got some help with synchronization from
 *
 * https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/Condition.html
 */
public class RingBuffer<E> {

    private int produce;
    private int consume;
    private int size;
    private Object[] buffer;
    private Lock lock = new ReentrantLock();
    private Condition full = lock.newCondition();
    private Condition empty = lock.newCondition();

    public RingBuffer(int size) {
        buffer = new Object[size];
    }

    public static void main(String[] args) throws Exception {
        RingBuffer<Integer> integerRingBuffer = new RingBuffer<>(3);
        Runnable putter = new Runnable() {
            int next = 0;
            @Override public void run() {
                try {
                    System.out.println("putting: "+(++next));
                    integerRingBuffer.put(next);
                }
                catch (InterruptedException ignored) {}
            }
        };
        Runnable getter = () -> {
            try {
                System.out.println("running getter");
                System.out.println("got: "+integerRingBuffer.get());
            }
            catch (InterruptedException ignored) {}
        };
        ScheduledExecutorService e = Executors.newScheduledThreadPool(2);
        e.scheduleAtFixedRate(putter, 0, 3, TimeUnit.SECONDS);
        e.scheduleAtFixedRate(getter, 4, 2, TimeUnit.SECONDS);
    }

    /** blocks until the put can succeed */
    public void put(E item) throws InterruptedException {
        lock.lock();
        try {
            // await() _temporarily_ releases the lock
            while (isFull()) full.await();
            buffer[produce] = item;
            produce = (produce+1)%buffer.length;
            size++;
            empty.signal();
        }
        finally {
            lock.unlock();
        }
    }

    /** blocks until the get can succeed */
    public E get() throws InterruptedException {
        lock.lock();
        try {
            while (isEmpty()) empty.await();
            //noinspection unchecked
            E ret = (E) buffer[consume];
            consume = (consume+1)%buffer.length;
            size--;
            full.signal();
            return ret;
        }
        // this happens if we `return ret` OR `throw InterruptedException`
        finally {
            lock.unlock();
        }
    }

    public boolean isFull() {
        return size == buffer.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override public String toString() {
        return "RingBuffer{"+"buffer="+Arrays.toString(buffer)+'}';
    }
}
