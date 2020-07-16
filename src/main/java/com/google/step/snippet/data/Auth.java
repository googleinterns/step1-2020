package com.google.step.snippet.data;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class Auth {

  UserService userService;

  public Auth() {
    this.userService = UserServiceFactory.getUserService();
  }

  public boolean isLoggedIn() {
    return this.userService.isUserLoggedIn();
  }

  public String getUrl(String redirectPath) {
    if (isLoggedIn()) {
      return this.userService.createLogoutURL(redirectPath);
    }
    return this.userService.createLoginURL(redirectPath);
  }

  public String getLabel() {
    if (isLoggedIn()) {
      return "Logout";
    }
    return "Login";
  }
}
