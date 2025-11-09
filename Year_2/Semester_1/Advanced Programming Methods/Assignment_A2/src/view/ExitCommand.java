package view;

import exceptions.ExitException;

public class ExitCommand extends Command {
  public ExitCommand(String key, String desc) {
    super(key, desc);
  }

  @Override
  public void execute() {
    throw new ExitException("Exit command has been called");
  }
}
