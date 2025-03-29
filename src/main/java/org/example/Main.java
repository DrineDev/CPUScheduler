package org.example;

import org.example.Algorithms.*;
import org.example.CLITools.Description;
import org.example.CLITools.Line;
import org.example.CLITools.Table;
import org.example.CLITools.Title;
import org.example.Classes.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        // Display program title and description
        Title title = new Title("CPU Scheduler Simulator");
        Description description = new Description(
                "Welcome to the CPU Scheduler Simulator! This program demonstrates various CPU scheduling algorithms."
        );
        Line line = new Line(50);

        System.out.println(title.getString());
        System.out.println(line.getString());
        System.out.println(description.getString());
        System.out.println(line.getString());

        Scanner scanner = new Scanner(System.in);

        // Get number of CPU cores
        System.out.print("Enter the number of CPU cores: ");
        int cpuCores = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Display available algorithms
        Table algorithms = new Table(new String[]{"No.", "Algorithm"});
        algorithms.addRow(new String[] {"1", "First Come First Serve (FCFS)"});
        algorithms.addRow(new String[] {"2", "Shortest Job First (SJF)"});
        algorithms.addRow(new String[] {"3", "Longest Job First (LJF)"});
        algorithms.addRow(new String[] {"4", "Round Robin (RR)"});
        algorithms.addRow(new String[] {"5", "Priority Scheduling (PS)"});
        algorithms.addRow(new String[] {"6", "Highest Response Ratio (HRRN)"});
        algorithms.addRow(new String[] {"7", "Multiple Queue Scheduling (MQS)"});
        algorithms.addRow(new String[] {"8", "Multilevel Feedback Queue Scheduling (MFQS)"});
        algorithms.addRow(new String[] {"9", "Exit"});

        System.out.println("Available Scheduling Algorithms:");
        System.out.println(algorithms.getString());
        System.out.println(line.getString());

        // Get user input for algorithm selection
        int choice = 0;
        while (choice < 1 || choice > 9) {
            System.out.print("Select an algorithm (1-9): ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice < 1 || choice > 9) {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        if (choice == 9) {
            System.out.println("Exiting the program. Goodbye!");
            return;
        }

        // Create the selected algorithm
        Algorithm scheduler = null;
        switch (choice) {
            case 1:
                scheduler = new FirstComeFirstServe(cpuCores);
                break;
            case 2:
                scheduler = new ShortestJobFirst(cpuCores);
                break;
            case 3:
                scheduler = new LongestJobFirst(cpuCores);
                break;
            case 4:
                System.out.print("Enter time quantum for Round Robin: ");
                int timeQuantum = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                scheduler = new RoundRobin(timeQuantum, cpuCores);
                break;
            case 5:
                System.out.print("Should Priority Scheduling be preemptive? (true/false): ");
                boolean isPreemptive = scanner.nextBoolean();
                scanner.nextLine(); // Consume newline
                scheduler = new PriorityScheduling(cpuCores, isPreemptive);
                break;
            case 6:
                scheduler = new HighestResponseRatio(cpuCores);
                break;
            case 7:
                System.out.print("Enter number of queues for Multiple Queue Scheduling: ");
                int numberOfQueues = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                scheduler = new MultipleQueueScheduling(cpuCores, numberOfQueues);
                break;
            case 8:
                System.out.print("Enter number of queues for Multilevel Feedback Queue Scheduling: ");
                int mfqsNumberOfQueues = scanner.nextInt();
                System.out.print("Enter aging threshold for Multilevel Feedback Queue Scheduling: ");
                int agingThreshold = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                scheduler = new MultilevelFeedbackQueueScheduling(cpuCores, mfqsNumberOfQueues, agingThreshold);
                break;
        }

        // Choose between JSON input or manual input
        System.out.println(line.getString());
        System.out.println("How would you like to input tasks?");
        System.out.println("1. Read from JSON file");
        System.out.println("2. Manual input");

        int inputMethod = 0;
        while (inputMethod != 1 && inputMethod != 2) {
            System.out.print("Select an option (1-2): ");
            inputMethod = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (inputMethod != 1 && inputMethod != 2) {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        List<Task> tasks = new ArrayList<>();

        if (inputMethod == 1) {
            // Read tasks from JSON file
            System.out.print("Enter JSON file path: ");
            String filePath = scanner.nextLine();

            try {
                tasks = readTasksFromJson(filePath);
                System.out.println("Successfully loaded " + tasks.size() + " tasks from JSON.");
            } catch (IOException | ParseException e) {
                System.err.println("Error reading tasks file: " + e.getMessage());
                System.out.println("Switching to manual input...");
                inputMethod = 2;
            }
        }

        if (inputMethod == 2) {
            // Manual input of tasks
            System.out.print("Enter the number of tasks: ");
            int numTasks = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            for (int i = 0; i < numTasks; i++) {
                System.out.println("\nTask " + (i + 1) + ":");

                System.out.print("Enter task ID: ");
                int id = scanner.nextInt();

                System.out.print("Enter burst time: ");
                int burstTime = scanner.nextInt();

                System.out.print("Enter arrival time: ");
                int arrivalTime = scanner.nextInt();

                // Only ask for priority if using Priority Scheduling
                int priorityNumber = 0;
                if (choice == 5) {
                    System.out.print("Enter priority (lower number = higher priority): ");
                    priorityNumber = scanner.nextInt();
                }

                scanner.nextLine(); // Consume newline

                Task task = new Task(id, burstTime, arrivalTime, priorityNumber);
                tasks.add(task);
            }
        }

        // Add all tasks to the scheduler
        System.out.println(line.getString());
        System.out.println("Adding tasks to the scheduler...");
        for (Task task : tasks) {
            scheduler.addTask(task);
            System.out.println("Added Task " + task.getId() + " (Burst: " + task.getBurstTime() +
                    ", Arrival: " + task.getArrivalTime() +
                    (task.getPriorityNumber() > 0 ? ", Priority: " + task.getPriorityNumber() : "") + ")");
        }

        // Start the scheduling process
        System.out.println(line.getString());
        System.out.println("Starting the scheduler with " + cpuCores + " CPU core(s)...");
        scheduler.startScheduling();

        // Calculate and display scheduling statistics
        System.out.println(line.getString());
        System.out.println("Scheduling Statistics:");
        System.out.println("Average Waiting Time: " + calculateAverageWaitingTime(scheduler) + " time units");
        System.out.println("Average Turnaround Time: " + calculateAverageTurnaroundTime(scheduler) + " time units");

        scanner.close();
    }

    /**
     * Calculate the average waiting time for all completed tasks.
     *
     * @param scheduler The algorithm scheduler that has completed task execution
     * @return The average waiting time
     */
    private static double calculateAverageWaitingTime(Algorithm scheduler) {
        List<Task> completedTasks = scheduler.getCompletedTasks();
        if (completedTasks.isEmpty()) {
            return 0.0;
        }

        double totalWaitingTime = 0.0;
        for (Task task : completedTasks) {
            // Waiting time = Completion time - Arrival time - Burst time
            totalWaitingTime += task.getCompletionTime() - task.getArrivalTime() - task.getBurstTime();
        }

        return totalWaitingTime / completedTasks.size();
    }

    /**
     * Calculate the average turnaround time for all completed tasks.
     *
     * @param scheduler The algorithm scheduler that has completed task execution
     * @return The average turnaround time
     */
    private static double calculateAverageTurnaroundTime(Algorithm scheduler) {
        List<Task> completedTasks = scheduler.getCompletedTasks();
        if (completedTasks.isEmpty()) {
            return 0.0;
        }

        double totalTurnaroundTime = 0.0;
        for (Task task : completedTasks) {
            // Turnaround time = Completion time - Arrival time
            totalTurnaroundTime += task.getCompletionTime() - task.getArrivalTime();
        }

        return totalTurnaroundTime / completedTasks.size();
    }

    /**
     * Reads tasks from a JSON file and returns them as a list of Task objects.
     *
     * @param filePath the path to the JSON file
     * @return a list of Task objects
     * @throws IOException if the file cannot be read
     * @throws ParseException if the JSON is invalid
     */
    public static List<Task> readTasksFromJson(String filePath) throws IOException, ParseException {
        List<Task> tasks = new ArrayList<>();

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            JSONArray taskArray = (JSONArray) obj;

            for (Object taskObj : taskArray) {
                JSONObject taskJson = (JSONObject) taskObj;

                int id = ((Long) taskJson.get("id")).intValue();
                int burstTime = ((Long) taskJson.get("burstTime")).intValue();
                int arrivalTime = ((Long) taskJson.get("arrivalTime")).intValue();

                // Priority is optional, default to 0 if not provided
                int priorityNumber = 0;
                if (taskJson.containsKey("priorityNumber")) {
                    priorityNumber = ((Long) taskJson.get("priorityNumber")).intValue();
                }

                Task task = new Task(id, burstTime, arrivalTime, priorityNumber);
                tasks.add(task);
            }
        }

        return tasks;
    }
}