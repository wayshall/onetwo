package org.xtend;

import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class HelloWorld {
  public static void main(final String[] args) {
    String _firstUpper = StringExtensions.toFirstUpper("wayshall");
    String _plus = ("hello world, " + _firstUpper);
    InputOutput.<String>println(_plus);
  }
}
