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
  public void validCardTest1() {
    Card actual =
        client.search(
            "https://stackoverflow.com/questions/22250067/how-to-get-address-of-a-pointer-in-c-c");
    Card expected =
        new Card(
            "How to get address of a pointer in c/c++?",
            null,
            "https://stackoverflow.com/questions/22250067/how-to-get-address-of-a-pointer-in-c-c",
            "<p>To get the address of p do:</p>\n\n"
                + "<pre><code>int **pp = &p;\n"
                + "</code></pre>\n\n"
                + "<p>and you can go on:</p>\n\n"
                + "<pre><code>int ***ppp = &pp;\n"
                + "int ****pppp = &ppp;\n...\n</code></pre>\n\n"
                + "<p>or, only in C++11, you can do:</p>\n\n"
                + "<pre><code>auto pp = std::addressof(p);\n"
                + "</code></pre>\n\n<p>To print the address in C, most compilers support"
                + " <code>%p</code>, so you can simply do:</p>\n\n<pre><code>printf(\"addr: %p\""
                + ", pp);\n</code></pre>\n\n<p>otherwise you need to cast it (assuming a 32 bit platform)"
                + "</p>\n\n<pre><code>printf(\"addr: 0x%u\", (unsigned)pp);\n</code></pre>\n\n"
                + "<p>In C++ you can do:</p>\n\n<pre><code>cout << \"addr: \" << pp;\n</code></pre>\n");
    assertEquals(expected, actual);
  }

  @Test
  public void validCardTest2() {
    Card actual =
        client.search("https://stackoverflow.com/questions/11928566/img-vs-image-tag-in-html");
    Card expected =
        new Card(
            "&lt;img&gt; vs &lt;image&gt; tag in HTML",
            null,
            "https://stackoverflow.com/questions/11928566/img-vs-image-tag-in-html",
            "<p>Yes and no. As you point out <code><image></code> has been a synonym for <code>"
                + "<img></code> for a long time. I believe it was an early Netscape browser that first "
                + "did this, possibly to compensate for user error, or possibly because there was dispute "
                + "at the time whether the element should actually be called <code><image></code> or <code>"
                + "<img></code>.</p>\n\n<p>Anyway, as pst points out, once it was implemented in a browser"
                + " that dominated the market of the time, web pages came to rely on it. Its persistence "
                + "is then down to commercial pressure on the browser manufacturers. If all the major "
                + "browsers support it, then Browser A decides that although it supported it in Version V, "
                + "t won't support it in version V+1, as soon as version V+1 is released, they get lots of "
                + "messages saying \"Site S is broken in your latest browser. You browser is rubbish. I'm going "
                + "to switch to browser B\".</p>\n\n<p>The HTML5 parsing spec requires that the <code><image>"
                + "</code> tag is mapped to the <code>img</code> element at the tree construction stage, so "
                + "there can never be any justification for using it.</p>\n\n<p>I would be less concerned "
                + "about browsers, than other HTML consumers, such as the lesser known search engines. I "
                + "believe that the <code>image</code> for <code>img</code> synonym is not widely known, and"
                + " the many such tools would therefore fail to pick up <code><image></code> as referencing "
                + "an image resource. </p>\n");
    assertEquals(expected, actual);
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
