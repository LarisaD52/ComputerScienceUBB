package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.IFileTable;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.Type;
import model.value.IValue;
import model.value.IntegerValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements IStatement {
    private final IExpression expression;
    private final String varName;

    public ReadFileStatement(IExpression expression, String varName) {
        this.expression = expression;
        this.varName = varName;
    }


    @Override
    public IStatement deepCopy() {
        return new ReadFileStatement(expression.deepCopy(), varName);
    }



    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        ISymbolTable<String, IValue> symTable = state.getSymbolTable();
        IFileTable<String, BufferedReader> fileTable = state.getFileTable();

        // 1. Verificăm variabila
        if (!symTable.isDefined(varName)) {
            throw new MyException("ReadFile: Variable '" + varName + "' not declared.");
        }
        IValue varValue = symTable.getValue(varName);
        if (!(varValue.getType() instanceof Type)) {
            throw new MyException("ReadFile: Variable '" + varName + "' is not of type int.");
        }

        // 2. Evaluăm expresia pentru numele fișierului
        IValue fileExpVal = expression.evaluate(symTable);
        if (!(fileExpVal instanceof StringValue)) {
            throw new MyException("ReadFile: Expression does not evaluate to a string.");
        }
        String fileName = ((StringValue) fileExpVal).getVal();

        // 3. Obținem BufferedReader din FileTable
        if (!fileTable.isOpen(fileName)) {
            throw new MyException("ReadFile: File '" + fileName + "' is not opened.");
        }
        BufferedReader br = fileTable.get(fileName);

        // 4. Citim linia și transformăm în IntegerValue
        int intVal;
        try {
            String line = br.readLine();
            if (line == null) {
                intVal = 0;
            } else {
                intVal = Integer.parseInt(line);
            }
        } catch (IOException _) {
            throw new MyException("ReadFile: Error reading from file '" + fileName + "'.");
        } catch (NumberFormatException _) {
            throw new MyException("ReadFile: File '" + fileName + "' contains invalid integer.");
        }

        // 5. Actualizăm SymTable
        symTable.update(varName, new IntegerValue(intVal));

        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + expression + ", " + varName + ")";
    }
}