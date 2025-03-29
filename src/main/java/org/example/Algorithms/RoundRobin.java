package org.example.Algorithms;

import org.example.Classes.Task;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class RoundRobin extends Algorithm {
    private int timeQuantum;

    public RoundRobin(int numCores, int timeQuantum) {
        super(numCores);
        this.timeQuantum = timeQuantum;
        // Use LinkedList to easily rotate tasks
        readyQueue = new LinkedList<>();
    }

    @Override
    public void startScheduling() {
        // Start progress bar thread
        Thread progressBarThread = new Thread(this::refreshProgressBars);
        progressBarThread.setDaemon(true);
        progressBarThread.start();

        isRunning = true;

        // Scheduling logic
        scheduler.scheduleAtFixedRate(() -> {
            if (!isRunning) {
                return;
            }

            // Update time and task queues
            updateTaskQueues();
            currentTime++;

            // Execute tasks if cores are available
            while (!readyQueue.isEmpty() && executingTasks.size() < coreCount) {
                // Get and remove the first task
                Task task = ((LinkedList<Task>)readyQueue).pollFirst();

                // Execute the task for a time quantum
                cpuCores.submit(() -> {
                    for (int i = 0; i < timeQuantum && !task.isCompleted(); i++) {
                        task.run();
                    }

                    // If task is not completed, add it back to the end of the queue
                    synchronized (this) {
                        if (!task.isCompleted()) {
                            readyQueue.add(task);
                        } else {
                            completedTasks.add(task);
                        }
                        executingTasks.remove(task);
                    }
                });

                executingTasks.add(task);
            }

            // Check if all tasks are completed
            if (allTasks.size() == completedTasks.size()) {
                stopScheduling();
                printTaskStatistics();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}