package view.Command;

import controller.Controller;
import exceptions.*;

import java.io.IOException;

public class RunExample extends Command {
    private final Controller ctrl;
    private boolean alreadyRun;

    public RunExample(String key, String desc, Controller ctrl) {
        super(key, desc);
        this.ctrl = ctrl;
        this.alreadyRun = false;
    }

    @Override
    public void execute() {
        if (alreadyRun) {
            throw new AlreadyRunException("Programul a fost deja rulat și nu poate fi rulat din nou.");
        }

        try {
            ctrl.allStep();
            alreadyRun = true; // Setează starea ca rulata

        } catch (MyException | InterpreterException | IOException e) {
            // Prinde orice eroare din controller/repo/executie
            throw new ErrorExecut("Eroare la executarea programului: " + e.getMessage());
        }
    }


}
