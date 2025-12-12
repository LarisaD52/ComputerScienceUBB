package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.IFileTable;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntegerType;
import model.type.StringType;
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
        IHeap heap = state.getHeap();

        if (!symTable.isDefined(varName)) {
            throw new MyException("ReadFile: Variable '" + varName + "' not declared.");
        }
        IValue varValue = symTable.getValue(varName);
        if (!(varValue.getType() instanceof IType)) {
            throw new MyException("ReadFile: Variable '" + varName + "' is not of type int.");
        }

        IValue fileExpVal = expression.evaluate(symTable, heap);
        if (!(fileExpVal instanceof StringValue)) {
            throw new MyException("ReadFile: Expression does not evaluate to a string.");
        }
        String fileName = ((StringValue) fileExpVal).getVal();

        if (!fileTable.isOpen(fileName)) {
            throw new MyException("ReadFile: File '" + fileName + "' is not opened.");
        }
        BufferedReader br = fileTable.get(fileName);


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


        symTable.update(varName, new IntegerValue(intVal));

        return null;
    }




    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        // 1. Expresia trebuie să se evalueze la StringType (numele fișierului)
        IType typexp = expression.typecheck(typeEnv);
        if (!typexp.equals(new StringType())) {
            throw new MyException("ReadFile: Expression must evaluate to StringType.");
        }

        // 2. Variabila trebuie să fie IntegerType
        if (!typeEnv.isDefined(varName)) {
            throw new MyException("ReadFile: Variable " + varName + " is not defined.");
        }
        IType varType = typeEnv.getValue(varName);
        if (!varType.equals(new IntegerType())) {
            throw new MyException("ReadFile: Variable " + varName + " must be IntegerType.");
        }

        // 3. Returnează mediul neschimbat
        return typeEnv;
    }

    @Override
    public String toString() {
        return "readFile(" + expression + ", " + varName + ")";
    }
}