/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internet.of.things.iot;

/**
 *
 * @author Artash
 */
//the thread that modifies environmental parameters
public class WeatherConditioner extends Thread {
    private Environment en;

    //constructor
    public WeatherConditioner(Environment e) {
        super("WeatherConditioner");
        this.en = e;
    }

    //thread behavior
    @Override
    public void run() {
        boolean isAlive = true;
        while (isAlive) {
            try {
                this.en.updateParameters();
                Thread.sleep(400);
            } catch (InterruptedException e) {
                System.out.println("INTERRUPT" + e);
                isAlive = false;
            }
        }//end while
        System.out.println("Il thread" + super.getName() + "termina...");
    }
}


