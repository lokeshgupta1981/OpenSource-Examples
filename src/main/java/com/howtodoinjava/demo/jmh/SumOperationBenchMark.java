package com.howtodoinjava.demo.jmh;

import org.openjdk.jmh.annotations.Benchmark;

public class SumOperationBenchMark {
  @Benchmark
  public void testMethod() {
    int a = 1;
    int b = 2;
    int sum = a + b;
    System.out.println(sum);
  }
}
