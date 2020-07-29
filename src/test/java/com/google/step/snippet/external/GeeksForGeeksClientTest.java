package com.google.step.snippet.external;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.step.snippet.data.Card;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class GeeksForGeeksClientTest {

  private final GeeksForGeeksClient client =
      new GeeksForGeeksClient("CSE_ID") {
        public String getVotes(String url) {
          return "0";
        }
      };

  @Test
  public void htmlCodeCard() {
    Card actual =
        client.search(
            "https://www.geeksforgeeks.org/how-to-change-the-height-of-br-tag/", "html br");
    Card expected =
        new Card(
            "How to change the height of br tag?",
            "&lt;!DOCTYPE html &gt; &lt;html&gt; &lt;head&gt; &lt;title&gt; Customized break"
                + " example &lt;/title&gt; &lt;style type=&quot;text/css&quot;&gt; .br { display:"
                + " block; margin-bottom: 0em; } .brmedium { display: block; margin-bottom: 1em; }"
                + " .brlarge { display: block; margin-bottom: 2em; } &lt;/style&gt; &lt;/head&gt;"
                + " &lt;body&gt; &lt;h3&gt;This page shows different break height between"
                + " lines&lt;/h3&gt; &lt;p&gt; Hi User &lt;span"
                + " class=&quot;brlarge&quot;&gt;&lt;/span&gt; Welcome to &lt;span"
                + " class=&quot;brmedium&quot;&gt;&lt;/span&gt; Geeks for geeks. &lt;span"
                + " class=&quot;br&quot;&gt;&lt;/span&gt; Hope you have enjoyed your stay."
                + " &lt;/p&gt; &lt;/body&gt; &lt;/html&gt;",
            "https://www.geeksforgeeks.org/how-to-change-the-height-of-br-tag/",
            "You can&rsquo;t change the height of \n"
                + "&lt;br&gt; tag as its not an HTML element, it is just an instruction which"
                + " enforces a line break. br does not take up any space in the page.",
            "0",
            "GeeksForGeeks",
            "https://geeksforgeeks.org/favicon.ico");
    assertEquals(expected, actual);
  }

  @Test
  public void escapeHtmlPage() {
    Card actual =
        client.search(
            "https://www.geeksforgeeks.org/how-to-change-the-height-of-br-tag/", "br tag");
    Card expected =
        new Card(
            "How to change the height of br tag?",
            "&lt;!DOCTYPE html &gt; &lt;html&gt; &lt;head&gt; &lt;title&gt; Customized break"
                + " example &lt;/title&gt; &lt;style type=&quot;text/css&quot;&gt; .br { display:"
                + " block; margin-bottom: 0em; } .brmedium { display: block; margin-bottom: 1em; }"
                + " .brlarge { display: block; margin-bottom: 2em; } &lt;/style&gt; &lt;/head&gt;"
                + " &lt;body&gt; &lt;h3&gt;This page shows different break height between"
                + " lines&lt;/h3&gt; &lt;p&gt; Hi User &lt;span"
                + " class=&quot;brlarge&quot;&gt;&lt;/span&gt; Welcome to &lt;span"
                + " class=&quot;brmedium&quot;&gt;&lt;/span&gt; Geeks for geeks. &lt;span"
                + " class=&quot;br&quot;&gt;&lt;/span&gt; Hope you have enjoyed your stay."
                + " &lt;/p&gt; &lt;/body&gt; &lt;/html&gt;",
            "https://www.geeksforgeeks.org/how-to-change-the-height-of-br-tag/",
            "You can&rsquo;t change the height of \n"
                + "&lt;br&gt; tag as its not an HTML element, it is just an instruction which"
                + " enforces a line break. br does not take up any space in the page.",
            "0",
            "GeeksForGeeks",
            "https://geeksforgeeks.org/favicon.ico");
    assertEquals(expected, actual);
  }

  @Test
  public void escapeHtmlQuery() {
    Card actual =
        client.search(
            "https://www.geeksforgeeks.org/how-to-display-error-without-alert-box-using-javascript/",
            "html display error");
    Card expected =
        new Card(
            "How to display error without alert box using JavaScript ?",
            "&lt;!DOCTYPE html&gt; &lt;html lang=&quot;en&quot;&gt; &lt;head&gt; &lt;meta"
                + " charset=&quot;UTF-8&quot;&gt; &lt;meta name=&quot;viewport&quot;"
                + " content=&quot;width=device-width, initial-scale=1.0&quot;&gt;"
                + " &lt;title&gt;Demo&lt;/title&gt; &lt;style&gt; h1 { color: green; } .container"
                + " { padding: 15px; width: 400px; } label, input { margin-bottom: 10px; } button"
                + " { float: right; margin-right: 10px; background-color: green; } &lt;/style&gt;"
                + " &lt;/head&gt; &lt;body&gt; &lt;center&gt; &lt;h1&gt;GeeksforGeeks&lt;/h1&gt;"
                + " &lt;b&gt;Display error without alert box&lt;/b&gt; &lt;br&gt;&lt;br&gt;"
                + " &lt;div class=&quot;container&quot;&gt; &lt;div&gt;"
                + " &lt;label&gt;Uername:&lt;/label&gt; &lt;input type=&quot;text&quot;"
                + " size=&quot;40&quot;&gt; &lt;/div&gt; &lt;div&gt; &lt;label&gt;Phone"
                + " no:&lt;/label&gt; &lt;input type=&quot;text&quot; id=&quot;number&quot;"
                + " size=&quot;40&quot;&gt; &lt;span id=&quot;error&quot;&gt;&lt;/span&gt;"
                + " &lt;/div&gt; &lt;button type=&quot;submit&quot;"
                + " onclick=&quot;errorMessage()&quot;&gt; Submit &lt;/button&gt; &lt;/div&gt;"
                + " &lt;/center&gt; &lt;/body&gt; &lt;script&gt; function errorMessage() { var"
                + " error = document.getElementById(&quot;error&quot;) if"
                + " (isNaN(document.getElementById(&quot;number&quot;).value)) { // Changing"
                + " content and color of content error.textContent = &quot;Please enter a valid"
                + " number&quot; error.style.color = &quot;red&quot; } else { error.textContent ="
                + " &quot;&quot; } } &lt;/script&gt; &lt;/html&gt;",
            "https://www.geeksforgeeks.org/how-to-display-error-without-alert-box-using-javascript/",
            "Errors in JavaScript can be displayed without the use of alert boxes but using the"
                + " alert box is the traditional way to do that. We can show errors with two"
                + " methods without using the alert box.",
            "0",
            "GeeksForGeeks",
            "https://geeksforgeeks.org/favicon.ico");
    assertEquals(expected, actual);
  }

  @Test
  public void noEscapeHtml() {
    Card actual =
        client.search(
            "https://www.geeksforgeeks.org/how-to-display-error-without-alert-box-using-javascript/",
            "javascript display error");
    Card expected =
        new Card(
            "How to display error without alert box using JavaScript ?",
            "Demo     \n"
                + "<h1>GeeksforGeeks</h1> <b>Display error without alert box</b> \n"
                + "<br>\n"
                + "<br> \n"
                + "<div> \n"
                + " <div> Uername:  \n </div> \n"
                + " <div> Phone no:  <span></span> \n"
                + " </div>  Submit  \n"
                + "</div>",
            "https://www.geeksforgeeks.org/how-to-display-error-without-alert-box-using-javascript/",
            "Errors in JavaScript can be displayed without the use of alert boxes but using the"
                + " alert box is the traditional way to do that. We can show errors with two"
                + " methods without using the alert box.",
            "GeeksForGeeks",
            "0",
            "https://geeksforgeeks.org/favicon.ico");
    assertEquals(expected, actual);
  }

  @Test
  public void dataStructuresCard() {
    Card actual =
        client.search(
            "https://www.geeksforgeeks.org/array-rotation/", "Program for array rotation");
    Card expected =
        new Card(
            "Program for array rotation",
            "// C++ program to rotate an array by // d elements #include  using namespace std;"
                + " /*Function to left Rotate arr[] of size n by 1*/ void leftRotatebyOne(int"
                + " arr[], int n) { int temp = arr[0], i; for (i = 0; i &lt; n - 1; i++) arr[i] ="
                + " arr[i + 1]; arr[i] = temp; } /*Function to left rotate arr[] of size n by d*/"
                + " void leftRotate(int arr[], int d, int n) { for (int i = 0; i &lt; d; i++)"
                + " leftRotatebyOne(arr, n); } /* utility function to print an array */ void"
                + " printArray(int arr[], int n) { for (int i = 0; i &lt; n; i++) cout &lt;&lt;"
                + " arr[i] &lt;&lt; \" \"; } /* Driver program to test above functions */ int"
                + " main() { int arr[] = { 1, 2, 3, 4, 5, 6, 7 }; int n = sizeof(arr) /"
                + " sizeof(arr[0]); // Function calling leftRotate(arr, 2, n); printArray(arr, n);"
                + " return 0; }",
            "https://www.geeksforgeeks.org/array-rotation/",
            "Write a function rotate(ar[], d, n) that rotates arr[] of size n by d elements.",
            "0",
            "GeeksForGeeks",
            "https://geeksforgeeks.org/favicon.ico");
    assertEquals(expected, actual);
  }

  @Test
  public void partiallyFilledCard() {
    Card actual =
        client.search("https://www.geeksforgeeks.org/category/geek-on-the-top/", "geek on top");
    assertNull(actual);
  }

  @Test
  public void nonexistentLink() {
    Card actual =
        client.search(
            "https://www.geeksforgeeks.org/analysis-of-algorithms-set-3asymptotic-notations/dijkstra",
            null);
    assertNull(actual);
  }

  @Test
  public void blankPageLink() {
    Card actual = client.search("https://www.geeksforgeeks.org/", "geeksforgeeks");
    assertNull(actual);
  }
}
