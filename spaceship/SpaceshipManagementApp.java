
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;

public class SpaceshipManagementApp extends Application {
    private List<Spaceship> spaceships;
    private int budget = 1000; // Starting budget
    private int turn = 1; // Game turn counter
    private Label budgetLabel;
    private Label turnLabel;
    private VBox spaceshipStatusBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initializeSpaceships();

        // UI Layout
        BorderPane root = new BorderPane();
        VBox controlsBox = new VBox(10);
        controlsBox.setPadding(new Insets(10));
        controlsBox.setSpacing(15);

        budgetLabel = new Label("Budget: $" + budget);
        budgetLabel.setFont(Font.font(16));

        turnLabel = new Label("Turn: " + turn);
        turnLabel.setFont(Font.font(16));

        spaceshipStatusBox = new VBox();
        spaceshipStatusBox.setSpacing(10);
        refreshSpaceshipStatus();

        Button moraleButton = new Button("Host Morale Booster (-$200, +10 Morale)");
        moraleButton.setOnAction(e -> handleAction(1));

        Button trainingButton = new Button("Conduct Training (-$300, +10 Performance)");
        trainingButton.setOnAction(e -> handleAction(2));

        Button skipButton = new Button("Do Nothing (+$100, -5 Morale, -5 Performance)");
        skipButton.setOnAction(e -> handleAction(3));

        controlsBox.getChildren().addAll(budgetLabel, turnLabel, moraleButton, trainingButton, skipButton);

        root.setTop(new Label("Spaceship Management Game"));
        root.setCenter(spaceshipStatusBox);
        root.setBottom(controlsBox);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Spaceship Management Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeSpaceships() {
        spaceships = new ArrayList<>();
        spaceships.add(new Spaceship("Star Voyager", 75, 80));
        spaceships.add(new Spaceship("Cosmic Explorer", 60, 90));
        spaceships.add(new Spaceship("Galaxy Ranger", 85, 70));
        spaceships.add(new Spaceship("Nebula Cruiser", 50, 60));
        spaceships.add(new Spaceship("Astro Defender", 90, 95));
    }

    private void refreshSpaceshipStatus() {
        spaceshipStatusBox.getChildren().clear();
        for (Spaceship spaceship : spaceships) {
            Label status = new Label(spaceship.name + " - Morale: " + spaceship.morale + ", Performance: " + spaceship.performance);
            spaceshipStatusBox.getChildren().add(status);
        }
    }

    private void handleAction(int choice) {
        switch (choice) {
            case 1:
                if (budget >= 200) {
                    budget -= 200;
                    adjustSpaceships(10, 0);
                    showMessage("Morale Booster Event Held! Fleet morale increased.");
                } else {
                    showMessage("Not enough budget for a morale booster event.");
                }
                break;
            case 2:
                if (budget >= 300) {
                    budget -= 300;
                    adjustSpaceships(0, 10);
                    showMessage("Training Conducted! Fleet performance improved.");
                } else {
                    showMessage("Not enough budget for training.");
                }
                break;
            case 3:
                budget += 100;
                adjustSpaceships(-5, -5);
                showMessage("You did nothing. Budget increased, but fleet morale and performance declined.");
                break;
        }

        refreshSpaceshipStatus();
        budgetLabel.setText("Budget: $" + budget);
        turn++;

        if (checkGameOver()) {
            showMessage("Game Over! A spaceship's morale or performance dropped to 0.");
            System.exit(0);
        } else if (turn > 5) {
            showMessage("Congratulations! You managed the fleet for 5 turns successfully.");
            System.exit(0);
        }

        turnLabel.setText("Turn: " + turn);
    }

    private void adjustSpaceships(int moraleChange, int performanceChange) {
        for (Spaceship spaceship : spaceships) {
            spaceship.morale = Math.max(0, Math.min(100, spaceship.morale + moraleChange));
            spaceship.performance = Math.max(0, Math.min(100, spaceship.performance + performanceChange));
        }
    }

    private boolean checkGameOver() {
        for (Spaceship spaceship : spaceships) {
            if (spaceship.morale <= 0 || spaceship.performance <= 0) {
                return true;
            }
        }
        return false;
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Update");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

class Spaceship {
    String name;
    int morale;
    int performance;

    public Spaceship(String name, int morale, int performance) {
        this.name = name;
        this.morale = morale;
        this.performance = performance;
    }
}
