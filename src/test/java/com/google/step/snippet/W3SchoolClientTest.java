package com.google.step.snippet;

import com.google.step.snippet.data.Card;
import com.google.step.snippet.external.W3SchoolClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class W3SchoolClientTest {

  W3SchoolClient client = new W3SchoolClient();
  Card imageCard = client.search("https://www.w3schools.com/tags/tag_img.asp");

  @Test
  public void htmlImageTitle() {
    String actual = imageCard.getTitle();
    String expected = "HTML <img> Tag";
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void htmlImageCode() {
    String actual = imageCard.getCode();
    String expected =
        "<img src=\"img_girl.jpg\" alt=\"Girl in a jacket\" width=\"500\" height=\"600\">";
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void htmlImageLink() {
    String actual = imageCard.getLink();
    String expected = "https://www.w3schools.com/tags/tag_img.asp";
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void htmlImageDescription() {
    String actual = imageCard.getDescription();
    String expected = "How to insert an image:";
    Assert.assertEquals(expected, actual);
  }
}
