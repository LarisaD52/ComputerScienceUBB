package model.statement;

import model.state.ProgramState;
import model.type.Type;

public record VariableDeclarationStatement (Type type, String variableName) implements IStatement {

    @Override
    public ProgramState execute(ProgramState state) {
        var symbolTable = state.symbolTable();               // 1️⃣ Obținem tabela de simboluri din starea programului.
        if( symbolTable.isDefined(variableName) ) {          // 2️⃣ Verificăm dacă variabila există deja.
            throw new RuntimeException("Variable already defined");  // 3️⃣ Dacă da, aruncăm eroare (nu putem redeclara o variabilă).
        }
        symbolTable.declarareVariable(variableName, type);   // 4️⃣ Dacă nu, adăugăm variabila în tabelă cu valoarea implicită a tipului (ex: 0, false).
        return state;                                        // 5️⃣ Returnăm aceeași stare (dar modificată în interior).
    }
}
