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
  public void noQuestionIdTest() {
    String question_id = client.getQuestionId("https://stackoverflow.com/questions");
    assertNull(question_id);
  }

  @Test
  public void getQuestionIdRegexTest() {
    String question_id = client.getQuestionId("https://stackoverflow.com/questions/dhwiurgewi");
    assertNull(question_id);
  }

  @Test
  public void invalidTaggedPathTest() {
    String question_id = client.getQuestionId("https://stackoverflow.com/tagged/java");
    assertNull(question_id);
  }

  @Test
  public void invalidAnswerPathTest() {
    String question_id = client.getQuestionId("https://stackoverflow.com/answer/2841892");
    assertNull(question_id);
  }

  @Test
  public void validQuestionIdTest() {
    String question_id =
        client.getQuestionId(
            "https://stackoverflow.com/questions/12912048/how-to-maintain-aspect-ratio-using-html-img-tag");
    assertEquals("12912048", question_id);
  }

  @Test
  public void validAnswerIdTest() {
    // Valid question id.
    String question_id = "12912048";
    String answer_id = client.getAnswerId(question_id);
    assertEquals("12912224", answer_id);
  }

  @Test
  public void invalidAnswerIdTest() {
    // Test for unanswered question.
    String question_id = "44686609";
    String answer_id = client.getAnswerId(question_id);
    assertNull(answer_id);
  }

  @Test
  public void validTitleTest() {
    // Valid question id.
    String question_id = "12912048";
    String title = client.getTitle(question_id);
    assertEquals("How to maintain aspect ratio using HTML IMG tag", title);
  }

  @Test
  public void invalidTitleTest() {
    // Id number that doesn't point to a question.
    String question_id = "12912224";
    String title = client.getTitle(question_id);
    assertNull(title);
  }

  @Test
  public void validAnswerBodyTest() {
    // Valid answer id.
    String answer_id = "63059653";
    String answer_body = client.getAnswerBody(answer_id);
    assertEquals(
        "<p>I found the solution and was add in the properties this line</p>\n"
            + "<pre><code>pageSizeOptions= {[10, 15, 20]}\n</code></pre>\n",
        answer_body);
  }

  @Test
  public void invalidAnswerBodyTest() {
    // Invalid answer id.
    String answer_id = "12912048";
    String answer_body = client.getAnswerBody(answer_id);
    assertNull(answer_body);
  }

  @Test
  public void noAnswerSearchTest() {
    Card actual =
        client.search(
            "https://stackoverflow.com/questions/44686609/implementing-a-neural-network-in-haskell",
            "implement neural network in haskell");
    assertNull(actual);
  }

  @Test
  public void fakeLinkSearchTest() {
    Card actual =
        client.search(
            "https://softwareengineering.stackexchange.com/questions/100/what-website-are-yo", "");
    assertNull(actual);
  }

  @Test
  public void validSearchTest() {
    Card actual =
        client.search(
            "https://stackoverflow.com/questions/63057965/custom-dropdown-to-page-size-on-reacttable",
            "custom dropdown to page size on reacttable");
    Card expected =
        new Card(
            "Custom dropdown to page size on ReactTable",
            null,
            "https://stackoverflow.com/questions/63057965/custom-dropdown-to-page-size-on-reacttable",
            "<p>I found the solution and was add in the properties this line</p>\n"
                + "<pre><code>pageSizeOptions= {[10, 15, 20]}\n</code></pre>\n",
            "StackOverflow",
            "https://stackoverflow.com/favicon.ico");
    assertEquals(expected, actual);
  }
}
