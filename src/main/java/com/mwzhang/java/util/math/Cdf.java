package com.mwzhang.java.util.math;

import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Mingwei on 12/9/15.
 * <p>
 * CDF related static functionality
 * <p>
 * Generate CDF with an input of a list of Integers.
 */
public class Cdf {

    Frequency freq;
    DescriptiveStatistics stats;

    TreeSet<Double> listDoubles;
    TreeSet<Integer> listIntegers;

    public Cdf() {
        // do something
        this.freq = new Frequency();
        this.stats = new DescriptiveStatistics();
        this.listDoubles = new TreeSet<>();
        this.listIntegers = new TreeSet<>();
    }

    public void addInt(Integer x) {
        freq.addValue(x);
        stats.addValue(x);
        listIntegers.add(x);
    }

    public void addFloat(double x) {
        freq.addValue(x);
        stats.addValue(x);
        listDoubles.add(x);
    }

    public Pair<List<Integer>, List<Double>> getCdfInt() {
        List<Double> percentiles = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (Integer n : listIntegers) {
            percentiles.add(freq.getCumPct(n));
            values.add(n);
        }

        return new Pair<>(values, percentiles);
    }

    public Pair<List<Double>, List<Double>> getCdfFloat() {
        List<Double> percentiles = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        for (Double n : listDoubles) {
            percentiles.add(freq.getCumPct(n));
            values.add(n);
        }
        return new Pair<>(values, percentiles);
    }

    public void reset() {
        stats.clear();
        freq.clear();
    }
}
