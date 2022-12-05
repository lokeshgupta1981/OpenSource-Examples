package com.howtodoinjava.demo.jmh;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 1000, timeUnit =  TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit =  TimeUnit.MILLISECONDS)
@Fork(1)
public class ApplicationBenchmarks {

  public static void main(String[] args) throws Exception {
    org.openjdk.jmh.Main.main(args);
  }

  /*@State(Scope.Benchmark)
  public static class Params {
    public double a = 1;
    public double b = 1;
  }

  @Benchmark
  public static double add(Params params) {
    return params.a + params.b;
  }*/
  @State(Scope.Benchmark)
  public static class Params {

    public int listSize = 10_000_000;  //1M
    public double b = 1;
  }


  @Benchmark
  public static void addSelectUsingArrayList(Params param, Blackhole blackhole) {

    ArrayList<String> arrayList = new ArrayList<>();
    for (int i = 0; i < param.listSize; i++) {
      arrayList.add("prefix_" + i);
    }

    for (int i = 0; i < param.listSize; i++) {
      blackhole.consume(arrayList.get(i));
    }
  }

  @Benchmark
  public static void addSelectUsingLinkedList(Params param, Blackhole blackhole) {

    LinkedList<String> linkedList = new LinkedList<>();
    for (int i = 0; i < param.listSize; i++) {
      linkedList.add("prefix_" + i);
    }

    for (int i = 0; i < param.listSize; i++) {
      blackhole.consume(linkedList.get(i));
    }
  }
}



