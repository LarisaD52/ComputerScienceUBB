package model.statement;


import exceptions.MyException;
import model.expression.IExpression;
import model.state.IFileTable;
import model.state.ProgramState;

import model.type.Type;
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
        // 1. Evaluate the expression
        IValue val = expression.evaluate(state.getSymbolTable());

        // 2. Check if the value is a StringValue
        if (!(val.getType() instanceof Type)) {
            throw new MyException("CloseRFile: Expression does not evaluate to a string.");
        }

        String fileName = ((StringValue) val).getVal();
        IFileTable<String, BufferedReader> fileTable = state.getFileTable();

        // 3. Check if file exists in FileTable
        if (!fileTable.isOpen(fileName)) {
            throw new MyException("CloseRFile: File '" + fileName + "' is not opened.");
        }

        // 4. Get BufferedReader and close it
        BufferedReader br = fileTable.get(fileName);
        try {
            br.close();
        } catch (IOException _) {
            throw new MyException("CloseRFile: Error closing file '" + fileName + "'.");
        }

        // 5. Remove entry from FileTable
        fileTable.remove(fileName);

        return state;
    }

    @Override
    public String toString() {
        return "closeRFile(" + expression + ")";
    }
}




