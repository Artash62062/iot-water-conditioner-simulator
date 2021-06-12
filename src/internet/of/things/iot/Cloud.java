/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internet.of.things.iot;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ani
 */
//shared object between Sensor thread and User thread
//1)object contains the circular buffers of fixed capacity 
//used to contain the temperature and light values
//2)a method to write values into the buffer: invoked by sensors. 
//It will have to block the sensors in case of lack of space in the buffer.
//3)the methods for obtaining the average of 4 consecutive values present 
//in the buffer and reading data: invoked by users. It will have to block 
//the users in case of an empty buffer
//methods must be mutually exclusive both with themselves and with each other

public class Cloud {
    //internal functional attributes
    // the buffer of temerature an the light

    private final Queue<Integer> tempBuffer;
    private final Queue<Integer> lightBuffer;
    private final ReentrantLock buffersLock;
    Semaphore newLightData;
    Semaphore newTempData;


    int maxSize;
    int minSize;
    //logical insertion and removal pointers


    //constructor
    public Cloud() {
        //initialize the functional attributes

        tempBuffer = new LinkedList<>();
        lightBuffer = new LinkedList<>();
        maxSize = 30;
        minSize = 4;
        this.buffersLock = new ReentrantLock();
        this.newLightData = new Semaphore(0, true); // stexic Lock Er@ hanem Semophore Em drel
        this.newTempData = new Semaphore(0, true);// nuyn@
    }//end constructor

    //method writeData(...) for writing a value by a sensor
    public void writeData(Sensor sensor) throws InterruptedException {
        //acquire a permission on the notFull semaphore
        //to make sure the buffer is not full
        writeTemperatureData(sensor.lastReadTemperature);
        writeLightData(sensor.lastReadLight);
        System.out.println("The sensor " + sensor.getName() + " has just wrote" + " the value" + "of temperature" + "in position " + tempBuffer.size() + " the value of light in position " + lightBuffer.size());

    }// end method writeData(...)


    public void writeTemperatureData(int temperature) throws InterruptedException {
        try {
            buffersLock.lock();
            tempBuffer.add(temperature); // buffer i mech avelacnum a temperature @
            newTempData.release(); // semophore i permit neri qnanak@ avelacnuma vor user @ karana haskana ep a mkaranalu karda
        } finally {
            buffersLock.unlock();
        }
    }


    public void writeLightData(int light) throws InterruptedException { // anum a nuyn ban@ inch verevin@ prost@ light i hamar
        try {
            buffersLock.lock();
            lightBuffer.add(light);
            newLightData.release();  // semophore i permit neri qnanak@ avelacnuma vor user @ karana haskana ep a mkaranalu karda
        } finally {
            buffersLock.unlock();
        }
    }


    public void readAverageTemp(User user) throws InterruptedException {
        newTempData.acquire(4); // Spasum a minchev Sempohire @ 4 hat permit unena ed jamanak 4 permit hanuma us ksuma ashxatel bufferneri het
        try {
            buffersLock.lock();                    // de pagum a bufferner@
            int sum = 0;
            for (int i = 0; i < 4; i++) {
                sum += tempBuffer.remove(); // verji grac infon buffer i glxic hanum a
            }
            System.out.println("The user " + user.getName() + " has just read the value "
                    + " from temperature Buffer and average is " + sum / 4); // de stex el mijin@ dusa talis eli :D
        } finally {
            buffersLock.unlock();
        }
    }

    public void readAverageLight(User user) throws InterruptedException { //nuyn@ inch vor verevin@ light i hamar anum
        newLightData.acquire(4);// Spasum a minchev Sempohire @ 4 hat permit unena ed jamanak 4 permit hanuma us ksuma ashxatel bufferneri het
        try {
            buffersLock.lock();
            int sum = 0;
            for (int i = 0; i < 4; i++) {
                sum += lightBuffer.remove();
            }
            System.out.println(" The user " + user.getName() + " has just read the value "
                    + " from Light Buffer and average is " + sum / 4);

        } finally {
            buffersLock.unlock();
        }

    }//end method readAverageTepm(...)


//
//    public int readAverageLight(User user) {
//        int value = -4;
////I have to make sure that there are at least 4 values to read
////acqure a permit on notEmpty
//        try {
//            this.notEmpty.acquire();
////if I am here it means that there is at least 4 values to read
/////////////////////???????????????Average value
//////
///////
//////
////proceed to the reading
////THE CRITICAL SECTION BEGINS
//            this.tempBufferLock.lock();
//            value = this.bufferLight[this.out];
//            System.out.println("The user" + user.getName() + "has just read the value"
//                    + "of the position" + this.out);
////update the logical pointer out
//            this.out = (this.out + 4) % this.bufferLight.length;
////notify the sensors that there is a new free place on a buffer
//            this.notFull.release();
//        } catch (InterruptedException e) {
//            System.out.println(e);
//        } finally {
////END OF CRITICAL SECTION
//            this.tempBufferlock.unlock();
//        }
//        return value;
//    }//end method readAverageLight(...)
//
//
//tempo media----------
//derivazione standard---------
}//end class





