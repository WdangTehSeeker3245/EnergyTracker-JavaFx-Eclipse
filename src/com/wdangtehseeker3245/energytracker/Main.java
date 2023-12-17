package com.wdangtehseeker3245.energytracker;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends Application {

    private double deviceWattPerMinute = 1.0; // Default value
    private double pricePerWatt = 0.1; // Default value

    private Label consumptionLabel;
    private Label statusLabel;
    private Label costLabel;

    private boolean isTracking;
    private double consumption;
    private double totalCost;

    private javafx.animation.Timeline timeline;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Real-time Energy Tracker");

        // Create a BorderPane layout
        BorderPane borderPane = new BorderPane();

        // MenuBar with Configuration menu
        MenuBar menuBar = new MenuBar();
        Menu configurationMenu = new Menu("Configuration");
        MenuItem configurationItem = new MenuItem("Open Configuration Window");
        configurationMenu.getItems().add(configurationItem);
        menuBar.getMenus().add(configurationMenu);

        // Set up the top section with the MenuBar
        borderPane.setTop(menuBar);

        // Center section with Labels and Buttons
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 50, 20)); // Added margin to the bottom
        grid.setVgap(10);
        grid.setHgap(10);

        // Labels to display information
        statusLabel = new Label("Status: Not Tracking");
        GridPane.setConstraints(statusLabel, 0, 0);

        consumptionLabel = new Label("Current Consumption: 0.00 kWh");
        GridPane.setConstraints(consumptionLabel, 0, 1);

        costLabel = new Label("Total Cost: $0.00");
        GridPane.setConstraints(costLabel, 0, 2);

        // Start button to initiate tracking
        Button startButton = new Button("Start Tracking");
        GridPane.setConstraints(startButton, 0, 3);

        // Stop button to pause tracking
        Button stopButton = new Button("Stop Tracking");
        GridPane.setConstraints(stopButton, 0, 4);

        // Resume button to resume tracking
        Button resumeButton = new Button("Resume Tracking");
        GridPane.setConstraints(resumeButton, 0, 5);

        // Add components to the grid
        grid.getChildren().addAll(statusLabel, consumptionLabel, costLabel, startButton, stopButton, resumeButton);

        // Set action for the start button
        startButton.setOnAction(e -> startTracking());

        // Set action for the stop button
        stopButton.setOnAction(e -> stopTracking());

        // Set action for the resume button
        resumeButton.setOnAction(e -> resumeTracking());

        // Set action for the configuration item
        configurationItem.setOnAction(e -> openConfigurationWindow());

        // Set up the center section
        borderPane.setCenter(grid);

        // Set up the scene
        Scene scene = new Scene(borderPane, 400, 250); // Increased the window height
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private void startTracking() {
        if (!isTracking) {
            // Change the status label
            statusLabel.setText("Status: Tracking");

            // Reset consumption and total cost to zero if starting tracking again
            consumption = 0.0;
            totalCost = 0.0;

            // Create a Timeline to update the labels at regular intervals
            timeline = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), event -> updateLabels())
            );
            timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
            timeline.play();

            isTracking = true;
        }
    }

    private void stopTracking() {
        if (isTracking) {
            timeline.pause();
            isTracking = false;
            statusLabel.setText("Status: Tracking Paused");
        }
    }

    private void resumeTracking() {
        if (!isTracking) {
            timeline.play();
            isTracking = true;
            statusLabel.setText("Status: Tracking");
        }
    }

    // Update labels with simulated consumption value, cost, and system time
    private void updateLabels() {
        // Simulate energy consumption increase over time
        consumption += deviceWattPerMinute / 60; // Convert watt-minutes to watt-hours
        // Calculate the current cost
        double currentCost = consumption * pricePerWatt;
        totalCost += currentCost;

        // Get the current system time
        String currentTime = getCurrentTime();

        // Update the labels with the simulated consumption value, cost, and system time
        javafx.application.Platform.runLater(() -> {
            consumptionLabel.setText("Current Consumption: " + String.format("%.2f kWh", consumption) +
                    " at " + currentTime);
            costLabel.setText("Total Cost: $" + String.format("%.9f", totalCost));
        });
    }

    // Get the current system time
    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(new Date());
    }

    // Open the configuration window
    private void openConfigurationWindow() {
        ConfigurationWindow configurationWindow = new ConfigurationWindow();
        configurationWindow.showAndWait(); // Show and wait for the configuration to be saved
    }

    // ConfigurationWindow class
    private class ConfigurationWindow extends Stage {

        private TextField wattInput;
        private TextField priceInput;

        public ConfigurationWindow() {
            setTitle("Configuration Window");

            GridPane configGrid = new GridPane();
            configGrid.setPadding(new Insets(20, 20, 20, 20));
            configGrid.setVgap(10);
            configGrid.setHgap(10);

            Label wattLabel = new Label("Device Watt Consumption per Minute:");
            GridPane.setConstraints(wattLabel, 0, 0);

            wattInput = new TextField(String.valueOf(deviceWattPerMinute));
            GridPane.setConstraints(wattInput, 1, 0);

            Label priceLabel = new Label("Price per Watt:");
            GridPane.setConstraints(priceLabel, 0, 1);

            priceInput = new TextField(String.valueOf(pricePerWatt));
            GridPane.setConstraints(priceInput, 1, 1);

            Button saveButton = new Button("Save Configuration");
            GridPane.setConstraints(saveButton, 0, 2);

            // Add components to the grid
            configGrid.getChildren().addAll(wattLabel, wattInput, priceLabel, priceInput, saveButton);

            // Set action for the save button
            saveButton.setOnAction(e -> saveConfiguration());

            Scene configScene = new Scene(configGrid, 600, 150); // Increased the window width
            setScene(configScene);
        }

        private void saveConfiguration() {
            try {
                // Parse the user input and update the configuration
                deviceWattPerMinute = Double.parseDouble(wattInput.getText());
                pricePerWatt = Double.parseDouble(priceInput.getText());

                // Close the configuration window
                close();
            } catch (NumberFormatException ex) {
                // Handle invalid input (non-numeric)
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter valid numeric values for watt consumption and price.");
                alert.showAndWait();
            }
        }
    }
}
