package model.state;

import model.type.Type;
import model.value.Value;

public interface SymbolTable {
    boolean isDefined(String variableName);

    Type getType(String variableName);

    void update(String variableName, Value value);

}
