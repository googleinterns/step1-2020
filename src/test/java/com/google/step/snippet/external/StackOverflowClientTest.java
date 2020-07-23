package com.google.step.snippet.external;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.step.snippet.data.Card;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class StackOverflowClientTest {
  private final StackOverflowClient client = new StackOverflowClient("CSE_ID");

  @Test
  public void getQuestionIdTest() {
    // Invalid question id because of wrong URL format.
    String id1 = client.getQuestionId("https://stackoverflow.com/questions");
    String id2 = client.getQuestionId("https://stackoverflow.com/questions/dhwiurgewi");
    String id3 = client.getQuestionId("https://stackoverflow.com/tagged/java");
    String id4 = client.getQuestionId("https://stackoverflow.com/answer/2841892");
    // Valid URL.
    String id5 =
        client.getQuestionId(
            "https://stackoverflow.com/questions/12912048/how-to-maintain-aspect-ratio-using-html-img-tag");

    assertNull(id1);
    assertNull(id2);
    assertNull(id3);
    assertNull(id4);
    assertEquals("12912048", id5);
  }

  @Test
  public void getAnswerIdTest() {
    // Valid question id.
    String question_id1 = "12912048";
    // Test for unanswered question.
    String question_id2 = "44686609";

    String answer_id1 = client.getAnswerId(question_id1);
    String answer_id2 = client.getAnswerId(question_id2);

    assertEquals("12912224", answer_id1);
    assertNull(answer_id2);
  }

  @Test
  public void getTitleTest() {
    // Valid question id.
    String question_id1 = "12912048";
    // Id number that doesn't point to a question.
    String question_id2 = "12912224";

    String title1 = client.getTitle(question_id1);
    String title2 = client.getTitle(question_id2);

    assertEquals("How to maintain aspect ratio using HTML IMG tag", title1);
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
            + " <code>&lt;img&gt;</code> for a long time. I believe it was an early Netscape"
            + " browser that first did this, possibly to compensate for user error, or possibly"
            + " because there was dispute at the time whether the element sho",
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
