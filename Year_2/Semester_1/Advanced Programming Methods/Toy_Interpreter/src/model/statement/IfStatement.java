package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.IExecutionStack;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.IType;
import model.value.BooleanValue;
import model.value.IValue;

public class IfStatement implements IStatement {
    private final IExpression condition;
    private final IStatement thenStatement;
    private final IStatement elseStatement;

    public IfStatement(IExpression condition, IStatement thenStatement, IStatement elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(condition.deepCopy(), thenStatement.deepCopy(), elseStatement.deepCopy());
    }


    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IExecutionStack<IStatement> stack = state.getExecutionStack();
        IValue conditionValue = condition.evaluate(state.getSymbolTable(), state.getHeap());

        if (!(conditionValue instanceof BooleanValue boolVal))
            throw new MyException("Condition in IF is not a boolean expression.");

        if (boolVal.value())
            stack.push(thenStatement);
        else
            stack.push(elseStatement);

        return null;
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        // 1. Condiția trebuie să fie Boolean
        IType typexp = condition.typecheck(typeEnv);
        if (typexp.equals(new BooleanType())) {
            // 2. Verifică ramura THEN (clone() este important pentru a izola declarațiile de variabile)
            thenStatement.typecheck(typeEnv.deepCopy()); // Presupunând că ISymbolTable are deepCopy/clone

            // 3. Verifică ramura ELSE
            elseStatement.typecheck(typeEnv.deepCopy()); // Presupunând că ISymbolTable are deepCopy/clone

            // 4. Returnează mediul inițial
            return typeEnv;
        } else {
            throw new MyException("The condition of IF has not the type bool.");
        }
    }

    @Override
    public String toString() {
        return "(IF(" + condition + ") THEN(" + thenStatement + ") ELSE(" + elseStatement + "))";
    }
}
