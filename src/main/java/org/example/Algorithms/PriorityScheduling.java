package org.example.Algorithms;

import org.example.Classes.Task;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

public class PriorityScheduling extends Algorithm {
    private boolean preemptive;

    public PriorityScheduling(int numCores, boolean preemptive) {
        super(numCores);
        this.preemptive = preemptive;

        // Priority queue sorted by priority number (lower number = higher priority)
        readyQueue = new PriorityQueue<>(Comparator.comparingInt(Task::getPriorityNumber));
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

            // Preemptive behavior: check if a higher priority task can interrupt
            if (preemptive) {
                handlePreemptivePriority();
            }

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

    // Handle preemption by interrupting lower priority tasks
    private void handlePreemptivePriority() {
        // Find the highest priority task in the ready queue
        Task highestPriorityTask = readyQueue.peek();

        if (highestPriorityTask != null) {
            // Check if any executing task has lower priority
            synchronized (executingTasks) {
                executingTasks.removeIf(currentTask -> {
                    if (currentTask.getPriorityNumber() > highestPriorityTask.getPriorityNumber()) {
                        // Preempt lower priority task
                        readyQueue.add(currentTask);
                        return true;
                    }
                    return false;
                });
            }
        }
    }
}
