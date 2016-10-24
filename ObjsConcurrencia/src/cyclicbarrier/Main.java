/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import readwritelock.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class Main {

    public static void main(String[] args) {
        A a = new A(100);
        CountDownLatch latch = new CountDownLatch(200);
        CyclicBarrier barrier = new CyclicBarrier(200);
        for(int i = 1; i <= 200; i++) {
            new RWThread(i, a, latch, barrier).start();
        }
        try {
            latch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("FINAL: " + a.get());
    }

}

class RWThread extends Thread {
    private final int value;
    private final A cuenta;
    private final CountDownLatch latch;
    private final CyclicBarrier barrier;

    public RWThread(int i, A a, CountDownLatch latch, CyclicBarrier barrier ) {
        cuenta = a;
        value = -1;
        this.latch = latch;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(RWThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(cuenta.addsub(value));
        latch.countDown();
    }
}
