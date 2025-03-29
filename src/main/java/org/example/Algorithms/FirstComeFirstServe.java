package org.example.Algorithms;

import org.example.Classes.Task;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

public class FirstComeFirstServe extends Algorithm {
    public FirstComeFirstServe(int numCores) {
        super(numCores);

        // Auto sort the queue by arrival time
        readyQueue = new PriorityQueue<>(Comparator.comparingInt(Task::getArrivalTime));
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
                Task task = readyQueue.poll();
                executeTask(task);
            }

            // Check if all tasks are completed
            if (allTasks.size() == completedTasks.size()) {
                stopScheduling();
                printTaskStatistics();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
