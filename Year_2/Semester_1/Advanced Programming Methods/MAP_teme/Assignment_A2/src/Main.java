import controller.Controller;
import exceptions.MyException;
import model.expression.*;
import model.statement.*;
import model.state.*;
import model.type.Type;
import model.value.*;
import repository.IRepository;
import repository.Repository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {

            //=======================
            // 🔹 Programul 1:
            // int v; v = 2; print(v);
            //========================
            IStatement ex1 = new CompoundStatement(
                    new VariableDeclarationStatement("v", Type.INTEGER),
                    new CompoundStatement(
                            new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                            new PrintStatement(new VariableExpression("v"))
                    )
            );

            // ================================================
            // 🔹 Programul 2:
            // int a; int b; a = 2 + 3 * 5; b = a + 1; print(b);
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
            // 🔹 Programul 3:
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

            // Program 4: demonstrație variabilă nedeclarată
// y = 5; print(y)
            IStatement ex4 = new CompoundStatement(
                    new AssignmentStatement("y", new ConstantExpression(new IntegerValue(5))),
                    new PrintStatement(new VariableExpression("y"))
            );

            //MENIU
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n=== MENIU PRINCIPAL ===");
                System.out.println("1. Program 1: int v; v=2; print(v)");
                System.out.println("2. Program 2: int a; int b; a=2+3*5; b=a+1; print(b)");
                System.out.println("3. Program 3: bool a; int v; a=true; if (a) then v=2 else v=3; print(v)");
                System.out.println("4. Program 4:");
                System.out.println("0. Iesire");
                System.out.print("Alege programul: ");

                String input = scanner.nextLine().trim();
                int option;

                try {
                    option = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Optiune invalida! Trebuie sa introduci un numar.");
                    continue; // reia meniul
                }


                if (option == 0) {
                    System.out.println("La revedere!");
                    break;
                }

                IStatement selectedProgram = switch (option) {
                    case 1 -> ex1;
                    case 2 -> ex2;
                    case 3 -> ex3;
                    case 4 -> ex4;
                    default -> {
                        System.out.println("Optiune invalida! Alege 0, 1, 2 sau 3.");
                        yield null;
                    }
                };

                if (selectedProgram != null) {
                    runProgram(selectedProgram);
                }
            }

        } catch (Exception e) {
            System.err.println("Eroare generala: " + e.getMessage());
        }
    }

    private static void runProgram(IStatement program) throws MyException {
        IExecutionStack<IStatement> exeStack = new StackExecutionStack<>();
        ISymbolTable<String, IValue> symTable = new MapSymbolTable<>();
        IOut<IValue> output = new ListOut<>();

        ProgramState prg = new ProgramState(exeStack, symTable, output, program);
        IRepository repo = new Repository(prg);
        Controller ctrl = new Controller(repo);

        System.out.println("\n=== EXECUȚIA PROGRAMULUI ===");
        ctrl.completeExecution(prg);
        System.out.println("\nRezultatul final: " + output.getAll());
    }
}
