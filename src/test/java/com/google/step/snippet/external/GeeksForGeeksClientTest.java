package com.google.step.snippet.external;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.step.snippet.data.Card;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class GeeksForGeeksClientTest {

  private final GeeksForGeeksClient client = new GeeksForGeeksClient("CSE_ID");

  @Test
  public void htmlCodeCard() {
    Card actual =
        client.search(
            "https://www.geeksforgeeks.org/how-to-change-the-height-of-br-tag/", "html br");
    Card expected =
        new Card(
            "How to change the height of br tag?",
            "Customized break example     \n"
                + "&lt;h3&gt;This page shows different break height between lines&lt;/h3&gt; \n"
                + "&lt;p&gt; Hi User &lt;span&gt;&lt;/span&gt; Welcome to &lt;span&gt;&lt;/span&gt;"
                + " Geeks for geeks. &lt;span&gt;&lt;/span&gt; Hope you have enjoyed your stay."
                + " &lt;/p&gt;",
            "https://www.geeksforgeeks.org/how-to-change-the-height-of-br-tag/",
            "You can&rsquo;t change the height of \n"
                + "&lt;br&gt; tag as its not an HTML element, it is just an instruction which"
                + " enforces a line break. br does not take up any space in the page.");
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
            "Write a function rotate(ar[], d, n) that rotates arr[] of size n by d elements.");
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
