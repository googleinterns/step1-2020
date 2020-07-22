package com.google.step.snippet.external;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.step.snippet.data.Card;
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
  public void validCardTest1() {
    Card actual =
        client.search(
            "https://stackoverflow.com/questions/22250067/how-to-get-address-of-a-pointer-in-c-c");

    assertEquals("How to get address of a pointer in c/c++?", actual.getTitle());
    assertNull(actual.getCode());
    assertEquals(
        "https://stackoverflow.com/questions/22250067/how-to-get-address-of-a-pointer-in-c-c",
        actual.getLink());
    assertEquals(
        "<p>To get the address of p do:</p>\n\n"
            + "<pre><code>int **pp = &amp;p;\n"
            + "</code></pre>\n\n"
            + "<p>and you can go on:</p>\n\n"
            + "<pre><code>int ***ppp = &amp;pp;\n"
            + "int ****pppp = &amp;ppp;\n"
            + "...\n"
            + "</code></pre>\n\n"
            + "<p>or, only in C++11, you can do:</p>\n\n"
            + "<pre><code>auto pp = std::addressof(p);\n"
            + "</code></pre>\n\n"
            + "<p>To print the addre",
        actual.getDescription().substring(0, 300));
  }

  @Test
  public void validCardTest2() {
    Card actual =
        client.search("https://stackoverflow.com/questions/11928566/img-vs-image-tag-in-html");

    assertEquals("&lt;img&gt; vs &lt;image&gt; tag in HTML", actual.getTitle());
    assertNull(actual.getCode());
    assertEquals(
        "https://stackoverflow.com/questions/11928566/img-vs-image-tag-in-html", actual.getLink());
    assertEquals(
        "<p>Yes and no. As you point out <code>&lt;image&gt;</code> has been a synonym for"
            + " <code>&lt;img&gt;</code> for a long time. I believe it was an early Netscape browser"
            + " that first did this, possibly to compensate for user error, or possibly because there"
            + " was dispute at the time whether the element sho",
        actual.getDescription().substring(0, 300));
  }

  @Test
  public void noAnswerTest() {
    Card actual =
        client.search(
            "https://stackoverflow.com/questions/44686609/implementing-a-neural-network-in-haskell");
    assertNull(actual);
  }

  @Test
  public void fakeLinkTest() {
    Card actual =
        client.search(
            "https://softwareengineering.stackexchange.com/questions/100/what-website-are-yo");
    assertNull(actual);
  }
}
