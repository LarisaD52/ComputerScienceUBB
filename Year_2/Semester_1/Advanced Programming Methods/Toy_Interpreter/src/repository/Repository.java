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

    private  List<ProgramState> programStates;
    private final String logFilePath;

    public Repository(ProgramState initialProgram, String logFilePath) {
        this.programStates = new ArrayList<>();
        this.programStates.add(initialProgram);
        this.logFilePath= logFilePath;


        // !!!!!am aduagat: curatarea fisierului log la initializare
        try {
            //deschid fișierul in modul de suprascriere (false) si il inchid imediat
            PrintWriter logFile = new PrintWriter(new FileWriter(logFilePath, false));
            logFile.close();
        } catch (IOException e) {
            System.err.println("Eroare la curatarea fisierului log: " + logFilePath + ": " + e.getMessage());
        }


    }

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


    public List<ProgramState> getPrgList() {
        return programStates;
    }

    @Override
    public void setPrgList(List<ProgramState> prgList) {
        this.programStates = prgList;
    }

    @Override
    public String toString() {
        return programStates.toString();
    }
}