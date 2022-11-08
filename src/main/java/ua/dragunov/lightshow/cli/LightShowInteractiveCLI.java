package ua.dragunov.lightshow.cli;

import ua.dragunov.lightshow.command.Command;
import ua.dragunov.lightshow.command.CommandFactory;
import ua.dragunov.lightshow.command.data.CreateLightShowRequest;
import ua.dragunov.lightshow.exceptions.LightShowException;

import java.util.Scanner;

public class LightShowInteractiveCLI {
    private final CommandFactory commandFactory;

    public LightShowInteractiveCLI(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }


    public void run() throws LightShowException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a light label:");
        String label = scanner.nextLine();

        System.out.println("Enter a color list in format: <color>,<color>....,<color> :");
        String colorList = scanner.nextLine();

        System.out.println("Enter a switching interval in the second:");
        int switchingInterval = scanner.nextInt();

        System.out.println("Enter amount switching :");
        int amountSwitching = scanner.nextInt();

        Command<String> command = commandFactory.newLightShowCommand(new CreateLightShowRequest(
                label,
                colorList.trim().split(","),
                switchingInterval,
                amountSwitching
        ));

        String switchingHistory = command.execute();

        System.out.println(switchingHistory);
    }

}
