package view.GUI;

import controller.IController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import model.expression.*;
import model.state.*;
import model.statement.*;
import model.type.*;
import model.value.*;
import repository.IRepository;
import repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class ProgramChooserController {
    private ProgramExecutorController programExecutorController;

    @FXML
    private ListView<IStatement> programsListView;

    @FXML
    private Button displayButton;

    public void setProgramExecutorController(ProgramExecutorController programExecutorController) {
        this.programExecutorController = programExecutorController;
    }

    @FXML
    public void initialize() {
        programsListView.setItems(getAllStatements());
        programsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        programsListView.getSelectionModel().selectFirst();
    }

    @FXML
    private void displayProgram(javafx.event.ActionEvent actionEvent) {
        if (programExecutorController == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ProgramExecutorController nu este setat (null). Leagă controller-ele în Main!");
            alert.showAndWait();
            return;
        }

        IStatement selectedStatement = programsListView.getSelectionModel().getSelectedItem();
        if (selectedStatement == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Te rugăm să selectezi un program!");
            alert.showAndWait();
            return;
        }

        try {
            ProgramState prg = new ProgramState(
                    new StackExecutionStack<IStatement>(),
                    new MapSymbolTable<>(),
                    new ListOut<IValue>(),
                    selectedStatement,
                    new MapFileTable(),
                    new Heap()
            );

            // ✅ FIX: pune programul în stiva de execuție (altfel ExeStack e gol și Run One Step nu face nimic)
            prg.getExecutionStack().push(selectedStatement);

            IRepository repo = new Repository(prg, "log.txt");
            IController ctrl = new controller.Controller(repo);

            programExecutorController.setController(ctrl);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private ObservableList<IStatement> getAllStatements() {
        List<IStatement> allStatements = new ArrayList<>();

        // Program 1
        IStatement ex1 = new CompoundStatement(new VariableDeclarationStatement("v", new IntegerType()),
                new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        allStatements.add(ex1);

        // Program 2
        IStatement ex2 = new CompoundStatement(new VariableDeclarationStatement("a", new IntegerType()),
                new CompoundStatement(new VariableDeclarationStatement("b", new IntegerType()),
                        new CompoundStatement(new AssignmentStatement("a", new BinaryOperatorExpression("+", new ConstantExpression(new IntegerValue(2)),
                                new BinaryOperatorExpression("*", new ConstantExpression(new IntegerValue(3)), new ConstantExpression(new IntegerValue(5))))),
                                new CompoundStatement(new AssignmentStatement("b", new BinaryOperatorExpression("+", new VariableExpression("a"), new ConstantExpression(new IntegerValue(1)))),
                                        new PrintStatement(new VariableExpression("b"))))));
        allStatements.add(ex2);

        // Program 3
        IStatement ex3 = new CompoundStatement(new VariableDeclarationStatement("a", new BooleanType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(new AssignmentStatement("a", new ConstantExpression(new BooleanValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
                                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));
        allStatements.add(ex3);

        // Program 4
        IStatement ex4 = new CompoundStatement(new AssignmentStatement("y", new ConstantExpression(new IntegerValue(5))),
                new PrintStatement(new VariableExpression("y")));
        allStatements.add(ex4);

        // Program 5
        IStatement ex5 = new CompoundStatement(new VariableDeclarationStatement("a", new IntegerType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(new AssignmentStatement("a", new ConstantExpression(new IntegerValue(12))),
                                new CompoundStatement(new IfStatement(new RelationalExpression("<", new VariableExpression("a"), new ConstantExpression(new IntegerValue(10))),
                                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));
        allStatements.add(ex5);

        // Program 6
        IStatement ex6 = new CompoundStatement(new VariableDeclarationStatement("v", new IntegerType()),
                new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(new IntegerValue(4))),
                        new CompoundStatement(new WhileStatement(new RelationalExpression(">", new VariableExpression("v"), new ConstantExpression(new IntegerValue(0))),
                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                        new AssignmentStatement("v", new BinaryOperatorExpression("-", new VariableExpression("v"), new ConstantExpression(new IntegerValue(1)))))),
                                new PrintStatement(new VariableExpression("v")))));
        allStatements.add(ex6);

        // Program 7
        IStatement ex7 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntegerType())),
                new CompoundStatement(new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntegerType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new VariableExpression("a")))))));
        allStatements.add(ex7);

        // Program 8
        IStatement ex8 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntegerType())),
                new CompoundStatement(new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntegerType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(new BinaryOperatorExpression("+",
                                                        new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))),
                                                        new ConstantExpression(new IntegerValue(5)))))))));
        allStatements.add(ex8);

        // Program 9
        IStatement ex9 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntegerType())),
                new CompoundStatement(new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(new WriteHeapStatement("v", new ConstantExpression(new IntegerValue(30))),
                                        new PrintStatement(new BinaryOperatorExpression("+",
                                                new ReadHeapExpression(new VariableExpression("v")),
                                                new ConstantExpression(new IntegerValue(5))))))));
        allStatements.add(ex9);

        // Program 10
        IStatement ex10 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntegerType())),
                new CompoundStatement(new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntegerType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new NewStatement("v", new ConstantExpression(new IntegerValue(30))),
                                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))))))));
        allStatements.add(ex10);

        // Program 11
        IStatement ex11 = new CompoundStatement(new VariableDeclarationStatement("v", new IntegerType()),
                new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new IntegerType())),
                        new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(new IntegerValue(10))),
                                new CompoundStatement(new NewStatement("a", new ConstantExpression(new IntegerValue(22))),
                                        new CompoundStatement(new ForkStatement(new CompoundStatement(new WriteHeapStatement("a", new ConstantExpression(new IntegerValue(30))),
                                                new CompoundStatement(new AssignmentStatement("v", new ConstantExpression(new IntegerValue(32))),
                                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))))),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))))))));
        allStatements.add(ex11);

        IStatement ex12 =
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new NewStatement("a", new ConstantExpression(new IntegerValue(20))),
                                new CompoundStatement(
                                        new ForStatement(
                                                "v",
                                                new ConstantExpression(new IntegerValue(0)),
                                                new ConstantExpression(new IntegerValue(3)),
                                                new BinaryOperatorExpression(
                                                        "+",
                                                        new VariableExpression("v"),
                                                        new ConstantExpression(new IntegerValue(1))
                                                ),
                                                new ForkStatement(
                                                        new CompoundStatement(
                                                                new PrintStatement(new VariableExpression("v")),
                                                                new AssignmentStatement(
                                                                        "v",
                                                                        new BinaryOperatorExpression(
                                                                                "*",
                                                                                new VariableExpression("v"),
                                                                                new ReadHeapExpression(
                                                                                        new VariableExpression("a")
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        ),
                                        new PrintStatement(
                                                new ReadHeapExpression(
                                                        new VariableExpression("a")
                                                )
                                        )
                                )
                        )
                );
        allStatements.add(ex12);

        return FXCollections.observableArrayList(allStatements);
    }
}
