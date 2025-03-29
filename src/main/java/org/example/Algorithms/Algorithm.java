package org.example.Algorithms;

import org.example.Classes.Task;

import java.util.*;
import java.util.concurrent.*;

public abstract class Algorithm {
    // Queues and task lists
    protected Queue<Task> readyQueue;
    protected Queue<Task> waitingQueue;
    protected List<Task> allTasks;
    protected List<Task> executingTasks;
    protected List<Task> completedTasks;

    // Concurrency and execution
    protected ExecutorService cpuCores;
    protected ScheduledExecutorService scheduler;

    // System parameters
    protected int coreCount;
    protected int currentTime;
    protected boolean isRunning;

    // Logging and monitoring
    protected boolean enableDetailedLogging;

    // Constructor
    public Algorithm(int numCores) {
        this.coreCount = numCores;

        // Thread-safe collections
        this.readyQueue = new ConcurrentLinkedQueue<>();
        this.waitingQueue = new ConcurrentLinkedQueue<>();
        this.allTasks = Collections.synchronizedList(new ArrayList<>());
        this.executingTasks = Collections.synchronizedList(new ArrayList<>());
        this.completedTasks = Collections.synchronizedList(new ArrayList<>());

        // Initialize thread pools
        this.cpuCores = Executors.newFixedThreadPool(numCores);
        this.scheduler = Executors.newScheduledThreadPool(1);

        this.currentTime = 0;
        this.isRunning = false;
        this.enableDetailedLogging = false;
    }

    // Abstract method to be implemented by specific scheduling algorithms
    public abstract void startScheduling();

    // Method to stop scheduling
    public synchronized void stopScheduling() {
        isRunning = false;
        cpuCores.shutdown();
        scheduler.shutdown();
        try {
            if (!cpuCores.awaitTermination(5, TimeUnit.SECONDS)) {
                cpuCores.shutdownNow();
            }
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            cpuCores.shutdownNow();
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Enhanced task addition method
    public synchronized void addTask(Task task) {
        allTasks.add(task);
        waitingQueue.add(task);
    }

    // Method to check and move tasks from waiting queue to ready queue
    protected void updateTaskQueues() {
        synchronized (waitingQueue) {
            Iterator<Task> iterator = waitingQueue.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (task.getArrivalTime() <= currentTime) {
                    readyQueue.add(task);
                    iterator.remove();
                }
            }
        }
    }

    // Execute a task on an available core
    protected void executeTask(Task task) {
        synchronized (executingTasks) {
            if (executingTasks.size() < coreCount) {
                cpuCores.submit(() -> {
                    while (!task.isCompleted()) {
                        task.run();

                        // Optional detailed logging
                        if (enableDetailedLogging) {
                            logTaskProgress(task);
                        }
                    }

                    // Move completed task
                    synchronized (this) {
                        task.setCompletionTime(currentTime);
                        executingTasks.remove(task);
                        completedTasks.add(task);
                    }
                });
                executingTasks.add(task);
            }
        }
    }

    // Optional detailed logging method
    protected void logTaskProgress(Task task) {
        System.out.printf("Task %d: Progress %s, Remaining Time: %d\n",
                task.getId(), task.getProgressBar(), task.getRemainingTime());
    }

    // Getters for task lists and statistics
    public List<Task> getReadyTasks() {
        return new ArrayList<>(readyQueue);
    }

    public List<Task> getWaitingTasks() {
        return new ArrayList<>(waitingQueue);
    }

    public List<Task> getExecutingTasks() {
        return new ArrayList<>(executingTasks);
    }

    public List<Task> getCompletedTasks() {
        return new ArrayList<>(completedTasks);
    }

    public double getAverageCompletionTime() {
        return completedTasks.stream()
                .mapToInt(Task::getBurstTime)
                .average()
                .orElse(0.0);
    }

    // Enable or disable detailed logging
    public void setDetailedLogging(boolean enable) {
        this.enableDetailedLogging = enable;
    }

    // Utility method to print task statistics
    public void printTaskStatistics() {
        System.out.println("\n--- Task Statistics ---");
        System.out.printf("Total Tasks: %d\n", allTasks.size());
        System.out.printf("Completed Tasks: %d\n", completedTasks.size());
        System.out.printf("Average Completion Time: %.2f\n", getAverageCompletionTime());
    }

    protected void refreshProgressBars() {
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            try {
                // Clear console (works on most Unix-like systems and Windows)
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                // Fallback for systems where above method doesn't work
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }

            // Header with basic information
            System.out.printf("%s Scheduler - Time: %d  Cores: %d/%d\n",
                    getClass().getSimpleName(), currentTime, getRunningTasksCount(), coreCount);
            System.out.println("----------------------------------------");

            // Waiting Tasks
            System.out.printf("\nWAITING TASKS (%d):\n", waitingQueue.size());
            printTaskList(waitingQueue);

            // Ready Queue
            System.out.printf("\nREADY QUEUE (%d):\n", readyQueue.size());
            printTaskList(readyQueue);

            // Executing Tasks
            System.out.println("\nEXECUTING TASKS:");
            printTaskList(executingTasks);

            // Completed Tasks
            System.out.printf("\nCOMPLETED TASKS (%d):\n", completedTasks.size());
            printTaskList(completedTasks);

            System.out.println("\nPress CTRL+C to exit");

            try {
                Thread.sleep(500); // Refresh twice per second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // Helper method to print task list
    private void printTaskList(Collection<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("  (No tasks)");
            return;
        }

        // Header
        System.out.println("  ID  Arrival  Burst  Priority  Progress");
        System.out.println("  --------------------------------------");

        // Tasks
        for (Task task : tasks) {
            System.out.printf("  %-3d %-7d %-6d %-9d %s\n",
                    task.getId(),
                    task.getArrivalTime(),
                    task.getBurstTime(),
                    task.getPriorityNumber(),
                    task.getProgressBar());
        }
    }

    // Helper method to count running tasks
    private int getRunningTasksCount() {
        return (int) executingTasks.stream()
                .filter(t -> !t.isCompleted())
                .count();
    }

}
