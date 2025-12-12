package model.statement;

import exceptions.MyException;
import exceptions.TypeMismatchException;
import exceptions.VariableNotDefinedException;
import model.expression.IExpression;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;
import model.value.IValue;
import model.state.IHeap;

public class AssignmentStatement implements IStatement {
  private final String variableName;
  private final IExpression expression;

  public AssignmentStatement(String variableName, IExpression expression) {
    this.variableName = variableName;
    this.expression = expression;
  }

    @Override
  public IStatement deepCopy() {
    return new AssignmentStatement(variableName, expression);

  }

  @Override
  public ProgramState execute(ProgramState state) throws MyException {
    ISymbolTable<String, IValue> symTable = state.getSymbolTable();
    IHeap heap = state.getHeap();

    if (!symTable.isDefined(variableName))
      throw new VariableNotDefinedException(
          "Variable '" + variableName + "' was not declared before.");

    IValue value = expression.evaluate(symTable, heap);
    IValue existingValue = symTable.getValue(variableName);

    if (!value.getType().equals(existingValue.getType()))
      throw new TypeMismatchException("Type mismatch for variable '" + variableName + "'.");

    symTable.update(variableName, value);
    return null;
  }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        if (!typeEnv.isDefined(variableName)) {
            throw new MyException("Assignment: Variable " + variableName + " is not defined.");
        }

        IType typeVar = typeEnv.getValue(variableName); // Tipul declarat al variabilei
        IType typeExp = expression.typecheck(typeEnv);  // Tipul rezultat din evaluarea expresiei

        if (typeVar.equals(typeExp)) {
            return typeEnv;
        } else {
            throw new MyException("Assignment: right hand side (" + typeExp + ") and left hand side (" + typeVar + ") have different types.");
        }
    }

  @Override
  public String toString() {
    return variableName + " = " + expression.toString();
  }
}
