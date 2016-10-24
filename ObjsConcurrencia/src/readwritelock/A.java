
package readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dordonez
 */
public class A {
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private int a;
    
    public A(int a) {
        this.a = a;
    }
    
    public int addsub(int value) {
        try {
            rwl.writeLock().lock();
            if(value > 0) {
                a += value;
            } else {
                int saldo = get();
                Thread.sleep(1);
                if(saldo + value >= 0) a += value;
                return a;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(A.class.getName()).log(Level.SEVERE, null, ex); 
        } finally {
            rwl.writeLock().unlock();
        }
        return Integer.MIN_VALUE;
    }
    
    public int get() {
        try {
            rwl.readLock().lock();
            return a;
        } finally {
            rwl.readLock().unlock();
        }
    }
    
}
