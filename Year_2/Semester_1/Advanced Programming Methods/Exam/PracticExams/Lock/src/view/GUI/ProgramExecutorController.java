package view.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import controller.IController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.state.ProgramState;
import model.value.IValue;

import java.util.*;
import java.util.stream.Collectors;

class Pair<T1, T2> {
    T1 first;
    T2 second;
    public Pair(T1 first, T2 second) { this.first = first; this.second = second; }
    public T1 getFirst() { return first; }
    public T2 getSecond() { return second; }
}

public class ProgramExecutorController {
    private IController controller;

    @FXML private TextField numberOfProgramStatesTextField;
    @FXML private TableView<Pair<Integer, IValue>> heapTableView;
    @FXML private TableColumn<Pair<Integer, IValue>, Integer> addressColumn;
    @FXML private TableColumn<Pair<Integer, IValue>, String> valueColumn;
    @FXML private ListView<String> outputListView;
    @FXML private ListView<String> fileTableListView;
    @FXML private ListView<Integer> programStateIdentifiersListView;
    @FXML private TableView<Pair<String, IValue>> symbolTableView;
    @FXML private TableColumn<Pair<String, IValue>, String> variableNameColumn;
    @FXML private TableColumn<Pair<String, IValue>, String> variableValueColumn;
    @FXML private ListView<String> executionStackListView;
    @FXML private Button runOneStepButton;

    @FXML private TableView<LockTableEntry> lockTableView;
    @FXML private TableColumn<LockTableEntry, Integer> lockLocationColumn;
    @FXML private TableColumn<LockTableEntry, Integer> lockValueColumn;

    public void setController(IController controller) {
        this.controller = controller;
        runOneStepButton.setDisable(false);   //  re-enable
        populate();
    }


    @FXML
    public void initialize() {
        programStateIdentifiersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Initializare coloane Heap
        addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().first).asObject());
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));

        // Initializare coloane Symbol Table
        variableNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first));
        variableValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));

        // Listener pentru a schimba datele afisate când selectezi alt ID de program
        programStateIdentifiersListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    ProgramState ps = getCurrentProgramState();
                    if (ps != null) {
                        populateExecutionStackListView();
                        populateSymbolTableView();
                        populateLockTable(ps);
                    }
                });

        lockLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location")
        );

        lockValueColumn.setCellValueFactory(new PropertyValueFactory<>("value")
        );

    }

    // FIX: selecteaza ProgramState dupa ID-ul selectat, nu dupa index
    private ProgramState getCurrentProgramState() {
        List<ProgramState> prgList = controller.getProgramStates();
        if (prgList == null || prgList.isEmpty()) return null;

        Integer selectedId = programStateIdentifiersListView.getSelectionModel().getSelectedItem();
        if (selectedId == null) return prgList.get(0);

        return prgList.stream()
                .filter(p -> p.getId() == selectedId)
                .findFirst()
                .orElse(prgList.get(0));
    }

    private void populate() {
        populateProgramStateIdentifiersListView();
        ProgramState currentProgramState = getCurrentProgramState();

        if (currentProgramState != null) {
            populateNumberOfProgramStatesTextField();
            populateHeapTableView();
            populateOutputListView();
            populateFileTableListView();
            populateSymbolTableView();
            populateExecutionStackListView();
            populateLockTable(currentProgramState);
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
        numberOfProgramStatesTextField.setText("0");
    }

    private void populateNumberOfProgramStatesTextField() {
        numberOfProgramStatesTextField.setText(String.valueOf(controller.getProgramStates().size()));
    }

    private void populateHeapTableView() {
        ProgramState ps = getCurrentProgramState();
        if (ps == null) return;
        ArrayList<Pair<Integer, IValue>> entries = new ArrayList<>();
        for (Map.Entry<Integer, IValue> e : ps.getHeap().getContent().entrySet())
            entries.add(new Pair<>(e.getKey(), e.getValue()));
        heapTableView.setItems(FXCollections.observableArrayList(entries));
    }

    private void populateOutputListView() {
        ProgramState ps = getCurrentProgramState();
        if (ps == null) return;
        List<String> out = ps.getOutput().getAll().stream().map(Object::toString).collect(Collectors.toList());
        outputListView.setItems(FXCollections.observableArrayList(out));
    }

    private void populateFileTableListView() {
        ProgramState ps = getCurrentProgramState();
        if (ps == null) return;
        fileTableListView.setItems(FXCollections.observableArrayList(Arrays.asList(ps.getFileTable().getAllKeys())));
    }

    private void populateProgramStateIdentifiersListView() {
        List<Integer> ids = controller.getProgramStates().stream().map(ProgramState::getId).collect(Collectors.toList());
        programStateIdentifiersListView.setItems(FXCollections.observableList(ids));

        // FIX: daca nu e nimic selectat, selecteaza primul
        if (!ids.isEmpty() && programStateIdentifiersListView.getSelectionModel().getSelectedItem() == null) {
            programStateIdentifiersListView.getSelectionModel().selectFirst();
        }

        populateNumberOfProgramStatesTextField();
    }

    private void populateSymbolTableView() {
        ProgramState ps = getCurrentProgramState();
        if (ps == null) return;
        ArrayList<Pair<String, IValue>> entries = new ArrayList<>();
        for (Map.Entry<String, IValue> e : ps.getSymbolTable().getContent().entrySet())
            entries.add(new Pair<>(e.getKey(), e.getValue()));
        symbolTableView.setItems(FXCollections.observableArrayList(entries));
    }

    private void populateExecutionStackListView() {
        ProgramState ps = getCurrentProgramState();
        if (ps == null) return;
        List<String> stack = ps.getExecutionStack().getReversed().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        executionStackListView.setItems(FXCollections.observableList(stack));
    }
    private void populateLockTable(ProgramState state) {
        ObservableList<LockTableEntry> data = FXCollections.observableArrayList();
        state.getLockTable().getContent().forEach((key, value) -> {
            data.add(new LockTableEntry(key, value));
        });
        lockTableView.setItems(data);
    }


    // FIX: FXML are onAction="#runOneStep", deci trebuie ActionEvent

    @FXML
    private void runOneStep(ActionEvent actionEvent) {
        if (controller == null) return;

        try {
            List<ProgramState> prgList = controller.getProgramStates();
            if (!prgList.isEmpty()) {
                controller.oneStepForAllPrg(prgList);
                populate();   // IMPORTANT: afisam starea actuala

                // daca nu mai exista executie activa
                if (controller.getProgramStates().isEmpty()) {
                    runOneStepButton.setDisable(true);
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("Executia s-a terminat!");
                    a.showAndWait();
                }
            } else {
                runOneStepButton.setDisable(true);
            }
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
    }

}
