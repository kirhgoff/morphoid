package org.kirhgoff.morphoid.ascii;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
 */
public class AsciiFactory {
  private static final String START_TAG = "--- ID";
  private static final String END_TAG = "--- END";

  private Map<String, AsciiImage> map;

  private AsciiFactory(Map<String, AsciiImage> map) {
    this.map = map;
  }

  public Set<String> ids() {
    return map.keySet();
  }

  public String getAsciiFrameString(String id) {
    return map.get(id).getAsciiFrame(0);
  }
  public List<String> getAsciiFrames(String id) {
    return map.get(id).getAsciiFrames();
  }

  public static AsciiFactory load(String filePath) throws IOException {
    InputStream inputStream = Thread
        .currentThread()
        .getContextClassLoader()
        .getResourceAsStream(filePath);

    try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
      final List<AsciiImage> images = new LinkedList<>();
      final AtomicReference<AsciiImage> current = new AtomicReference<>();
      final StringBuilder sb = new StringBuilder();
      buffer.lines().forEach(line -> {
        if (line.startsWith(START_TAG)) {
          String id = line.substring(START_TAG.length() + 1).trim();
          AsciiImage image = new AsciiImage(id);
          images.add(image);
          current.set(image);
          sb.setLength(0);
        } else if (line.startsWith(END_TAG)){
          current.get().addFrame(sb.toString());
          sb.setLength(0);
        } else {
          sb.append(line).append("\n");
        }
      });

      Map<String, AsciiImage> map = images.stream()
          .collect(Collectors.toMap(AsciiImage::getId, identity()));

      return new AsciiFactory(new ConcurrentHashMap<>(map));
    }

  }

  private static class AsciiImage {
    private String id;
    private List<String> frames = new ArrayList<>(1);

    AsciiImage(String id) {
      this.id = id;
    }

    String getId() {
      return id;
    }

    void addFrame(String string) {
      frames.add(string);
    }

    String getAsciiFrame(int index) {
      return frames.get(index);
    }

    public List<String> getAsciiFrames() {
      return Collections.unmodifiableList(frames);
    }
  }
}
