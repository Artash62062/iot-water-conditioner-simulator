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


    Cloud c;
    private final Random rnd;

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
                userTimeList.add(endCurrentTimeMillis - startCurrentTimeMillis); // stex skzbic hakaraknei drel jamanaki verj@ jamanaki skzbic er hanum dra hamar er bacasakan

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }//end for
        System.out.println("The thread" + super.getName() + "termina...");
    }//end run()

    public static double getSdUser() {
        double standardDeviation = 0;
        int length = userTimeList.size();
        double media = getMediaUser();
        for (Long aLong : userTimeList) {
            standardDeviation += Math.pow(aLong - media, 2);
        }
        return Math.sqrt(standardDeviation / length);
    }

    public static double getMediaUser() {
        long temp = 0;
        for (Long userTime : userTimeList) {
            temp += userTime;
        }
        return (double) temp / userTimeList.size();
    }
}//end class
    

