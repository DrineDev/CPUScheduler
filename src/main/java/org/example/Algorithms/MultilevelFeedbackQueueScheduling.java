package org.example.Algorithms;

import org.example.Classes.Task;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MultilevelFeedbackQueueScheduling extends Algorithm {
    private List<Queue<Task>> queues;
    private List<Integer> timeQuantums;
    private int numberOfQueues;
    private int agingThreshold;

    public MultilevelFeedbackQueueScheduling(int numCores, int numberOfQueues, int agingThreshold) {
        super(numCores);
        this.numberOfQueues = numberOfQueues;
        this.agingThreshold = agingThreshold;

        // Initialize queues and time quantums
        queues = new ArrayList<>(numberOfQueues);
        timeQuantums = new ArrayList<>(numberOfQueues);

        for (int i = 0; i < numberOfQueues; i++) {
            queues.add(new LinkedList<>());
            // Exponentially increasing time quantums
            timeQuantums.add((int) Math.pow(2, i));
        }
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

            // Aging mechanism
            applyAgingMechanism();

            // Execute tasks from queues
            executeTasksFromQueues();

            // Check if all tasks are completed
            if (allTasks.size() == completedTasks.size()) {
                stopScheduling();
                printTaskStatistics();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    // Custom task addition to start at highest priority queue
    @Override
    public synchronized void addTask(Task task) {
        allTasks.add(task);
        // Initially add to the highest priority queue
        queues.get(0).add(task);
    }

    // Aging mechanism to prevent starvation
    private void applyAgingMechanism() {
        for (int i = 1; i < numberOfQueues; i++) {
            Queue<Task> currentQueue = queues.get(i);
            Queue<Task> higherQueue = queues.get(i - 1);

            // Move tasks that have waited too long to a higher priority queue
            currentQueue.removeIf(task -> {
                int waitingTime = currentTime - task.getArrivalTime();
                if (waitingTime > agingThreshold) {
                    higherQueue.add(task);
                    return true;
                }
                return false;
            });
        }
    }

    // Execute tasks from multiple queues
    private void executeTasksFromQueues() {
        for (int queueIndex = 0; queueIndex < numberOfQueues; queueIndex++) {
            Queue<Task> currentQueue = queues.get(queueIndex);
            int timeQuantum = timeQuantums.get(queueIndex);

            while (!currentQueue.isEmpty() && executingTasks.size() < coreCount) {
                Task task = currentQueue.poll();

                int finalQueueIndex = queueIndex;
                cpuCores.submit(() -> {
                    // Execute for the queue's time quantum
                    for (int i = 0; i < timeQuantum && !task.isCompleted(); i++) {
                        task.run();
                    }

                    synchronized (this) {
                        if (!task.isCompleted()) {
                            // Move to next queue if not completed
                            if (finalQueueIndex < numberOfQueues - 1) {
                                queues.get(finalQueueIndex + 1).add(task);
                            } else {
                                // If in the last queue, cycle back
                                currentQueue.add(task);
                            }
                        } else {
                            completedTasks.add(task);
                        }
                        executingTasks.remove(task);
                    }
                });

                executingTasks.add(task);
            }
        }
    }
}
