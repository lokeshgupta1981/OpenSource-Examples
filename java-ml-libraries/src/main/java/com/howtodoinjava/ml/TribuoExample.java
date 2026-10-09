package com.howtodoinjava.ml;

import org.tribuo.Example;
import org.tribuo.Model;
import org.tribuo.MutableDataset;
import org.tribuo.Prediction;
import org.tribuo.classification.Label;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.sgd.linear.LogisticRegressionTrainer;
import org.tribuo.impl.ArrayExample;
import org.tribuo.provenance.SimpleDataSourceProvenance;

public class TribuoExample {

  static Example<Label> fruit(String label, double weight, double length) {
    return new ArrayExample<>(new Label(label), new String[] {"weight", "length"}, new double[] {weight, length});
  }

  public static void main(String[] args) {
    LabelFactory factory = new LabelFactory();
    MutableDataset<Label> data = new MutableDataset<>(new SimpleDataSourceProvenance("fruits", factory), factory);
    data.add(fruit("apple", 150, 7));
    data.add(fruit("apple", 170, 8));
    data.add(fruit("apple", 140, 7));
    data.add(fruit("apple", 160, 8));
    data.add(fruit("banana", 120, 18));
    data.add(fruit("banana", 130, 20));
    data.add(fruit("banana", 110, 17));
    data.add(fruit("banana", 125, 19));

    Model<Label> model = new LogisticRegressionTrainer().train(data);

    Prediction<Label> p = model.predict(fruit("unknown", 118, 19));
    System.out.println("118 g, 19 cm -> " + p.getOutput().getLabel());
    System.out.println("Trained by: " + model.getProvenance().getTrainerProvenance().getClassName());
  }
}
