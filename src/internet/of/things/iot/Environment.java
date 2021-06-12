/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internet.of.things.iot;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ani
 */
//shared object between Sensor thread and thread WeatherConditioner
//1)object contains the current values​of temperature and light
//2)a method to measure the environment's temperature and light values: 
//invoked by the sensors. It must block the sensors in case there aren't 
//new parameters.
//3)a method to update parameters the ambient's temperature and light values: 
//invoked by WeatherConditioner 
//methods must be mutually exclusive both with themselves and with each other
public class Environment extends Thread {
    //internal functional attributes
    private int valueTemp;
    private int valueLight;

    private final ReentrantLock lock;
    private final Condition newParameters;

    public Environment() {
        this.valueTemp = 0;
        this.valueLight = 0;
        this.lock = new ReentrantLock();
        this.newParameters = this.lock.newCondition();
    }

    public void measureParameters(Sensor s) throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(0);
            this.newParameters.await();
            s.lastReadTemperature = this.valueTemp;
            s.lastReadLight = this.valueLight;
            System.out.println("The " + super.getName() + " has measured the "
                    + " parameters of tempreture and light");
        } finally {
            this.lock.unlock();
        }

    }

    //methodo updateParameters(...) for updating parameters of the environment's 
    //tempreture and light invoked by WeatherConditioner
    public void updateParameters() throws InterruptedException {
        this.lock.lock();
        try {
            Thread.sleep(50);
            this.valueLight += 1000;
            this.valueTemp += 10 + (0.00022 * this.valueLight);
            System.out.println("The WeatherConditioner has updated " + valueTemp +
                    " temprature and " + valueLight + " light");
            this.newParameters.signalAll();
        } finally {
            this.lock.unlock();
        }
    }//end method
}//end class
