/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internet.of.things.iot;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author ani
 */
public class InternetOfThingsIoT {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int nSensors = 0;
        int nUsers = 0;


        while (nSensors <= 0 || nUsers <= 0) {
            try {
                System.out.println("Enter the number of Sensors");
                nSensors = scan.nextInt();
                System.out.println("Enter the number of Users");
                nUsers = scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scan.next();
            }
        }
        scan.close();

        Cloud cloud = new Cloud();
        Environment environment = new Environment();

        //create thread WeatherComditioner
        WeatherConditioner weatherconditioner = new WeatherConditioner(environment);

        //create thread Sensore
        Sensor[] sensors = new Sensor[nSensors];
        // inizialize the thread Sensor

        //create thread User
        User[] users = new User[nUsers];
        // inizialize the thread User

        environment.start();

        weatherconditioner.start();

        for (int i = 0; i < users.length; i++) {
            users[i] = new User(cloud, "User_" + i);
            users[i].start();
        }
        for (int i = 0; i < sensors.length; i++) {
            sensors[i] = new Sensor(environment, cloud, "Sensor_" + i);
            sensors[i].start();
        }
        try {
            for (User user : users) user.join();
            for (Sensor sensor : sensors) {
                sensor.interrupt();
                sensor.join();
            }
            weatherconditioner.interrupt();
            weatherconditioner.join();
            environment.interrupt();
            environment.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

