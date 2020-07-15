package com.google.step.snippet.external;

import com.google.step.snippet.data.Card;

public interface Client {
  Card search(String url);

  String getCSEId();
}
