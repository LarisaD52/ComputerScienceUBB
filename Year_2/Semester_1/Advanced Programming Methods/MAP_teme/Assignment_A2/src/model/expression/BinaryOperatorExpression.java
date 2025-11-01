package model.expression;

import exceptions.DivisionByZeroException;
import exceptions.MyException;
import exceptions.TypeMismatchException;
import exceptions.UndefinedOperatorException;
import model.state.ISymbolTable;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.IValue;

public record BinaryOperatorExpression (String operator, IExpression left, IExpression right) implements IExpression{

    @Override
    public IValue evaluate(ISymbolTable symbolTable) throws MyException {
        var leftTerm = left.evaluate(symbolTable);
        var rightTerm = right.evaluate(symbolTable);

        switch (operator) {
            case "+", "-", "*", "/":
                checkTypes(leftTerm, rightTerm, Type.INTEGER);
                var leftValueI = (IntegerValue) leftTerm;
                var rightValueI = (IntegerValue) rightTerm;

                if (operator.equals("/") && rightValueI.value() == 0)
                    throw new DivisionByZeroException("Division by zero!");

                return evaluateArithmeticExpression(leftValueI, rightValueI);

            case "&&", "||":
                checkTypes(leftTerm, rightTerm, Type.BOOLEAN);
                var leftValueB = (BooleanValue) leftTerm;
                var rightValueB = (BooleanValue) rightTerm;
                return evaluateBooleanExpression(leftValueB, rightValueB);

            default:
                throw new UndefinedOperatorException("Undefined operator: " + operator);
        }
    }


    private void checkTypes(IValue leftTerm, IValue rightTerm, Type type) {
        if (leftTerm.getType() != type || rightTerm.getType() != type){
            throw new TypeMismatchException("Wrong types for operator: " + operator);
        }
    }

    private IntegerValue evaluateArithmeticExpression(IntegerValue leftValueI, IntegerValue rightValueI) {
        return switch (operator){
            case "+" -> new IntegerValue(leftValueI.value() + rightValueI.value());
            case "-" -> new IntegerValue(leftValueI.value() - rightValueI.value());
            case "*" -> new IntegerValue(leftValueI.value() * rightValueI.value());
            case "/" -> new IntegerValue(leftValueI.value() / rightValueI.value());
            default -> throw new UndefinedOperatorException("Undefined arithmetic operator: " + operator);
        };
    }
    private BooleanValue evaluateBooleanExpression(BooleanValue leftValueB, BooleanValue rightValueB) {
        return switch (operator){
            case "&&" -> new BooleanValue(leftValueB.value() && rightValueB.value());
            case "||" -> new BooleanValue(leftValueB.value() || rightValueB.value());
            default -> throw new UndefinedOperatorException("Undefined boolean operator: " + operator);
        };
    }
}
