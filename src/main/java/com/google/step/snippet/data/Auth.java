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

  public String getLoginUrl(String redirectPath) {
    return this.userService.createLoginURL(redirectPath);
  }

  public String getLogoutUrl(String redirectPath) {
    return this.userService.createLogoutURL(redirectPath);
  }
}
