package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TasksWorkshop {

    public static class TaskManager {
        static final String FILE_NAME = "tasks.csv";
        static final String[] OPTIONS = {"add", "remove", "list", "exit"};
        static String[][] tasks;

        public static void displayOptions(String[] OPTIONS) {
            System.out.print(ConsoleColors.BLUE+"Please select an option: "+ConsoleColors.RESET);
            for (String option : OPTIONS) {
                System.out.print(option+" ");
            }
        }

        public static String[][] loadDataToTab(String fileName) {
            Path dir = Paths.get(fileName);
            if (!Files.exists(dir)) {
                System.out.println("File does not exist.");
                return new String[0][0];
            }

            String[][] tab = null;
            try {
                List<String> strings = Files.readAllLines(dir);
                if (strings.isEmpty()) return new String[0][0];
                tab = new String[strings.size()][strings.get(0).split(",").length];

                for (int i = 0; i < strings.size(); i++) {
                    String[] split = strings.get(i).split(",");
                    for (int j = 0; j < split.length; j++) {
                        tab[i][j] = split[j];
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tab;
        }

        public static void saveTasksToFile(String[][] tasks, String fileName) {
            List<String> lines = new ArrayList<>();
            for (String[] task : tasks) {
                lines.add(String.join(",", task));
            }

            try {
                Path path = Paths.get(fileName);
                Files.write(path, lines);
                System.out.println("Tasks saved to file successfully.");
            } catch (IOException e) {
                System.out.println("Error while saving tasks to file.");
                e.printStackTrace();
            }
        }

        public static void listTasks(String[][] tasks) {
            if (tasks == null || tasks.length == 0) {
                System.out.println("No tasks available.");
                return;
            }

            System.out.println("Listing tasks:");
            for (int i = 0; i < tasks.length; i++) {
                System.out.print(i + ". ");
                for (int j = 0; j < tasks[i].length; j++) {
                    System.out.print(tasks[i][j]);
                    if (j < tasks[i].length - 1) {
                        System.out.print(" | ");
                    }
                }
                System.out.println();
            }
        }

        public static String[][] removeTask(String[][] tasks) {
            if (tasks.length == 0) {
                System.out.println("No tasks to remove.");
                return tasks;
            }

            Scanner scanner = new Scanner(System.in);
            int index = -1;

            while (true) {
                System.out.print("Enter the index of the task to remove: ");
                String input = scanner.nextLine();

                try {
                    index = Integer.parseInt(input);
                    if (index >= 0 && index < tasks.length) {
                        String[][] newTasks = new String[tasks.length - 1][tasks[0].length];
                        for (int i = 0, j = 0; i < tasks.length; i++) {
                            if (i != index) {
                                newTasks[j++] = tasks[i];
                            }
                        }
                        System.out.println("Task removed successfully.");
                        return newTasks;
                    } else {
                        System.out.println("Invalid index. Try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Enter a number.");
                }
            }
        }

        public static void addTask() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please add task description:");
            String description = scanner.nextLine();
            System.out.println("Please add task due date:");
            String dueDate = scanner.nextLine();
            System.out.println("Is your task important? (true/false):");
            String isImportant = scanner.nextLine();

            tasks = Arrays.copyOf(tasks, tasks.length + 1);
            tasks[tasks.length - 1] = new String[3];
            tasks[tasks.length - 1][0] = description;
            tasks[tasks.length - 1][1] = dueDate;
            tasks[tasks.length - 1][2] = isImportant;

            saveTasksToFile(tasks, FILE_NAME);
        }

        public static void exitApplication(String message) {
            System.out.println(ConsoleColors.RED+message+ConsoleColors.RESET);
            System.exit(0);
        }

        public static void main(String[] args) {
            tasks = loadDataToTab(FILE_NAME);

            while (true) {
                displayOptions(OPTIONS);
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();

                switch (input) {
                    case "add":
                        addTask();
                        break;
                    case "remove":
                        tasks = removeTask(tasks);
                        saveTasksToFile(tasks, FILE_NAME);
                        break;
                    case "list":
                        listTasks(tasks);
                        break;
                    case "exit":
                        exitApplication("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid input. Try again.");
                }
            }
        }
    }
}