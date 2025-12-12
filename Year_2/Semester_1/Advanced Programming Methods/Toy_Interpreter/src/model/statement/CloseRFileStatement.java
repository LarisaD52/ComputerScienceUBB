package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.IFileTable;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;
import model.type.StringType;
import model.value.IValue;
import model.value.StringValue;
import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStatement implements IStatement{
    private final IExpression expression;
    public CloseRFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IStatement deepCopy() {
        return new CloseRFileStatement(expression.deepCopy());
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        IValue val = expression.evaluate(state.getSymbolTable(), state.getHeap());

        if (!(val.getType() instanceof IType)) {
            throw new MyException("CloseRFile: Expression does not evaluate to a string.");
        }

        String fileName = ((StringValue) val).getVal();
        IFileTable<String, BufferedReader> fileTable = state.getFileTable();

        if (!fileTable.isOpen(fileName)) {
            throw new MyException("CloseRFile: File '" + fileName + "' is not opened.");
        }

        //get BufferedReader and close it
        BufferedReader br = fileTable.get(fileName);
        try {
            br.close();
        } catch (IOException _) {
            throw new MyException("CloseRFile: Error closing file '" + fileName + "'.");
        }

        fileTable.remove(fileName);

        return null;
    }



    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        //Expresia trebuie sa se evalueze la StringType (numele fisierului)
        IType typexp = expression.typecheck(typeEnv);
        if (!typexp.equals(new StringType())) {
            throw new MyException("CloseRFile: Expression must evaluate to StringType.");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "closeRFile(" + expression + ")";
    }
}




