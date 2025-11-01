package repository;

import model.state.ProgramState;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository{

    private final List<ProgramState> programStates;

    public Repository(ProgramState initialProgram) {
        this.programStates = new ArrayList<>();
        this.programStates.add(initialProgram);
    }

    @Override
    public ProgramState getCurrentProgram() {
        return programStates.get(0);
    }

    @Override
    public String toString() {
        return programStates.toString();
    }
}
