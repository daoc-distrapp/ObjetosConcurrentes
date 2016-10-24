/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class Main {

    public static void main(String[] args) {
        BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(5);
        CountDownLatch latch = new CountDownLatch(200);
        CyclicBarrier barrier = new CyclicBarrier(200);
        for(int i = 1; i <= 100; i++) {
            new Productor(i, cola, latch, barrier).start();
            new Consumidor(cola, latch, barrier).start();
        }
        try {
            latch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("FINALIZADO");
    }

}

class Productor extends Thread {
    private final int i;
    private final BlockingQueue<Integer> cola;
    private final CountDownLatch latch;
    private final CyclicBarrier barrier;

    public Productor(int i, BlockingQueue<Integer> cola, CountDownLatch latch, CyclicBarrier barrier ) {
        this.cola = cola;
        this.i = i;
        this.latch = latch;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(Productor.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int j = 0; j < 10; j++) {
            try {
                cola.put(i);
            } catch (InterruptedException ex) {
                Logger.getLogger(Productor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        latch.countDown();
    }
}

class Consumidor extends Thread {
    private final BlockingQueue<Integer> cola;
    private final CountDownLatch latch;
    private final CyclicBarrier barrier;

    public Consumidor(BlockingQueue<Integer> cola, CountDownLatch latch, CyclicBarrier barrier ) {
        this.cola = cola;
        this.latch = latch;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int j = 0; j < 10; j++) {
            try {
                System.out.print(cola.take() + ":");
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        latch.countDown();
    }
}
