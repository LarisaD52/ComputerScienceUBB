package model.statement;

import exceptions.MyException;
import exceptions.NotBoolean;
import model.expression.IExpression;
import model.state.IExecutionStack;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.IType;
import model.value.BooleanValue;
import model.value.IValue;

public class WhileStatement implements IStatement {
    private final IExpression condition;
    private final IStatement body;

    public WhileStatement(IExpression condition, IStatement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IExecutionStack<IStatement> stack = state.getExecutionStack();
        IValue conditionValue = condition.evaluate(state.getSymbolTable(),  state.getHeap());

        if (!(conditionValue instanceof BooleanValue(boolean value)))
            throw new NotBoolean("Condition in WHILE is not a boolean expression.");

        if (value) {
            stack.push(this); //pentru a veridica din nou conditia dupa body
            stack.push(body); //executam corpul
        }
        return null;
    }




    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        // 1. Verifica tipul condiției
        IType typexp = condition.typecheck(typeEnv);
        if (!typexp.equals(new BooleanType())) {
            throw new MyException("The condition of WHILE has not the type bool.");
        }

        // 2. Verifica tipul corpului (nu afecteaza typeEnv-ul exterior)
        // Nu este necesar clone-ul aici, deoarece nu se adauga variabile noi
        body.typecheck(typeEnv);

        // 3. Returneaza mediul de tipuri neschimbat
        return typeEnv;
    }
    @Override
    public IStatement deepCopy() {
        return new  WhileStatement(condition.deepCopy(), body.deepCopy());
    }
}
