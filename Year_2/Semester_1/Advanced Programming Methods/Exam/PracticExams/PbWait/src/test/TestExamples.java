package test;

import controller.Controller;
import exceptions.InterpreterException;
import exceptions.MyException;
import model.expression.*;
import model.statement.*;
import model.state.*;
import model.type.IntegerType;
import model.type.StringType;
import model.value.*;

import repository.Repository;

import java.io.BufferedReader;
import java.io.IOException;

public class TestExamples {

    /*
        string varf;
        varf="test.in";
        openRFile(varf);
        int varc;
        readFile(varf,varc);print(varc);
        readFile(varf,varc);print(varc)
        closeRFile(varf)
    */

    public static void main(String[] args) throws MyException, InterpreterException, IOException {
        IStatement program = new CompoundStatement(
                new VariableDeclarationStatement("varf", new StringType()),
                new CompoundStatement(
                        new AssignmentStatement("varf", new ConstantExpression(new StringValue("src/test/test.in"))),
                        new CompoundStatement(
                                new OpenRFileStatement(new VariableExpression("varf")),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("varc", new IntegerType()),
                                        new CompoundStatement(
                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(
                                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(
                                                                        new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseRFileStatement(new VariableExpression("varf"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        IExecutionStack<IStatement> exeStack = new StackExecutionStack<>();
        ISymbolTable<String, IValue> symTable = new MapSymbolTable<>();
        IOut<IValue> output = new ListOut<>();
        IFileTable<String, BufferedReader> fileTable = new MapFileTable();

        ProgramState prgState = new ProgramState(exeStack, symTable, output, program, fileTable);
        Repository repo = new Repository(prgState, "exemplu_de_la_2.9.txt");
        Controller ctrl = new Controller(repo);


        try {
            ctrl.allStep();
        } catch (Exception e) {
            System.out.println("Eroare: " + e.getMessage());
        } finally {
            for (String file : prgState.getFileTable().getAllKeys()) {
                try {
                    prgState.getFileTable().get(file).close();
                } catch (IOException ignored) {}
            }
            prgState.getFileTable().clear();
        }

    }

}
