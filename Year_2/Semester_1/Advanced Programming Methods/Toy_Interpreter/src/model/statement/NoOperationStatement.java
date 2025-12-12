package model.statement;

import exceptions.MyException;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;

public class NoOperationStatement implements IStatement {
  @Override
  public ProgramState execute(ProgramState state) {
    return null;
  }

  @Override
  public IStatement deepCopy() {
    return new NoOperationStatement();
  }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
  public String toString() {
    return "NOP";
  }
}
