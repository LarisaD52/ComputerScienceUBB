package model.statement;

import exceptions.FileAlreadyOpenException;
import exceptions.FileNotFoundException;
import exceptions.MyException;
import model.expression.IExpression;
import model.state.IFileTable;
import model.state.ProgramState;
import model.type.Type;
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
        // Evaluăm expresia
        IValue val = expression.evaluate(state.getSymbolTable());

        // Verificăm dacă tipul este StringType
        if (!(val.getType() instanceof Type)) {
            throw new MyException("OpenRFile: Expression is not of type string.");
        }

        StringValue fileName = (StringValue) val;
        IFileTable<String, BufferedReader> fileTable = state.getFileTable();

        // Verificăm dacă fișierul nu e deja deschis
        if (fileTable.isOpen(fileName.getVal())) {
            throw new FileAlreadyOpenException("OpenRFile: File already opened: " + fileName.getVal());
        }

        // Deschidem fișierul
        try {
            BufferedReader br = new BufferedReader(new FileReader(String.valueOf(fileName.getVal())));
            fileTable.addOpenFile(fileName.getVal(), br);
        } catch (IOException _ ) {          // _ inseamna anonim, si nu se poate pune e pt c e nu i folosit in interiorul lui catch
            throw new FileNotFoundException("OpenRFile: Cannot open file: " + fileName.getVal());
        }

        return state;
    }

    @Override
    public String toString() {
        return "openRFile(" + expression + ")";
    }
}
