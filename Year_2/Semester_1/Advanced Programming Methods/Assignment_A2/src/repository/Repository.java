package repository;

import exceptions.InterpreterException;
import model.state.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository{

    private final List<ProgramState> programStates;
    private final String logFilePath;

    public Repository(ProgramState initialProgram, String logFilePath) {
        this.programStates = new ArrayList<>();
        this.programStates.add(initialProgram);
        this.logFilePath= logFilePath;
    }

    @Override
    public ProgramState getCurrentProgram() {
        return programStates.get(0);
    }

    @Override
    public void logPrgStateExec(ProgramState programState) throws InterpreterException, IOException {
        PrintWriter logFile;
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        logFile.println(programState.toString());
        logFile.close();
    }

    @Override
    public String getLogFilePath() {
        return logFilePath;
    }

    @Override
    public String toString() {
        return programStates.toString();
    }
}