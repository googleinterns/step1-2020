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
  public void invalidQuestionIdTest() {
    Card card1 = client.search("https://stackoverflow.com/questions");
    Card card2 = client.search("https://stackoverflow.com/questions/dhwiurgewi");
    Card card3 = client.search("https://stackoverflow.com/tagged/java");
    Card card4 = client.search("https://stackoverflow.com/answer/2841892");

    assertNull(card1);
    assertNull(card2);
    assertNull(card3);
    assertNull(card4);
  }
}
