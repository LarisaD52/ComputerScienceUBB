package controller;

import exceptions.InterpreterException;
import exceptions.MyException;
import model.state.ProgramState;

import java.io.IOException;

public interface IController {
    ProgramState oneStep(ProgramState state) throws MyException;
    void allStep() throws MyException, InterpreterException, IOException;
    ProgramState copyProgramState (ProgramState original) throws MyException;
}
