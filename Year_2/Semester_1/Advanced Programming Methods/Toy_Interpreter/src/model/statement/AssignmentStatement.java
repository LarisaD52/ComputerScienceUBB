package model.statement;

import exceptions.MyException;
import exceptions.TypeMismatchException;
import exceptions.VariableNotDefinedException;
import model.expression.IExpression;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.value.IValue;

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
    // daca vrei chiar deep copy si pentru expresii, trebuie sa ai deepCopy si acolo
  }

  @Override
  public ProgramState execute(ProgramState state) throws MyException {
    ISymbolTable<String, IValue> symTable = state.getSymbolTable();

    if (!symTable.isDefined(variableName))
      throw new VariableNotDefinedException(
          "Variable '" + variableName + "' was not declared before.");

    IValue value = expression.evaluate(symTable);
    IValue existingValue = symTable.getValue(variableName);

    if (!value.getType().equals(existingValue.getType()))
      throw new TypeMismatchException("Type mismatch for variable '" + variableName + "'.");

    symTable.update(variableName, value);
    return state;
  }

  @Override
  public String toString() {
    return variableName + " = " + expression.toString();
  }
}
