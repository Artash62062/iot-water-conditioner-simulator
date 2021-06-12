/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internet.of.things.iot;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ani
 */
public class Sensor extends Thread {
    private final Cloud cl;
    private final Environment env;
    int readingErrorPercentage;
    int lastReadTemperature;
    int lastReadLight;

    //constructor
    public Sensor(Environment e, Cloud c, String name) {
        super(name);
        this.env=e;
        readingErrorPercentage = getRandomNumber(-10,10);
        this.cl = c;
    }

    //thread behavior
    @Override
    public void run() {
        boolean isAlive = true;
        while (isAlive) {
            try {
                env.measureParameters(this);
                this.cl.writeData(this);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                isAlive = false;
            }
        }
        System.out.println("Il thread " + super.getName() + " termina...");
    }//end run


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}


