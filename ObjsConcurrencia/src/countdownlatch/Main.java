/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package countdownlatch;

import java.util.concurrent.CountDownLatch;
import readwritelock.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class Main {

    public static void main(String[] args) {
        A a = new A();
        CountDownLatch latch = new CountDownLatch(200);
        for(int i = 1; i <= 200; i++) {
            new RWThread(i, a, latch).start();
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

    public RWThread(int i, A a, CountDownLatch latch) {
        cuenta = a;
        value = (i % 2 == 0 ? 1 : -1);
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println(cuenta.addsub(value));
        latch.countDown();
    }
}
