package model.statement;


import exceptions.MyException;
import model.state.ProgramState;

public interface IStatement {
    ProgramState execute(ProgramState state) throws MyException;
}
