package repository;

import exceptions.InterpreterException;

import model.state.ProgramState;

import java.io.IOException;

public interface IRepository {
    ProgramState getCurrentProgram();

    void logPrgStateExec(ProgramState programState) throws IOException, InterpreterException;


    String getLogFilePath();
}
