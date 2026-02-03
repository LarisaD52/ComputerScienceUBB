package model.statement;

import exceptions.MyException;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;
import model.value.IValue;

public class VariableDeclarationStatement implements IStatement {
    private final String name;
    private final IType type;

    public VariableDeclarationStatement(String name, IType type) {
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

        //fiecare tip defineste o valoare implicita
        symTable.put(name, type.getDefaultValue());
        return null;
    }



    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        //verifica daca variabila e deja definita
        if (typeEnv.isDefined(name)) {
            throw new MyException("Variable " + name + " is already defined.");
        }
        //adauga variabila noua in mediu de tipuri
        typeEnv.put(name, type);
        return typeEnv;        //ret. mediul de tipuri actualizat

    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
