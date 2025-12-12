package model.statement;

import exceptions.MyException;

import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;

public interface IStatement {
  ProgramState execute(ProgramState state) throws MyException;

  IStatement deepCopy();



  ISymbolTable<String, IType> typecheck(ISymbolTable<String,IType> typeEnv) throws MyException;
}
