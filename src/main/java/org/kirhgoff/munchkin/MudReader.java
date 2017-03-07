package org.kirhgoff.munchkin;

import org.kirhgoff.munchkin.api.GameStateParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

class MudReader implements Runnable {
  private final InputStreamReader reader;
  private final GameStateParser processor;

  public MudReader(InputStream inputStream, GameStateParser processor) {
    this.reader = new InputStreamReader(inputStream);
    this.processor = processor;
  }

  public void run() {
    try {
      char buffer[] = new char[64*1024];
      int readCount = reader.read(buffer);
      while (readCount != 0) {
        char[] actuallyRead = Arrays.copyOfRange(buffer, 0, readCount);
        String string = String.valueOf(actuallyRead);
        processor.parseIncomingLore(string);
        System.out.print(string);

        //Arrays.fill(buffer, ' '); //TODO
        readCount = reader.read(buffer);
      }
      System.out.println("System> mud lore printer exited");
    } catch (Exception e) {
      System.out.println("System> mud lore printer caught an exception");
      e.printStackTrace();
    }
  }
}
