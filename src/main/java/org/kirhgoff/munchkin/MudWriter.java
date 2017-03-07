package org.kirhgoff.munchkin;

import org.kirhgoff.munchkin.api.CommandProcessor;

import java.io.OutputStream;
import java.io.PrintWriter;

public class MudWriter implements CommandProcessor {

  private final PrintWriter writer;

  public MudWriter(OutputStream outputStream) {
    this.writer = new PrintWriter(outputStream);
  }

  @Override
  public void issueCommand(String command) {
    synchronized (writer) {
      writer.println(command);
      writer.flush();
    }
  }

}
