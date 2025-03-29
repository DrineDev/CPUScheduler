package org.example.Algorithms;

import org.example.Classes.Task;

import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class HighestResponseRatio extends Algorithm {
    public HighestResponseRatio(int numCores) {
        super(numCores);
    }

    @Override
    public void startScheduling() {
        // Start progress display thread
        Thread progressThread = new Thread(this::refreshProgressBars);
        progressThread.setDaemon(true);
        progressThread.start();

        isRunning = true;

        // Scheduling logic using scheduler
        scheduler.scheduleAtFixedRate(() -> {
            if (!isRunning) {
                return;
            }

            // Update time and task queues
            updateTaskQueues();
            currentTime++;

            // Select and execute task with highest response ratio
            if (!readyQueue.isEmpty() && executingTasks.size() < coreCount) {
                // Sort ready queue by response ratio
                Task selectedTask = selectHighestResponseRatioTask();
                if (selectedTask != null) {
                    readyQueue.remove(selectedTask);
                    executeTask(selectedTask);
                }
            }

            // Check if all tasks are completed
            if (allTasks.size() == completedTasks.size()) {
                stopScheduling();
                printTaskStatistics();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    // Select task with highest response ratio from ready queue
    private Task selectHighestResponseRatioTask() {
        return readyQueue.stream()
                .max(Comparator.comparingDouble(this::calculateResponseRatio))
                .orElse(null);
    }

    // Calculate response ratio: (Waiting Time + Burst Time) / Burst Time
    private double calculateResponseRatio(Task task) {
        int waitingTime = currentTime - task.getArrivalTime();
        return (waitingTime + task.getBurstTime()) / (double) task.getBurstTime();
    }
}