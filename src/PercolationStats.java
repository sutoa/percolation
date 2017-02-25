import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

import static edu.princeton.cs.algs4.StdRandom.uniform;
import static java.lang.String.format;

public class PercolationStats {
    private final int runs;
    private double[] thresholdPct;
    public static void main(String[] args) {
        if(args.length != 2) throw new IllegalArgumentException("missing size or run number");

        final int size = Integer.parseInt(args[0]);
        final int runs = Integer.parseInt(args[1]);

        final PercolationStats stats = new PercolationStats(size, runs);
        System.out.println(format("mean:            %s", stats.mean()));
        System.out.println(format("stddev:          %s", stats.stddev()));
        System.out.println(format("confidenceLo:    %s", stats.confidenceLo()));
        System.out.println(format("confidenceHi:    %s", stats.confidenceHi()));
    }

    public PercolationStats(int size, int runs) {
        if(size <= 0 || runs <= 0) throw new IllegalArgumentException("size and runs must be positive integers");

        this.runs = runs;
        thresholdPct = new double[runs];
        Stopwatch stopwatch = new Stopwatch();
        for (int i = 0; i < runs; i++) {
            System.out.println(format("Run # %s ......", i));
            thresholdPct[i] = openSiteOneAtATimeTillPercolation(size);
        }
        final double t = stopwatch.elapsedTime();
        System.out.println(format("%s runs for size %s took %s seconds", runs, size, t));
        System.out.println(format("average run time is %s seconds", t/runs));
    }

    public double mean(){
        return StdStats.mean(thresholdPct);
    }

    public double stddev(){
        return StdStats.stddev(thresholdPct);
    }

    public double confidenceLo() {
        return mean() - 1.96*stddev()/Math.sqrt(runs);
    }

    public double confidenceHi() {
        return mean() + 1.96*stddev()/Math.sqrt(runs);
    }


    private static double openSiteOneAtATimeTillPercolation(int gridSize) {
        final Percolation p = new Percolation(gridSize);
        final double numberOfSites = gridSize * gridSize;

        List<Integer> closedSites = new ArrayList<>();
        for (int i = 0; i < numberOfSites; i++) {
            closedSites.add(i);
        }

        while (!p.percolates()) {
            openSiteRandomly(p, closedSites);
        }

        return 1 - closedSites.size()/ (numberOfSites);

    }

    private static void openSiteRandomly(Percolation p, List<Integer> closedSites) {
        final int uniform = uniform(closedSites.size());
        final int siteToOpen = closedSites.get(uniform);
        p.open(siteToOpen / p.size(), siteToOpen % p.size());
        closedSites.remove(uniform);
    }
}