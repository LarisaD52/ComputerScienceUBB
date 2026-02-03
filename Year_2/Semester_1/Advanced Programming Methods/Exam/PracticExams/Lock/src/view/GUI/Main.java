package view.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // incarcare ProgramChooser
        FXMLLoader programListLoader = new FXMLLoader();
        programListLoader.setLocation(Main.class.getResource("ProgramChooserController.fxml"));
        Parent programListRoot = programListLoader.load();

        ProgramChooserController programChooserController = programListLoader.getController();

        // incarcare ProgramExecutor
        FXMLLoader programExecutorLoader = new FXMLLoader();
        programExecutorLoader.setLocation(Main.class.getResource("ProgramExecutorController.fxml"));
        Parent programExecutorRoot = programExecutorLoader.load();

        ProgramExecutorController programExecutorController = programExecutorLoader.getController();

        //legatura intre controllere
        programChooserController.setProgramExecutorController(programExecutorController);

        //afisare fereastra de selectie (Chooser)
        primaryStage.setTitle("Select a program");
        primaryStage.setScene(new Scene(programListRoot, 525, 450));
        primaryStage.show();

        // afisare fereastra principala
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Interpreter");
        secondaryStage.setScene(new Scene(programExecutorRoot, 850, 500));
        secondaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}