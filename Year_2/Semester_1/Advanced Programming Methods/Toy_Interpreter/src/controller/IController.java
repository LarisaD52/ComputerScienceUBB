package controller;

import exceptions.InterpreterException;
import exceptions.MyException;
import model.state.ProgramState;

import java.io.IOException;
import java.util.List;

public interface IController {

    ProgramState copyProgramState (ProgramState original) throws MyException;

    List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) throws MyException;

    void allStep() throws MyException, InterpreterException, IOException;

    void oneStepForAllPrg(List<ProgramState> inPrgList)
            throws MyException, InterruptedException;

}
