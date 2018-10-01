// Java 3
// Lesson 5
// Homework
// Автор: Евгений Пермяков

//------------------------------------
// Гонка

// MainClass.java

package ru.geekbrains.homework5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainClass {
    public static final int CARS_COUNT = 8;

    public static void main(String[] args) {

        CountDownLatch checkeredFlag = new CountDownLatch(CARS_COUNT);
        CyclicBarrier startFlag = new CyclicBarrier(CARS_COUNT);
        Semaphore tunnelSemaphore = new Semaphore(CARS_COUNT / 2);
        AtomicBoolean isWinner = new AtomicBoolean(false);

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(tunnelSemaphore), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), startFlag, checkeredFlag, isWinner);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        try {
            checkeredFlag.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}

// End of MainClass.java

// Car.java

package ru.geekbrains.homework5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
    private static int CARS_COUNT;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private CyclicBarrier startFlag;
    private CountDownLatch checkeredFlag;
    private AtomicBoolean isWinner;

    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier startFlag, CountDownLatch checkeredFlag, AtomicBoolean isWinner) {
        this.race = race;
        this.speed = speed;
        this.startFlag = startFlag;
        this.checkeredFlag = checkeredFlag;
        this.isWinner = isWinner;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            startFlag.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        // Проверить, есть ли победитель, если нет, то стать победителем.
        if(isWinner.compareAndSet(false, true))
            System.out.println(this.name + " - WIN");
        checkeredFlag.countDown();
    }
}

// End of Car.java

// Tunnel.java

package ru.geekbrains.homework5;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    Semaphore tunnelSemaphore;

    public Tunnel(Semaphore tunnelSemaphore) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
        this.tunnelSemaphore = tunnelSemaphore;
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                // Взять семафор
                tunnelSemaphore.acquire();
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                // Отпустить семафор
                tunnelSemaphore.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// End of Tunnel.java
