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
    String answer_id = "218510";
    String answer_body = client.getAnswerBody(answer_id);
    assertEquals(
        "<p>When you declare a reference variable (i.e. an object)"
            + " you are really creating a pointer to an object. Consider the following"
            + " code where you declare a variable of primitive type <code>int</code>:</p>\n\n"
            + "<pre><code>int x;\nx = 10;\n</code></pre>\n\n<p>In this example, the variable"
            + " <code>x</code> is an <code>int</code> and Java will initialize it to <code>0</code>"
            + " for you. When you assign it the value of <code>10</code> on the second line, "
            + "your value of <code>10</code> is written into the memory location referred to by "
            + "<code>x</code>.</p>\n\n<p>But, when you try to declare a reference <em>type</em>,"
            + " something different happens. Take the following code:</p>\n\n<pre><code>Integer num;\n"
            + "num = new Integer(10);\n</code></pre>\n\n<p>The first line declares a variable named <code>"
            + "num</code>, but it does not actually contain a primitive value yet. Instead, it contains a "
            + "pointer (because the type is <code>Integer</code> which is a reference type). Since you have "
            + "not yet said what to point to, Java sets it to <code>null</code>, which means \"<strong>I am "
            + "pointing to <em>nothing</em></strong>\".</p>\n\n<p>In the second line, the <code>new</code> "
            + "keyword is used to instantiate (or create) an object of type <code>Integer</code> and the "
            + "pointer variable <code>num</code> is assigned to that <code>Integer</code> object. </p>\n\n"
            + "<p>The <code>NullPointerException</code> occurs when you declare a variable but did not "
            + "create an object and assign to the variable before trying to use the contents of the variable"
            + " (called <em>dereferencing</em>). So you are pointing to something that does not actually "
            + "exist.</p>\n\n<p>Dereferencing usually happens when using <code>.</code> to access a method or"
            + " field, or using <code>[</code> to index an array. </p>\n\n<p>If you attempt to dereference "
            + "<code>num</code> BEFORE creating the object you get a <code>NullPointerException</code>. In t"
            + "he most trivial cases, the compiler will catch the problem and let you know that \"<code>num "
            + "may not have been initialized</code>,\" but sometimes you may write code that does not "
            + "directly create the object.</p>\n\n<p>For instance, you may have a method as follows:</p>\n\n"
            + "<pre><code>public void doSomething(SomeObject obj) {\n //do something to obj\n}\n</code></pre>"
            + "\n\n<p>In which case, you are not creating the object <code>obj</code>, but rather assuming "
            + "that it was created before the <code>doSomething()</code> method was called. Note, it is "
            + "possible to call the method like this:</p>\n\n<pre><code>doSomething(null);\n</code></pre>\n\n"
            + "<p>In which case, <code>obj</code> is <code>null</code>. If the method is intended to do some"
            + "thing to the passed-in object, it is appropriate to throw the <code>NullPointerException</code"
            + "> because it's a programmer error and the programmer will need that information for debugging "
            + "purposes. Please include the name of the object variable in the exception message, like</p>\n\n"
            + "<pre><code>Objects.requireNonNull(a, \"a\");\n</code></pre>\n\n<p>Alternatively, there may be "
            + "cases where the purpose of the method is not solely to operate on the passed in object, and th"
            + "erefore a null parameter may be acceptable. In this case, you would need to check for a <strong>"
            + "null parameter</strong> and behave differently. You should also explain this in the documentatio"
            + "n. For example, <code>doSomething()</code> could be written as:</p>\n\n<pre><code>/**\n * @param"
            + " obj An optional foo for ____. May be null, in which case \n * the result will be ____.\n */\npu"
            + "blic void doSomething(SomeObject obj) {\n if(obj == null) {\n //do something\n } else {\n //do s"
            + "omething else\n }\n}\n</code></pre>\n\n<p>Finally, <a href=\"https://stackoverflow.com/q/3988788"
            + "/2775450\">How to pinpoint the exception & cause using Stack Trace</a></p>\n\n<blockquote>\n <p>"
            + "What methods/tools can be used to determine the cause so that you stop\n the exception from caus"
            + "ing the program to terminate prematurely?</p>\n</blockquote>\n\n<p>Sonar with findbugs can detec"
            + "t NPE.\n<a href=\"https://stackoverflow.com/questions/20899931/can-sonar-catch-null-pointer-exce"
            + "ptions-caused-by-jvm-dynamically\">Can sonar catch null pointer exceptions caused by JVM"
            + " Dynamically</a></p>\n",
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
            "https://stackoverflow.com/questions/44686609/implementing-a-neural-network-in-haskell");
    assertNull(actual);
  }

  @Test
  public void fakeLinkSearchTest() {
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
