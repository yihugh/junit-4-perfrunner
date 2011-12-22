package org.jboss.perfrunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.runner.Description;

class PerfRunDescription {
  private static final Pattern methodNameValuesPattern = Pattern.compile("(.*)\\[([0-9, .e+-]*)\\]");
  private static final Pattern methodValuePattern = Pattern.compile("[0-9.e+-]+");

  private final String className;
  private final String methodName;
  private final List<Double> paramValues;

  public PerfRunDescription(Description d) {
    className = d.getClassName();
    Matcher m = methodNameValuesPattern.matcher(d.getMethodName());
    if (!m.matches()) {
      throw new AssertionError("Unrecognized method name+value syntax in test description '" + d.getMethodName() + "'");
    }
    methodName = m.group(1);
    String values = m.group(2);

    Matcher vm = methodValuePattern.matcher(values);
    List<Double> valueList = new ArrayList<Double>();
    while (vm.find()) {
      valueList.add(Double.valueOf(vm.group()));
    }
    this.paramValues = Collections.unmodifiableList(valueList);
  }

  public String getClassName() {
    return className;
  }

  public String getMethodName() {
    return methodName;
  }

  /**
   * Returns an unmodifiable list of the parameter values that were used for this test run.
   */
  public List<Double> getParamValues() {
    return paramValues;
  }
}