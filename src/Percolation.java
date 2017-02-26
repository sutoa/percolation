import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Arrays;


public class Percolation {
    private final WeightedQuickUnionUF sites;
    private final int size;
    private SiteStatus[] statuses;
    private int numberOfOpenSites;

    public Percolation(int n) {
        if(n <=0) {
            throw new IllegalArgumentException();
        }
        size = n;
        sites = new WeightedQuickUnionUF(size * size);
        statuses = new SiteStatus[size * size];
        Arrays.fill(statuses, SiteStatus.CLOSED);
        numberOfOpenSites = 0;

    }

    public void open(int row, int col) {
        checkSitePosition(row, col);
        if (isOpen(row, col)) return;

        final int index = index(row, col);

        statuses[index] = SiteStatus.OPEN;
        numberOfOpenSites++;

        if (row > 1)
            connectIfBothSitesAreOpen(index - size, index);
        if (row < size)
            connectIfBothSitesAreOpen(index + size, index);
        if (col > 1)
            connectIfBothSitesAreOpen(index - 1, index);
        if (col < size)
            connectIfBothSitesAreOpen(index + 1, index);
    }

    private void connectIfBothSitesAreOpen(int site1, int site2) {
        if (statuses[site1] == SiteStatus.OPEN && statuses[site2] == SiteStatus.OPEN)
            sites.union(site1, site2);
    }

    public boolean isOpen(int row, int col) {
        checkSitePosition(row, col);
        return statuses[index(row, col)] == SiteStatus.OPEN;
    }

    private void checkSitePosition(int row, int col) {
        if (row <= 0 || row > size) throw new ArrayIndexOutOfBoundsException();
        if (col <= 0 || col > size) throw new ArrayIndexOutOfBoundsException();
    }

    public boolean percolates() {
        for (int i = 1; i <= size; i++) {
            if (isFull(size, i)) return true;
        }
        return false;
    }

    public boolean isFull(int row, int col) {
        checkSitePosition(row, col);
        if (!isOpen(row, col)) return false;

        final int pos = index(row, col);
        for (int i = 1; i <= size; i++) {
            if (isOpen(1, i) && sites.connected(index(1, i), pos)) return true;
        }
        return false;
    }

    private int index(int row, int col) {
        return (row - 1) * size + col - 1;
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    private enum SiteStatus {
        OPEN, CLOSED;
    }
}