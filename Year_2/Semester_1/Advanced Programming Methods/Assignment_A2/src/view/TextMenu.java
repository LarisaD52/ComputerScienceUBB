package view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {

    private final Map<String, Command> commands;

    public TextMenu() {
        commands = new HashMap<>();
    }

    public void addCommand(Command c) {
        commands.put(c.getKey(), c);
    }

    // afisez meniu in format dorit
    private void printMenu() {
        System.out.println("\n=== MENIU PRINCIPAL ===");

        for (Command com : commands.values()) {
            String line = String.format("%4s : %s", com.getKey(), com.getDescription());
            System.out.println(line);
        }
        System.out.println();
    }


    public void show() {
        Scanner scanner = new Scanner(System.in);
        boolean finished = false;

        while (!finished) {
            printMenu();
            System.out.print("Alege programul: ");

            String key = scanner.nextLine().trim();

            Command com = commands.get(key);

            if (com == null) {
                System.out.println("Optiune invalida! Incearca din nou.");
            } else {
                if (key.equals("0")) {
                    finished = true;
                    System.out.println("Iesire... Programul s-a încheiat.");
                } else {
                    com.execute();
                }
            }
        }
        scanner.close();
    }
}
