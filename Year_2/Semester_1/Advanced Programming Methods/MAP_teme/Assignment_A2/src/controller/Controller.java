package controller;

import exceptions.EmptyExecutionStackException;
import exceptions.MyException;
import model.state.IExecutionStack;
import model.state.ProgramState;
import model.statement.IStatement;
import repository.IRepository;


public class Controller {
    IRepository repository;

    public Controller(IRepository repository) {
        this.repository = repository;
    }

    public ProgramState oneStep(ProgramState state) throws MyException {
        IExecutionStack<IStatement> stack = state.getExecutionStack();
        if (stack.isEmpty())
            throw new EmptyExecutionStackException("Program state stack is empty!");
        IStatement crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    public void completeExecution(ProgramState state) throws MyException {
        ProgramState programState = repository.getCurrentProgram();

        System.out.println("Initial program state:" + programState);

        while (!programState.getExecutionStack().isEmpty()) {
            oneStep(programState);

            System.out.println("Program state after step:");
            System.out.println(programState);
        }
        System.out.println("Final program state:" + programState);
    }

    private void displayProgramState(ProgramState state) {
        System.out.println("Execution Stack: " + state.getExecutionStack());
        System.out.println("Symbol Table: " + state.getSymbolTable());
        System.out.println("Output: " + state.getOutput());
        System.out.println("--------------------------------------------------");
    }
}




