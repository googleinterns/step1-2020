package com.google.step.snippet.external;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.step.snippet.data.Card;
import com.google.step.snippet.external.StackOverflowClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class StackOverflowClientTest {
  private final Client client = new StackOverflowClient("CSE_ID");

  @Test
  public void questionIdTest() {
    Card card1 = client.search("https://stackoverflow.com/questions");
    Card card2 = client.search("https://stackoverflow.com/questions/dhwiurgewi");
    Card card3 = client.search("https://stackoverflow.com/tagged/java");
    Card card4 = client.search("https://stackoverflow.com/answer/2841892");

    assertNull(card1);
    assertNull(card2);
    assertNull(card3);
    assertNull(card4);
  }

  @Test
  public void validCardTest() {
    Card actual =
        client.search(
            "https://stackoverflow.com/questions/22250067/how-to-get-address-of-a-pointer-in-c-c");

    assertEquals("How to get address of a pointer in c/c++?", actual.getTitle());
    assertNull(actual.getCode());
    assertEquals(
        "https://stackoverflow.com/questions/22250067/how-to-get-address-of-a-pointer-in-c-c",
        actual.getLink());
    assertEquals(
        "<p>To get the address of p do:</p>\n\n<pre><code>int **pp = &amp;p;\n</code></pre>\n\n<p>and"
            + " you can go on:</p>\n\n<pre><code>int ***ppp = &amp;pp;\nint ****pppp = &amp;ppp;\n...\n</code>"
            + "</pre>\n\n<p>or, only in C++11, you can do:</p>\n\n<pre><code>auto pp = std::addressof(p);\n</co"
            + "de></pre>\n\n<p>To print the addre",
        actual.getDescription().substring(0, 300));
  }
}
