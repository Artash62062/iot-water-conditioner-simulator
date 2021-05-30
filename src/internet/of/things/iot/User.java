/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internet.of.things.iot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ani
 */
public class User extends Thread {

    static List<Long> userTimeList = new ArrayList<>();
    static ReentrantLock userTimeListLisLock = new ReentrantLock();


    Cloud c;
    private Random rnd;

    //constructor
    public User(Cloud c, String nome) {
        super(nome);
        this.c = c;
        this.rnd = new Random();
    }//end constructor

    //thread behavior
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            //simulate waiting in the interval [0,99]ms
            int timeToSleep = this.rnd.nextInt(100);
            try {
                Thread.sleep(timeToSleep);
                long startCurrentTimeMillis = System.currentTimeMillis();
                c.readAverageTemp(this);
                c.readAverageLight(this);
                long endCurrentTimeMillis = System.currentTimeMillis();
                userTimeListLisLock.lock();
                userTimeList.add(startCurrentTimeMillis - endCurrentTimeMillis);
                userTimeListLisLock.unlock();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }//end for
        System.out.println("The thread" + super.getName() + "termina...");
    }//end run()
}//end class
    

