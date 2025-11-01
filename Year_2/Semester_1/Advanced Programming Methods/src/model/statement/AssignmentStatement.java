package model.statement;

import model.expression.Expression;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.value.Value;

public class AssignmentStatement implements Statement {
    private final Expression expression;
    private final String variableName;

    public AssignmentStatement(Expression expression, String variableName) {
        this.expression = expression;
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        SymbolTable symbolTable = state.getSymbolTable();
        if(!symbolTable.isDefined(variableName)) {
            throw new RuntimeException("Variable not defined");
        }
        Value value = expression.evaluate(symbolTable);
        if(!value.getType().equals(symbolTable.getType(variableName))) {
            throw new RuntimeException("Type mismatch");
        }
        symbolTable.update(variableName, value);
        return null;
    }
}
