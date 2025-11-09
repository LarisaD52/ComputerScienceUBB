package test;

import controller.Controller;
import exceptions.MyException;
import model.expression.*;
import model.statement.*;
import model.state.*;
import model.type.Type;
import model.value.*;

import repository.Repository;

import java.io.BufferedReader;

public class TestFile1 {


    /*
        string varf;
        varf="test.in";
        openRFile(varf);
        int varc;
        readFile(varf,varc);print(varc);
        readFile(varf,varc);print(varc)
        closeRFile(varf)
    */

    public static void main(String[] args) {
        try{
            IStatement program = new CompoundStatement(
                    new VariableDeclarationStatement("varf", Type.STRING), //string = "varf"
                    new CompoundStatement(
                            new AssignmentStatement("varf", new ConstantExpression(new StringValue("src/test/test.in"))),
                            new CompoundStatement(
                                    new OpenRFileStatement(new VariableExpression("varf")),
                                    new CompoundStatement(
                                            new VariableDeclarationStatement("varc", Type.INTEGER),
                                            new CompoundStatement(
                                                    new ReadFileStatement(new VariableExpression("varf"),"varc"),
                                                    new CompoundStatement(
                                                            new PrintStatement(new VariableExpression("varc")),
                                                            new CompoundStatement(
                                                                    new ReadFileStatement(new VariableExpression("varf"),"varc"),
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


            Repository repo = new Repository(prgState, "log.txt");
            Controller ctrl = new Controller(repo);


            ctrl.allStep(); //pentru a executa

            System.out.println("Output final: " + output.getAll());



        }catch (MyException e){
            System.err.println("Interpreter error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


