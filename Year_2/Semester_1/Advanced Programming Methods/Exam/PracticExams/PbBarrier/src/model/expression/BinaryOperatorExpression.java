package model.expression;

import exceptions.DivisionByZeroException;
import exceptions.MyException;
import exceptions.TypeMismatchException;
import exceptions.UndefinedOperatorException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.type.IType;
import model.type.IntegerType;
import model.type.BooleanType;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.IValue;


public record BinaryOperatorExpression(String operator, IExpression left, IExpression right) implements IExpression {

    @Override
    public IValue evaluate(ISymbolTable<String, IValue> symbolTable, IHeap heap) throws MyException {
        IValue leftTerm = left.evaluate(symbolTable, heap);
        IValue rightTerm = right.evaluate(symbolTable, heap);

        switch (operator) {
            case "+", "-", "*", "/" -> {
                checkTypes(leftTerm, rightTerm, new IntegerType());
                IntegerValue leftValueI = (IntegerValue) leftTerm;
                IntegerValue rightValueI = (IntegerValue) rightTerm;

                if (operator.equals("/") && rightValueI.value() == 0)
                    throw new DivisionByZeroException("Division by zero!");

                return evaluateArithmeticExpression(leftValueI, rightValueI);
            }

            case "&&", "||" -> {
                checkTypes(leftTerm, rightTerm, new BooleanType());
                BooleanValue leftValueB = (BooleanValue) leftTerm;
                BooleanValue rightValueB = (BooleanValue) rightTerm;
                return evaluateBooleanExpression(leftValueB, rightValueB);
            }

            default -> throw new UndefinedOperatorException("Undefined operator: " + operator);
        }
    }

    private void checkTypes(IValue leftTerm, IValue rightTerm, IType expectedType) {
        if (!leftTerm.getType().equals(expectedType) || !rightTerm.getType().equals(expectedType)) {
            throw new TypeMismatchException("Wrong types for operator '" + operator +
                    "': left=" + leftTerm.getType() + ", right=" + rightTerm.getType());
        }
    }

    private IntegerValue evaluateArithmeticExpression(IntegerValue leftValueI, IntegerValue rightValueI) {
        return switch (operator) {
            case "+" -> new IntegerValue(leftValueI.value() + rightValueI.value());
            case "-" -> new IntegerValue(leftValueI.value() - rightValueI.value());
            case "*" -> new IntegerValue(leftValueI.value() * rightValueI.value());
            case "/" -> new IntegerValue(leftValueI.value() / rightValueI.value());
            default -> throw new UndefinedOperatorException("Undefined arithmetic operator: " + operator);
        };
    }

    private BooleanValue evaluateBooleanExpression(BooleanValue leftValueB, BooleanValue rightValueB) {
        return switch (operator) {
            case "&&" -> new BooleanValue(leftValueB.value() && rightValueB.value());
            case "||" -> new BooleanValue(leftValueB.value() || rightValueB.value());
            default -> throw new UndefinedOperatorException("Undefined boolean operator: " + operator);
        };
    }

    @Override
    public IExpression deepCopy() {
        return new BinaryOperatorExpression(operator, left.deepCopy(), right.deepCopy());
    }

    @Override
    public IType typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        IType type1 = left.typecheck(typeEnv);
        IType type2 = right.typecheck(typeEnv);

        if (operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/")) {
            if (type1.equals(new IntegerType())) {
                if (type2.equals(new IntegerType())) {
                    return new IntegerType();
                } else {
                    throw new MyException("Second operand of arithmetic expression is not an integer.");
                }
            } else {
                throw new MyException("First operand of arithmetic expression is not an integer.");
            }
        } else if (operator.equals("&&") || operator.equals("||")) {
            if (type1.equals(new BooleanType())) {
                if (type2.equals(new BooleanType())) {
                    return new BooleanType();
                } else {
                    throw new MyException("Second operand of logical expression is not a boolean.");
                }
            } else {
                throw new MyException("First operand of logical expression is not a boolean.");
            }
        } else {
            throw new MyException("Undefined operator: " + operator);
        }
    }
}
