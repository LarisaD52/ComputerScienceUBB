package controller;

import exceptions.EmptyExecutionStackException;
import exceptions.InterpreterException;
import exceptions.MyException;
import model.state.*;
import model.statement.IStatement;
import repository.IRepository;

import java.io.IOException;


public class Controller implements IController {
    IRepository repository;

    public IRepository getRepository() {
        return repository;
    }


    public Controller(IRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProgramState oneStep(ProgramState state) throws MyException {
        IExecutionStack<IStatement> stack = state.getExecutionStack();
        if (stack.isEmpty())
            throw new EmptyExecutionStackException("Program state stack is empty!");
        IStatement crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    @Override
    public void allStep() throws MyException, InterpreterException, IOException {
        ProgramState prg = repository.getCurrentProgram();
        repository. logPrgStateExec(prg);
        while (!prg.getExecutionStack().isEmpty()){
            oneStep(prg);
            repository. logPrgStateExec(prg);
        }}


    public ProgramState copyProgramState(ProgramState original) throws MyException {
        IStatement originalProgram = original.getOriginalProgram();
        IStatement copiedProgram = originalProgram.deepCopy(); // folosim deep copy pe statement-ul principal
        return new ProgramState(
                new StackExecutionStack<>(),
                new MapSymbolTable<>(),
                new ListOut<>(),
                copiedProgram,
                new MapFileTable());
    }

}




