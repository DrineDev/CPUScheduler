package org.example.Classes;

import org.example.CLITools.ProgressBar;

public class Task implements Runnable {
    private int id;
    private int burstTime;
    private int remainingTime;
    private int arrivalTime;
    private int priorityNumber;
    private int completionTime;
    private ProgressBar progressBar;

    public Task(int id, int burstTime, int arrivalTime, int priorityNumber) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priorityNumber = priorityNumber;
        this.progressBar = new ProgressBar(0, burstTime, 50);
    }

    public Task(int id, int burstTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.arrivalTime = 0;
        this.priorityNumber = 0;
        this.progressBar = new ProgressBar(0, burstTime, 50);
    }

    public Task(int id, int burstTime, int arrivalTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priorityNumber = 0;
        this.progressBar = new ProgressBar(0, burstTime, 50);
    }

    public int getId() { return id; }
    public int getBurstTime() { return burstTime; }
    public int getArrivalTime() { return arrivalTime; }
    public int getPriorityNumber() { return priorityNumber; }
    public int getRemainingTime() { return remainingTime; }
    public String getProgressBar() { return progressBar.getString(); }
    public int getCompletionTime() { return completionTime; }
    public boolean isCompleted() { return remainingTime <= 0; }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
        this.progressBar = new ProgressBar(burstTime - remainingTime, burstTime, 50);
        this.progressBar.drawProgressBar();
    }

    public void setCompletionTime(int completionTime) { this.completionTime = completionTime; }

    @Override
    public void run() {
        if (remainingTime > 0) {
            try {
                Thread.sleep(1000); // Simulate 1 time unit of work
                remainingTime--;
                updateProgressBar();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void updateProgressBar() {
        this.progressBar = new ProgressBar(
                burstTime - remainingTime,
                burstTime,
                50
        );
    }
}
