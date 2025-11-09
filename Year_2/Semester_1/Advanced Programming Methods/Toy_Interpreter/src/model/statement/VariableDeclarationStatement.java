package model.statement;

import exceptions.MyException;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.Type;
import model.value.IValue;

public class VariableDeclarationStatement implements IStatement {
    private final String name;
    private final Type type;

    public VariableDeclarationStatement(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public IStatement deepCopy() {
        return new VariableDeclarationStatement(name, type);
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        ISymbolTable<String, IValue> symTable = state.getSymbolTable();

        if (symTable.isDefined(name))
            throw new MyException("Variable '" + name + "' already declared.");

        // Each type can define a default value (you can later add a factory for this)
        symTable.put(name, type.getDefaultValue());
        return state;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
