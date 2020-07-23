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
  public void getAnswerBodyTest() {
    // Valid answer id.
    String answer_id1 = "218510";
    // Invalid answer id.
    String answer_id2 = "12912048";

    String answer_body1 = client.getAnswerBody(answer_id1);
    String answer_body2 = client.getAnswerBody(answer_id2);

    assertEquals(
        "<p>When you declare a reference variable (i.e. an object) you are really creating a"
            + " pointer to an object. Consider the following code where you declare a variable of"
            + " primitive type <code>int</code>:</p>\n\n"
            + "<pre><code>int x;\n"
            + "x = 10;\n"
            + "</code></pre>\n\n"
            + "<p>In this example, the variable <code>x</code> is an <",
        answer_body1.substring(0, 300));
    assertNull(answer_body2);
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

  @Test
  public void validSearchTest() {
    Card actual = 
        client.search(
            "https://stackoverflow.com/questions/63057965/custom-dropdown-to-page-size-on-reacttable");
    Card expected = new Card("Custom dropdown to page size on ReactTable", 
                              null, 
                              "https://stackoverflow.com/questions/63057965/custom-dropdown-to-page-size-on-reacttable", 
                              "<p>I found the solution and was add in the properties this line</p>\n" 
                              + "<pre><code>pageSizeOptions= {[10, 15, 20]}\n</code></pre>\n");
    assertEquals(expected, actual);
  }
}
