/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author diego
 */
public class A {
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private int a = 0;
    
    public int addsub(int value) {
        rwl.writeLock().lock();
        try {
            a += value;
            return a;
        } finally {
            rwl.writeLock().unlock();
        }
    }
    
    public int get() {
        rwl.readLock().lock();
        try {
            return a;
        } finally {
            rwl.readLock().unlock();
        }
    }
    
}
