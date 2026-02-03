package view.CLI;

import controller.Controller;

import exceptions.MyException;
import model.expression.*;
import model.statement.*;
import model.state.*;
import model.type.BooleanType;
import model.type.IType;
import model.type.IntegerType;
import model.type.RefType;
import model.value.*;
import repository.IRepository;
import repository.Repository;
import view.Command.ExitCommand;
import view.Command.RunExample;
import model.state.MapFileTable;
import model.statement.NewStatement;
import view.Command.TextMenu;




public class Interpreter {
    public static void main(String[] args) {


        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));

        // =======================
        // -->Programul 1:
        // int v;
        // v = 2;
        // print(v);
        // ========================
        IStatement ex1 =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                                new PrintStatement(new VariableExpression("v"))));
        try {

            ex1.typecheck(new MapSymbolTable<String, IType>());

            ProgramState prg1 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex1, new MapFileTable());
            IRepository repo1 = new Repository(prg1, "log1.txt");
            Controller ctr1 = new Controller(repo1);
            menu.addCommand(new RunExample("1", "Program 1: int v; v=2; print(v)", ctr1));
        } catch (MyException e) {
            System.out.println("Program 1 Eșuat la Type Check/Inițializare: " + e.getMessage());
        }


        // ================================================
        // -->Programul 2:
        // int a;
        // int b;
        // a = 2 + 3 * 5;
        // b = a + 1;
        // print(b);
        // =================================================
        IStatement ex2 =
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new IntegerType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("b", new IntegerType()),
                                new CompoundStatement(
                                        new AssignmentStatement(
                                                "a",
                                                new BinaryOperatorExpression(
                                                        "+",
                                                        new ConstantExpression(new IntegerValue(2)),
                                                        new BinaryOperatorExpression(
                                                                "*",
                                                                new ConstantExpression(new IntegerValue(3)),
                                                                new ConstantExpression(new IntegerValue(5))))),
                                        new CompoundStatement(
                                                new AssignmentStatement(
                                                        "b",
                                                        new BinaryOperatorExpression(
                                                                "+",
                                                                new VariableExpression("a"),
                                                                new ConstantExpression(new IntegerValue(1)))),
                                                new PrintStatement(new VariableExpression("b"))))));
        try {
            ex2.typecheck(new MapSymbolTable<String, IType>());
            ProgramState prg2 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex2, new MapFileTable());
            IRepository repo2 = new Repository(prg2, "log2.txt");
            Controller ctr2 = new Controller(repo2);
            menu.addCommand(new RunExample("2", "Program 2: int a; int b; a=2+3*5; b=a+1; print(b)", ctr2));
        } catch (MyException e) {
                System.out.println(e.getMessage());
    }


        // ==============================
        // --> Programul 3:
        // bool a; int v; a = true;
        // if (a) then v = 2 else v = 3;
        // print(v);
        // ==============================
        IStatement ex3 =
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new BooleanType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("v", new IntegerType()),
                                new CompoundStatement(
                                        new AssignmentStatement("a", new ConstantExpression(new BooleanValue(true))),
                                        new CompoundStatement(
                                                new IfStatement(
                                                        new VariableExpression("a"),
                                                        new AssignmentStatement(
                                                                "v", new ConstantExpression(new IntegerValue(2))),
                                                        new AssignmentStatement(
                                                                "v", new ConstantExpression(new IntegerValue(3)))),
                                                new PrintStatement(new VariableExpression("v"))))));
        try {
            ex3.typecheck(new MapSymbolTable<String, IType>());
            ProgramState prg3 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex3, new MapFileTable());
            IRepository repo3 = new Repository(prg3, "log3.txt");
            Controller ctr3 = new Controller(repo3);
            menu.addCommand(new RunExample("3", "Program 3: bool a; int v; a=true; if (a) then v=2 else v=3; print(v)", ctr3));
        } catch (MyException e) {
            System.out.println("Program 3 Eșuat la Type Check/Inițializare: " + e.getMessage());
        }
        // =====================================
        // --> Program 4: variabila nedeclarata
        // y = 5; print(y)
        // =====================================
        IStatement ex4 =
                new CompoundStatement(
                        new AssignmentStatement("y", new ConstantExpression(new IntegerValue(5))),
                        new PrintStatement(new VariableExpression("y")));

        try {

            ex4.typecheck(new MapSymbolTable<String, IType>());
            ProgramState prg4 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex4, new MapFileTable());
            IRepository repo4 = new Repository(prg4, "log4.txt");
            Controller ctr4 = new Controller(repo4);
            menu.addCommand(new RunExample("4", "Program 4: y = 5; print(y)", ctr4));
        } catch (MyException e) {
            System.err.println("Program 4 Eșuat la Type Check/Inițializare: " + e.getMessage());
        }

        // ==================
        // --> Program 5:
        // if (a < 10) then v=2 else v=3; print(v)
        // ========================================
        IStatement ex5 =
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new IntegerType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("v", new IntegerType()),
                                new CompoundStatement(
                                        new AssignmentStatement("a", new ConstantExpression(new IntegerValue(12))),
                                        new CompoundStatement(
                                                new IfStatement(
                                                        new RelationalExpression(
                                                                "<",
                                                                new VariableExpression("a"),
                                                                new ConstantExpression(new IntegerValue(10))),
                                                        new AssignmentStatement(
                                                                "v", new ConstantExpression(new IntegerValue(2))),
                                                        new AssignmentStatement(
                                                                "v", new ConstantExpression(new IntegerValue(3)))),
                                                new PrintStatement(new VariableExpression("v"))))));

        try {

            ex5.typecheck(new MapSymbolTable<String, IType>());
            ProgramState prg5 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex5, new MapFileTable());
            IRepository repo5 = new Repository(prg5, "log5.txt");
            Controller ctr5 = new Controller(repo5);
            menu.addCommand(new RunExample("5",  "Program 5: if (a < 10) then v=2 else v=3; print(v)", ctr5));
        } catch (MyException e) {
            System.err.println("Program 5 Eșuat la Type Check/Inițializare: " + e.getMessage());
        }
        // ===============
        // --> Program 6
        // int v; v=4; (while (v>0) print(v);v=v-1);print(v)
        // =================================================
        IStatement ex6 =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(4))),
                                new CompoundStatement(
                                        new WhileStatement(
                                                new RelationalExpression(
                                                        ">",
                                                        new VariableExpression("v"),
                                                        new ConstantExpression(new IntegerValue(0))),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new AssignmentStatement(
                                                                "v",
                                                                new BinaryOperatorExpression(
                                                                        "-",
                                                                        new VariableExpression("v"),
                                                                        new ConstantExpression(new IntegerValue(1)))))),
                                        new PrintStatement(new VariableExpression("v")))));

        try {
            ex6.typecheck(new MapSymbolTable<String, IType>());
            ProgramState prg6 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex6, new MapFileTable());
            IRepository repo6 = new Repository(prg6, "log6.txt");
            Controller ctr6 = new Controller(repo6);
            menu.addCommand(new RunExample("6",  "Program 6: int v; v=4; (while (v>0) print(v);v=v-1);print(v)", ctr6));
        } catch (MyException e) {
            System.err.println("Program 6 Eșuat la Type Check/Inițializare: " + e.getMessage());
        }


        // ===========
        // --> Program 7:
        /// Ref int v;new(v,20);Ref Ref int a; new(a,v);print(v);print(a)
        // =============================================================
        IStatement ex7 =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                                new CompoundStatement(
                                        new VariableDeclarationStatement(
                                                "a", new RefType(new RefType(new IntegerType()))),
                                        new CompoundStatement(
                                                new NewStatement("a", new VariableExpression("v")),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new VariableExpression("a")))))));
        try {
            ex7.typecheck(new MapSymbolTable<String, IType>());
            ProgramState prg7 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex7, new MapFileTable());
            IRepository rep7 = new Repository(prg7, "log7.txt");
            Controller ctr7 = new Controller(rep7);
            menu.addCommand(new RunExample("7",  "Program 7: Ref int v;new(v,20);Ref Ref int a; new(a,v);print(v);print(a)", ctr7));
        } catch (MyException e) {
            System.err.println("Program 7 Eșuat la Type Check/Inițializare: " + e.getMessage());
        }

        // ================
        // --> Program 8:
        /// Ref int v;new(v,20);Ref Ref int a; new(a,v);print(rH(v));print(rH(rH(a))+5)
        // ==============================================================================
        IStatement ex8 =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                                new CompoundStatement(
                                        new VariableDeclarationStatement(
                                                "a", new RefType(new RefType(new IntegerType()))),
                                        new CompoundStatement(
                                                new NewStatement("a", new VariableExpression("v")),
                                                new CompoundStatement(
                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                        new PrintStatement(
                                                                new BinaryOperatorExpression(
                                                                        "+",
                                                                        new ReadHeapExpression(
                                                                                new ReadHeapExpression(new VariableExpression("a"))),
                                                                        new ConstantExpression(new IntegerValue(5)))))))));
        try {
            ex8.typecheck(new MapSymbolTable<String, IType>());
            ProgramState prg8 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex8, new MapFileTable());
            IRepository rep8 = new Repository(prg8, "log8.txt");
            Controller ctr8 = new Controller(rep8);
            menu.addCommand(new RunExample("8", "Program 8: Ref int v;new(v,20);Ref Ref int a; new(a,v);print(rH(v));print(rH(rH(a))+5", ctr8));
        } catch (MyException e) {
            System.err.println("Program 8 Eșuat la Type Check/Inițializare: " + e.getMessage());
        }

        // ================
        // --> Program 9:
        /// Ref int v;new(v,20);print(rH(v)); wH(v,30);print(rH(v)+5);
        // ================================================================
        IStatement ex9 =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                                new CompoundStatement(
                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                        new CompoundStatement(
                                                new WriteHeapStatement("v", new ConstantExpression(new IntegerValue(30))),
                                                new PrintStatement(
                                                        new BinaryOperatorExpression(
                                                                "+",
                                                                new ReadHeapExpression(new VariableExpression("v")),
                                                                new ConstantExpression(new IntegerValue(5))
                                                        )
                                                )
                                        )
                                )
                        )
                );
        try {
            ex9.typecheck(new MapSymbolTable<String, IType>());
            ProgramState prg9 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex9, new MapFileTable());
            IRepository rep9 = new Repository(prg9, "log9.txt");
            Controller ctr9 = new Controller(rep9);
            menu.addCommand(new RunExample("9", "Program 9: Ref int v;new(v,20);print(rH(v)); wH(v,30);print(rH(v)+5);", ctr9));
        } catch (MyException e) {
            System.err.println("Program 9 Eșuat la Type Check/Inițializare: " + e.getMessage());
        }





        //=========================
        // --> Program 10:
        // Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))
        //=======================================
        IStatement ex10 = new CompoundStatement(
                new VariableDeclarationStatement("v", new RefType(new IntegerType())),
                new CompoundStatement(
                        new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(
                                new VariableDeclarationStatement("a", new RefType(new RefType(new IntegerType()))),
                                new CompoundStatement(
                                        new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(
                                                new NewStatement("v", new ConstantExpression(new IntegerValue(30))),
                                                new PrintStatement(
                                                        new ReadHeapExpression(
                                                                new ReadHeapExpression(new VariableExpression("a"))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        try {
            ex10.typecheck(new MapSymbolTable<String, IType>());
            ProgramState prg10 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex10, new MapFileTable());
            IRepository rep10 = new Repository(prg10, "log10.txt");
            Controller ctr10 = new Controller(rep10,true);
            menu.addCommand(new RunExample("10", "Program 10: Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))", ctr10));
        } catch (MyException e) {
            System.err.println("Program 10 Eșuat la Type Check/Inițializare: " + e.getMessage());
        }





    // ===========
    // Program 11:
    // int v; Ref int a; v=10; new(a,22);
    // fork( wH(a,30); v=32; print(v); print(rH(a)) );
    // print(v); print(rH(a));
    //============================

        IStatement ex11 = new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("a", new RefType(new IntegerType())),
                                new CompoundStatement(
                                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(10))),
                                        new CompoundStatement(
                                                new NewStatement("a", new ConstantExpression(new IntegerValue(22))),
                                                new CompoundStatement(
                                                        new ForkStatement(
                                                                new CompoundStatement(
                                                                        new WriteHeapStatement("a", new ConstantExpression(new IntegerValue(30))),
                                                                        new CompoundStatement(
                                                                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(32))),
                                                                                new CompoundStatement(
                                                                                        new PrintStatement(new VariableExpression("v")),
                                                                                        new PrintStatement(
                                                                                                new ReadHeapExpression(
                                                                                                        new VariableExpression("a")
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                        new CompoundStatement(
                                                                new PrintStatement(new VariableExpression("v")),
                                                                new PrintStatement(
                                                                        new ReadHeapExpression(
                                                                                new VariableExpression("a")
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
        try {
            ex11.typecheck(new MapSymbolTable<String, IType>());
            ProgramState prg11 = new ProgramState(new StackExecutionStack<>(), new MapSymbolTable<>(), new ListOut<>(), ex11, new MapFileTable());
            IRepository rep11= new Repository(prg11, "log11.txt");
            Controller ctr11= new Controller(rep11,true);
            menu.addCommand(new RunExample("11", "Program 11: int v; Ref int a; v=10; new(a,22); fork( wH(a,30); v=32; print(v); print(rH(a)) ); print(v); print(rH(a));", ctr11));
        } catch (MyException e) {
            System.err.println("Program 11 Eșuat la Type Check/Inițializare: " + e.getMessage());
        }





        IStatement ex12 =
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new VariableDeclarationStatement("b", new RefType(new IntegerType())),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("v", new IntegerType()),
                                        new CompoundStatement(
                                                new NewStatement("a", new ConstantExpression(new IntegerValue(0))),
                                                new CompoundStatement(
                                                        new NewStatement("b", new ConstantExpression(new IntegerValue(0))),
                                                        new CompoundStatement(
                                                                new WriteHeapStatement("a",
                                                                        new ConstantExpression(new IntegerValue(1))),
                                                                new CompoundStatement(
                                                                        new WriteHeapStatement("b",
                                                                                new ConstantExpression(new IntegerValue(2))),
                                                                        new CompoundStatement(
                                                                                new ConditionalAssignmentStatement(
                                                                                        "v",
                                                                                        new RelationalExpression(
                                                                                                "<",
                                                                                                new ReadHeapExpression(
                                                                                                        new VariableExpression("a")),
                                                                                                new ReadHeapExpression(
                                                                                                        new VariableExpression("b"))
                                                                                        ),
                                                                                        new ConstantExpression(new IntegerValue(100)),
                                                                                        new ConstantExpression(new IntegerValue(200))
                                                                                ),
                                                                                new CompoundStatement(
                                                                                        new PrintStatement(
                                                                                                new VariableExpression("v")),
                                                                                        new CompoundStatement(
                                                                                                new ConditionalAssignmentStatement(
                                                                                                        "v",
                                                                                                        new RelationalExpression(
                                                                                                                ">",
                                                                                                                new BinaryOperatorExpression(
                                                                                                                        "-",
                                                                                                                        new ReadHeapExpression(
                                                                                                                                new VariableExpression("b")),
                                                                                                                        new ConstantExpression(
                                                                                                                                new IntegerValue(2))
                                                                                                                ),
                                                                                                                new ReadHeapExpression(
                                                                                                                        new VariableExpression("a"))
                                                                                                        ),
                                                                                                        new ConstantExpression(
                                                                                                                new IntegerValue(100)),
                                                                                                        new ConstantExpression(
                                                                                                                new IntegerValue(200))
                                                                                                ),
                                                                                                new PrintStatement(
                                                                                                        new VariableExpression("v"))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        try {
            ex12.typecheck(new MapSymbolTable<String, IType>());

            ProgramState prgCond = new ProgramState(
                    new StackExecutionStack<>(),
                    new MapSymbolTable<>(),
                    new ListOut<>(),
                    ex12,
                    new MapFileTable()
            );

            IRepository repCond = new Repository(prgCond, "log12.txt");
            Controller ctrCond = new Controller(repCond, true);

            menu.addCommand(new RunExample(
                    "12",
                    "Program Conditional Assignment",
                    ctrCond));

        } catch (MyException e) {
            System.err.println("Program ex12 Eșuat: " + e.getMessage());
        }



        menu.show();
    }
}
