package repository;

import exceptions.InterpreterException;

import model.state.ProgramState;

import java.io.IOException;
import java.util.List;

public interface IRepository {

    void logPrgStateExec(ProgramState programState) throws IOException, InterpreterException;

    List<ProgramState> getPrgList();

    void setPrgList(List<ProgramState> prgList);

    String getLogFilePath();
}
