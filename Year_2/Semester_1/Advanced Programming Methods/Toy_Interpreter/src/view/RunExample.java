package view;

import controller.Controller;
import exceptions.*;
import repository.IRepository;
import repository.Repository;
import model.state.ProgramState;

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


            //facem deep copy pt program
            ProgramState copiedState = ctrl.copyProgramState(ctrl.getRepository().getCurrentProgram());
            IRepository tempRepo = new Repository(copiedState, ctrl.getRepository().getLogFilePath());

            Controller tempCtrl = new Controller(tempRepo);

            tempCtrl.allStep();


                //logam rezultatul in fisier (folosind repository)
            ctrl.getRepository().logPrgStateExec(copiedState);

            alreadyRun = true;
        } catch (MyException | InterpreterException | IOException e) {
      throw new ErrorExecut("Eroare la executarea programului: " + e.getMessage());
        }

    }
}
