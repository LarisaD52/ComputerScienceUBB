import controller.Controller;
import model.expression.*;
import model.statement.*;
import model.state.*;
import model.type.Type;
import model.value.*;
import repository.IRepository;
import repository.Repository;
import view.ExitCommand;
import view.RunExample;
import view.TextMenu;
import model.state.MapFileTable;


public class Interpreter {
    public static void main(String[] args) {

        //=======================
        // -->Programul 1:
        // int v;
        // v = 2;
        // print(v);
        //========================
        IStatement ex1 = new CompoundStatement(new VariableDeclarationStatement("v", Type.INTEGER),
                new CompoundStatement(
                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                        new PrintStatement(new VariableExpression("v"))
                )
        );


        // ================================================
        // -->Programul 2:
        // int a;
        // int b;
        // a = 2 + 3 * 5;
        // b = a + 1;
        // print(b);
        // =================================================
        IStatement ex2 = new CompoundStatement(
                new VariableDeclarationStatement("a", Type.INTEGER),
                new CompoundStatement(
                        new VariableDeclarationStatement("b", Type.INTEGER),
                        new CompoundStatement(
                                new AssignmentStatement("a",
                                        new BinaryOperatorExpression("+",
                                                new ConstantExpression(new IntegerValue(2)),
                                                new BinaryOperatorExpression("*",
                                                        new ConstantExpression(new IntegerValue(3)),
                                                        new ConstantExpression(new IntegerValue(5)))
                                        )
                                ),
                                new CompoundStatement(
                                        new AssignmentStatement("b",
                                                new BinaryOperatorExpression("+",
                                                        new VariableExpression("a"),
                                                        new ConstantExpression(new IntegerValue(1)))
                                        ),
                                        new PrintStatement(new VariableExpression("b"))
                                )
                        )
                )
        );



        //==============================
        // --> Programul 3:
        // bool a; int v; a = true;
        // if (a) then v = 2 else v = 3;
        // print(v);
        //==============================
        IStatement ex3 = new CompoundStatement(
                new VariableDeclarationStatement("a", Type.BOOLEAN),
                new CompoundStatement(
                        new VariableDeclarationStatement("v", Type.INTEGER),
                        new CompoundStatement(
                                new AssignmentStatement("a", new ConstantExpression(new BooleanValue(true))),
                                new CompoundStatement(
                                        new IfStatement(
                                                new VariableExpression("a"),
                                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(3)))
                                        ),
                                        new PrintStatement(new VariableExpression("v"))
                                )
                        )
                )
        );


        //=====================================
        // --> Program 4: variabila nedeclarata
        // y = 5; print(y)
        //=====================================
        IStatement ex4 = new CompoundStatement(
                new AssignmentStatement("y", new ConstantExpression(new IntegerValue(5))),
                new PrintStatement(new VariableExpression("y"))
        );



        //==================
        // --> Program 5:
        // if (a < 10) then v=2 else v=3; print(v)
        //========================================
        IStatement ex5 = new CompoundStatement(
                new VariableDeclarationStatement("a", Type.INTEGER),
                new CompoundStatement(
                        new VariableDeclarationStatement("v", Type.INTEGER),
                        new CompoundStatement(
                                new AssignmentStatement("a", new ConstantExpression(new IntegerValue(12))),
                                new CompoundStatement(
                                        new IfStatement(
                                                new RelationalExpression("<",
                                                        new VariableExpression("a"),
                                                        new ConstantExpression(new IntegerValue(10))
                                                ),
                                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(3)))
                                        ),
                                        new PrintStatement(new VariableExpression("v"))
                                )
                        )
                )
        );


        ProgramState prg1 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex1, new MapFileTable());
        IRepository repo1 = new Repository(prg1, "log1.txt");
        Controller ctr1 = new Controller(repo1);

        ProgramState prg2 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex2, new MapFileTable());
        IRepository repo2 = new Repository(prg2, "log2.txt");
        Controller ctr2 = new Controller(repo2);

        ProgramState prg3 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex3, new MapFileTable());
        IRepository repo3 = new Repository(prg3, "log3.txt");
        Controller ctr3 = new Controller(repo3);

        ProgramState prg4 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex4, new MapFileTable());
        IRepository repo4 = new Repository(prg4, "log4.txt");
        Controller ctr4 = new Controller(repo4);

        ProgramState prg5 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex5, new MapFileTable());
        IRepository repo5 = new Repository(prg5, "log5.txt");
        Controller ctr5 = new Controller(repo5);


        //======TextMenu=====
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "Iesire"));
        menu.addCommand(new RunExample("1", "Program 1: int v; v=2; print(v)", ctr1));
        menu.addCommand(new RunExample("2", "Program 2: int a; int b; a=2+3*5; b=a+1; print(b)", ctr2));
        menu.addCommand(new RunExample("3", "Program 3: bool a; int v; a=true; if (a) then v=2 else v=3; print(v)", ctr3));
        menu.addCommand(new RunExample("4", "Program 4: y = 5; print(y)", ctr4));
        menu.addCommand(new RunExample("5", "Program 5: if (a < 10) then v=2 else v=3; print(v)", ctr5));
        menu.show();
    }
}

