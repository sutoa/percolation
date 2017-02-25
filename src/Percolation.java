import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import static java.util.Arrays.fill;
import static java.util.stream.IntStream.range;

public class Percolation {
    private final WeightedQuickUnionUF sites;
    private final int size;
    private SiteStatus[] statuses;

    public Percolation(int n) {
        size = n;
        sites = new WeightedQuickUnionUF(size * size);
        statuses = new SiteStatus[size * size];
        fill(statuses, SiteStatus.CLOSED);

    }

    public int size() {
        return size;
    }

    public void open(int row, int col) {
        if (isOpen(row, col)) return;

        final int index = index(row, col);

        statuses[index] = SiteStatus.OPEN;

        if (row > 0)
            connectIfBothSitesAreOpen(index - size, index);
        if (row < size - 1)
            connectIfBothSitesAreOpen(index + size, index);
        if (col > 0)
            connectIfBothSitesAreOpen(index - 1, index);
        if (col < size - 1)
            connectIfBothSitesAreOpen(index + 1, index);
    }

    private void connectIfBothSitesAreOpen(int site1, int site2) {
        if (isOpen(site1) && isOpen(site2))
            sites.union(site1, site2);
    }

    public boolean isOpen(int row, int col) {
        return isOpen(index(row, col));
    }

    public boolean isOpen(int i) {
        return statuses[i] == SiteStatus.OPEN;
    }

    public boolean percolates() {
        return range(0, size)
                .anyMatch(x -> isFull(size - 1, x));
    }

    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) return false;

        final int pos = index(row, col);
        return range(0, size)
                .anyMatch(x -> isOpen(0, x) && sites.connected(x, pos));
    }

    private int index(int row, int col) {
        return row * size + col;
    }

    public static enum SiteStatus {
        OPEN, FULL, CLOSED;
    }
}