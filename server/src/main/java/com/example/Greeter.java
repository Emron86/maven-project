package com.example;

/**
 * This is a class.
 */
public class Greeter {

  /**
   * This is a constructor.
   */
  public Greeter() {

  }
  /**
   * @param someone the name of the person
   * @return greeting string
   */
  public String greet(final String someone) {
    final return String.format("Hello, %s!", someone);
  }
}
