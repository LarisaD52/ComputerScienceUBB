package model.statement;

import exceptions.FileAlreadyOpenException;
import exceptions.FileNotFoundException;
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
import java.io.FileReader;
import java.io.IOException;

public class OpenRFileStatement implements IStatement {

    private final IExpression expression;

    public OpenRFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IStatement deepCopy() {
        return new OpenRFileStatement(expression.deepCopy());
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        IValue val = expression.evaluate(state.getSymbolTable(), state.getHeap());

        if (!(val.getType() instanceof IType)) {
            throw new MyException("OpenRFile: Expression is not of type string.");
        }

        StringValue fileName = (StringValue) val;
        IFileTable<String, BufferedReader> fileTable = state.getFileTable();

        if (fileTable.isOpen(fileName.getVal())) {
            throw new FileAlreadyOpenException("OpenRFile: File already opened: " + fileName.getVal());
        }

        //DESCHIDEM FIS
        try {
            BufferedReader br = new BufferedReader(new FileReader(String.valueOf(fileName.getVal())));
            fileTable.addOpenFile(fileName.getVal(), br);
        } catch (IOException _ ) {          // _ inseamna anonim, si nu se poate pune e pt c e nu i folosit in interiorul lui catch
            throw new FileNotFoundException("OpenRFile: Cannot open file: " + fileName.getVal());
        }

        return null;
    }


    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        //expresia trebuie sa se evalueze la StringType (numele fisierului)
        IType typexp = expression.typecheck(typeEnv);
        if (!typexp.equals(new StringType())) {
            throw new MyException("OpenRFile: Expression must evaluate to StringType.");
        }
        return typeEnv;
    }


    @Override
    public String toString() {
        return "openRFile(" + expression + ")";
    }
}
