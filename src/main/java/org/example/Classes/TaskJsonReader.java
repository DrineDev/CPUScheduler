package org.example.Classes;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * A utility class to read Task objects from a JSON file for the CPU Scheduler.
 */
public class TaskJsonReader {

    public static List<Task> readTasksFromJson(String filePath) throws IOException, ParseException {
        List<Task> tasks = new ArrayList<>();

        // Create a JSON parser
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            // Parse the JSON file
            Object obj = parser.parse(reader);

            // Convert the parsed object to a JSON array
            JSONArray taskArray = (JSONArray) obj;

            // Iterate through each object in the array
            for (Object taskObj : taskArray) {
                JSONObject taskJson = (JSONObject) taskObj;

                // Extract task attributes
                int id = ((Long) taskJson.get("id")).intValue();
                int burstTime = ((Long) taskJson.get("burstTime")).intValue();
                int arrivalTime = ((Long) taskJson.get("arrivalTime")).intValue();

                // Priority is optional, default to 0 if not provided
                int priorityNumber = 0;
                if (taskJson.containsKey("priorityNumber")) {
                    priorityNumber = ((Long) taskJson.get("priorityNumber")).intValue();
                }

                // Create a new Task object
                Task task = new Task(id, burstTime, arrivalTime, priorityNumber);
                tasks.add(task);
            }
        }

        return tasks;
    }

    /**
     * Example of how to use the TaskJsonReader.
     */
    public static void main(String[] args) {
        try {
            List<Task> tasks = readTasksFromJson("tasks.json");
            System.out.println("Successfully loaded " + tasks.size() + " tasks");

            // Print out all loaded tasks for verification
            for (Task task : tasks) {
                System.out.println("Task ID: " + task.getId() +
                        ", Burst Time: " + task.getBurstTime() +
                        ", Arrival Time: " + task.getArrivalTime() +
                        ", Priority: " + task.getPriorityNumber());
            }

            // Tasks can now be passed to your CPU scheduler

        } catch (IOException | ParseException e) {
            System.err.println("Error reading tasks file: " + e.getMessage());
        }
    }
}
