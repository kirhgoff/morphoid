package org.kirhgoff.morphoid.ascii;

import org.kirhgoff.morphoid.TestUtilsTrait;
import org.kirhgoff.morphoid.engine.Physical;
import org.kirhgoff.morphoid.engine.Creature;
import org.kirhgoff.morphoid.render.GameGeometry;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
 */
public class AsciiFactoryTestTrait implements TestUtilsTrait {
  @Test
  public void testLoadSimple() throws Exception {
    AsciiFactory factory = AsciiFactory.makeFor("ascii/simple.txt", null);
    assertThat(factory).isNotNull();

    String text = factory.getAsciiFrameString("simple");
    assertThat(text).isNotNull();
    assertThat(text).isEqualTo(" 12\n  34\n");
  }

  @Test
  public void testLoadComplex() throws Exception {
    AsciiFactory factory = AsciiFactory.makeFor("ascii/complex.txt", null);
    assertThat(factory).isNotNull();
    assertThat(factory.ids()).containsOnly("first", "second", "third");

    List<String> list = factory.getAsciiFrames("first");
    assertThat(list).isNotNull();
    assertThat(list.size()).isEqualTo(2);

    list = factory.getAsciiFrames("second");
    assertThat(list).isNotNull();
    assertThat(list).containsExactly("0\n", "1\n", "2\n");

    list = factory.getAsciiFrames("third");
    assertThat(list).isNotNull();
    assertThat(list.size()).isEqualTo(2);
    assertThat(list).containsExactly("empty\n\nline\n", "missed\n");
  }

  @Test
  public void testEntity() throws Exception {

    GameGeometry geometry = new GameGeometry(wrap(100), wrap(100), 10, 10);
    AsciiFactory factory = AsciiFactory.makeFor("ascii/game.txt", geometry);

    AsciiSprite sprite = factory.getSprite(fake("player", 5, 5));
    assertThat(sprite.getOrigin().getX()).isEqualTo(50);
    assertThat(sprite.getOrigin().getY()).isEqualTo(50);
    assertThat(sprite.getAscii()).isEqualTo("P\n");

    sprite = factory.getSprite(fake("monster", -1, 15));
    assertThat(sprite.getOrigin().getX()).isEqualTo(-10);
    assertThat(sprite.getOrigin().getY()).isEqualTo(150);
    assertThat(sprite.getAscii()).isEqualTo("M\n");

  }

  private Creature fake(String kind, int x, int y) {
    Creature entity = mock(Creature.class);
    when(entity.kind()).thenReturn(kind);
    when(entity.origin()).thenReturn(new Physical(x, y));
    return entity;
  }

}