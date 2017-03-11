package org.kirhgoff.morphoid.ascii;

import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
 */
public class AsciiFactoryTest {
  @Test
  public void testLoadSimple() throws Exception {
    AsciiFactory factory = AsciiFactory.load("ascii/simple.txt");
    assertThat(factory).isNotNull();

    String text = factory.getAsciiFrameString("simple");
    assertThat(text).isNotNull();
    assertThat(text).isEqualTo(" 12\n  34\n");

  }

  @Test
  public void testLoadComplex() throws Exception {
    AsciiFactory factory = AsciiFactory.load("ascii/complex.txt");
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


}