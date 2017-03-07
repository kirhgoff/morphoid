package org.kirhgoff.munchkin;

import org.kirhgoff.munchkin.api.CommandProcessor;

import java.util.Scanner;

public class UserInputReader implements Runnable {
  private final Scanner scanner;
  private final CommandProcessor processor;

  public UserInputReader(CommandProcessor processor) {
    this.processor = processor;
    this.scanner = new Scanner(System.in);
  }

  public void run() {
    boolean keepRunning = true;

    while(keepRunning) {
      String command = scanner.next();
      if (command.equals("quitnow")) {
        keepRunning = false;
      }
      System.out.println("User>" + command);
      processor.issueCommand(command);
    }

    scanner.close();
    System.out.println("System> Quit reader");
  }
}
