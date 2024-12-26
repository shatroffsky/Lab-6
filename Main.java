package com.example.asyncdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
public class AsyncDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncDemoApplication.class, args);
    }

}

@Component
class UserTaskScheduler implements CommandLineRunner {

    private boolean isTaskEnabled = false;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final Instant startTime = Instant.now();

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Чи бажаєте виконувати задачу кожні 10 секунд? (Y/N): ");
        String input = scanner.nextLine().trim().toUpperCase();

        if ("Y".equals(input)) {
            isTaskEnabled = true;
            System.out.println("Задача увімкнена. Буде виконуватись кожні 10 секунд.");
        } else {
            System.out.println("Задача пропущена.");
        }

        scheduleRandomTask();
    }

    // Метод для регулярної задачі (виконання кожні 10 секунд)
    @Scheduled(fixedRate = 10000)
    public void periodicTask() {
        if (isTaskEnabled) {
            System.out.println("Задача виконується кожні 10 секунд: " + Instant.now());
        } else {
            System.out.println("Поточна задача пропущена.");
        }
    }

    // Метод для задачі з випадковим інтервалом
    private void scheduleRandomTask() {
        Runnable randomTask = () -> {
            try {
                int randomDelay = new Random().nextInt(10) + 1; // Інтервал від 1 до 10 секунд
                TimeUnit.SECONDS.sleep(randomDelay);

                Duration elapsedTime = Duration.between(startTime, Instant.now());
                System.out.println("Задача з випадковим інтервалом виконується через " + randomDelay + " секунд. Час від запуску програми: " + elapsedTime.getSeconds() + " секунд.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Помилка виконання випадкової задачі: " + e.getMessage());
            }
        };

        executorService.scheduleWithFixedDelay(randomTask, 0, 1, TimeUnit.SECONDS);
    }
}
