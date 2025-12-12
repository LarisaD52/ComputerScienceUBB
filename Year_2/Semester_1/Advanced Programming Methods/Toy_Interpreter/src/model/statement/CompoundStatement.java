package model.statement;

import exceptions.MyException;
import exceptions.NotNull;
import model.state.IExecutionStack;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;

public class CompoundStatement implements IStatement {
    private final IStatement first;
    private final IStatement second;

    public CompoundStatement(IStatement first, IStatement second) throws NotNull {
        if (first == null || second == null) {
            throw new NotNull("CompoundStatement cannot have null statements");
        }
        this.first = first;
        this.second = second;
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(first.deepCopy(), second.deepCopy());
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IExecutionStack<IStatement> stack = state.getExecutionStack();
        stack.push(second);
        stack.push(first);
        return null;
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        //obtin mediul de tipuri dupa verificarea primei instructiuni
        ISymbolTable<String, IType> typeEnv1 = first.typecheck(typeEnv);

        //verific a doua instructiune folosind mediul actualizat
        ISymbolTable<String, IType> typeEnv2 = second.typecheck(typeEnv1);

        return typeEnv2; //mediul final
    }

    @Override

    public String toString() {
        String s1 = (first == null ? "null" : first.toString());
        String s2 = (second == null ? "null" : second.toString());
        return "(" + s1 + "; " + s2 + ")";
    }

}
