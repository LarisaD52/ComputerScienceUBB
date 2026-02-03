package view.GUI;

import controller.IController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.state.ProgramState;
import model.value.IValue;

import java.util.*;
import java.util.stream.Collectors;

// Pair local (folosit pentru Heap și SymTable)
class Pair<T1, T2> {
    T1 first;
    T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() { return first; }
    public T2 getSecond() { return second; }
}

public class ProgramExecutorController {

    private IController controller;

    @FXML private TextField numberOfProgramStatesTextField;

    // Heap
    @FXML private TableView<Pair<Integer, IValue>> heapTableView;
    @FXML private TableColumn<Pair<Integer, IValue>, Integer> addressColumn;
    @FXML private TableColumn<Pair<Integer, IValue>, String> valueColumn;

    // Output / FileTable
    @FXML private ListView<String> outputListView;
    @FXML private ListView<String> fileTableListView;

    // Program states
    @FXML private ListView<Integer> programStateIdentifiersListView;

    // Symbol table
    @FXML private TableView<Pair<String, IValue>> symbolTableView;
    @FXML private TableColumn<Pair<String, IValue>, String> variableNameColumn;
    @FXML private TableColumn<Pair<String, IValue>, String> variableValueColumn;

    // Execution stack
    @FXML private ListView<String> executionStackListView;

    // BarrierTable
    @FXML private TableView<BarrierTableViewEntry> barrierTableView;
    @FXML private TableColumn<BarrierTableViewEntry, Integer> barrierIndexColumn;
    @FXML private TableColumn<BarrierTableViewEntry, Integer> barrierNColumn;
    @FXML private TableColumn<BarrierTableViewEntry, String> barrierListColumn;

    @FXML private Button runOneStepButton;

    // =========================
    // INITIALIZATION
    // =========================
    @FXML
    public void initialize() {

        programStateIdentifiersListView
                .getSelectionModel()
                .setSelectionMode(SelectionMode.SINGLE);

        // Heap
        addressColumn.setCellValueFactory(
                p -> new SimpleIntegerProperty(p.getValue().first).asObject()
        );
        valueColumn.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().second.toString())
        );

        // Symbol table
        variableNameColumn.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().first)
        );
        variableValueColumn.setCellValueFactory(
                p -> new SimpleStringProperty(p.getValue().second.toString())
        );

        // BarrierTable
        barrierIndexColumn.setCellValueFactory(
                new PropertyValueFactory<>("index")
        );
        barrierNColumn.setCellValueFactory(
                new PropertyValueFactory<>("n")
        );
        barrierListColumn.setCellValueFactory(
                new PropertyValueFactory<>("waitingList")
        );

        // Listener pentru schimbarea ProgramState-ului selectat
        programStateIdentifiersListView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    populateExecutionStackListView();
                    populateSymbolTableView();
                });
    }

    // =========================
    // CONTROLLER SET
    // =========================
    public void setController(IController controller) {
        this.controller = controller;
        runOneStepButton.setDisable(false);
        populate();
    }

    // =========================
    // CORE LOGIC
    // =========================
    private ProgramState getCurrentProgramState() {
        List<ProgramState> prgList = controller.getProgramStates();
        if (prgList == null || prgList.isEmpty()) return null;

        Integer selectedId =
                programStateIdentifiersListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedId == null)
            return prgList.get(0);

        return prgList.stream()
                .filter(p -> p.getId() == selectedId)
                .findFirst()
                .orElse(prgList.get(0));
    }

    private void populate() {
        populateProgramStateIdentifiersListView();
        ProgramState ps = getCurrentProgramState();

        if (ps != null) {
            populateNumberOfProgramStatesTextField();
            populateHeapTableView();
            populateOutputListView();
            populateFileTableListView();
            populateSymbolTableView();
            populateExecutionStackListView();
            barrierTableView.setItems(getBarrierTableData());
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        heapTableView.setItems(FXCollections.observableArrayList());
        outputListView.setItems(FXCollections.observableArrayList());
        fileTableListView.setItems(FXCollections.observableArrayList());
        symbolTableView.setItems(FXCollections.observableArrayList());
        executionStackListView.setItems(FXCollections.observableArrayList());
        barrierTableView.setItems(FXCollections.observableArrayList());
        numberOfProgramStatesTextField.setText("0");
    }

    // =========================
    // POPULATE METHODS
    // =========================
    private void populateNumberOfProgramStatesTextField() {
        numberOfProgramStatesTextField
                .setText(String.valueOf(controller.getProgramStates().size()));
    }

    private void populateProgramStateIdentifiersListView() {
        List<Integer> ids = controller.getProgramStates()
                .stream()
                .map(ProgramState::getId)
                .collect(Collectors.toList());

        programStateIdentifiersListView
                .setItems(FXCollections.observableArrayList(ids));

        if (!ids.isEmpty() &&
                programStateIdentifiersListView.getSelectionModel().getSelectedItem() == null) {
            programStateIdentifiersListView.getSelectionModel().selectFirst();
        }
    }

    private void populateHeapTableView() {
        ProgramState ps = getCurrentProgramState();
        if (ps == null) return;

        List<Pair<Integer, IValue>> entries = ps.getHeap()
                .getContent()
                .entrySet()
                .stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        heapTableView.setItems(FXCollections.observableArrayList(entries));
    }

    private void populateOutputListView() {
        ProgramState ps = getCurrentProgramState();
        if (ps == null) return;

        outputListView.setItems(
                FXCollections.observableArrayList(
                        ps.getOutput().getAll()
                                .stream()
                                .map(Object::toString)
                                .collect(Collectors.toList())
                )
        );
    }

    private void populateFileTableListView() {
        ProgramState ps = getCurrentProgramState();
        if (ps == null) return;

        fileTableListView.setItems(
                FXCollections.observableArrayList(
                        Arrays.asList(ps.getFileTable().getAllKeys())
                )
        );
    }

    private void populateSymbolTableView() {
        ProgramState ps = getCurrentProgramState();
        if (ps == null) return;

        List<Pair<String, IValue>> entries = ps.getSymbolTable()
                .getContent()
                .entrySet()
                .stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        symbolTableView.setItems(FXCollections.observableArrayList(entries));
    }

    private void populateExecutionStackListView() {
        ProgramState ps = getCurrentProgramState();
        if (ps == null) return;

        executionStackListView.setItems(
                FXCollections.observableArrayList(
                        ps.getExecutionStack()
                                .getReversed()
                                .stream()
                                .map(Object::toString)
                                .collect(Collectors.toList())
                )
        );
    }

    private ObservableList<BarrierTableViewEntry> getBarrierTableData() {
        ObservableList<BarrierTableViewEntry> list =
                FXCollections.observableArrayList();

        ProgramState ps = getCurrentProgramState();
        if (ps == null) return list;

        ps.getBarrierTable().getContent().forEach((index, pair) -> {
            list.add(
                    new BarrierTableViewEntry(
                            index,
                            pair.getKey(),
                            pair.getValue().toString()
                    )
            );
        });

        return list;
    }

    // =========================
    // ONE STEP
    // =========================
    @FXML
    private void runOneStep(ActionEvent actionEvent) {
        if (controller == null) return;

        try {
            List<ProgramState> prgList = controller.getProgramStates();
            if (!prgList.isEmpty()) {
                controller.oneStepForAllPrg(prgList);
                populate();

                if (controller.getProgramStates().isEmpty()) {
                    runOneStepButton.setDisable(true);
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("Execuția s-a terminat!");
                    a.showAndWait();
                }
            }
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }
}
