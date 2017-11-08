package org.kirhgoff.morphoid.ascii;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.kirhgoff.morphoid.engine.Creature;
import org.kirhgoff.morphoid.render.GameGeometry;

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
public class AsciiRenderer {
  private static final String START_TAG = "--- ID";
  private static final String END_TAG = "--- END";

  private final GameGeometry geometry;
  private final Map<String, AsciiImage> map;

  private AsciiRenderer(GameGeometry geometry, Map<String, AsciiImage> map) {
    this.geometry = geometry;
    this.map = map;
  }

  public Set<String> ids() {
    return map.keySet();
  }

  public String getAsciiFrameString(String id) {
    return getImage(id).getAsciiFrame(0);
  }

  public List<String> getAsciiFrames(String id) {
    return getImage(id).getAsciiFrames();
  }

  private AsciiImage getImage(String id) {
    if (!map.containsKey(id)) {
      throw new IllegalArgumentException("Cannot find key "
          + id + ", keys available " + ids());
    }
    return map.get(id);
  }

  public GameGeometry getGeometry() {
    return geometry;
  }

  public static AsciiRenderer makeFor(String filePath, GameGeometry geometry) throws IOException {
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

      return new AsciiRenderer(geometry, new ConcurrentHashMap<>(map));
    }

  }

  public AsciiSprite getSprite(Creature entity) {
    String id = entity.kind();
    String ascii = getAsciiFrameString(id);
    Point2D point = geometry.convertToScreen(entity.origin().x(), entity.origin().y());
    //TODO rework sprite creation
    return new AsciiSprite(point, ascii, getPalette(id));
  }

  public String getCell(String cellType) {
    switch (cellType) {
      case "seed": return "o";
      case "shroomseed": return "*";
      case "mover": return "m";
      case "feeder": return ">";
      case "player": return "&";
      case "projectile": return ".";
      default: return "?";
    }
  }

  public Color getPalette(String id) {
    switch (id) {
      case "ooze": return Color.GREEN;
      case "shroom": return Color.BLUE;
      case "player": return Color.RED;
      case "projectile": return Color.YELLOW;
      default: return Color.BLACK;
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
