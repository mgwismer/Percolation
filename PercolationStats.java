//written by Margaret Wismer September 4, 2013
//Calls the Percolation class T times and calculates 
//statistics on the results. 

public class PercolationStats {
    private double[] FracOpen;
    private int N1;
    private int T1;
    
    public PercolationStats(int N, int T) {
        FracOpen = new double[T];
        N1 = N;
        T1 = T;
        if((T1 <= 0)||(N1 <= 0))
            throw new IllegalArgumentException("out of range");
        for(int k = 0; k < T; k++)
        {
            double NumSitesOpen = 0;
            Percolation PercTest = new Percolation(N);
            while (!PercTest.percolates()) {
                int i = StdRandom.uniform(N) + 1;
                int j = StdRandom.uniform(N) + 1;
                if (!PercTest.isOpen(i,j)) {
                    PercTest.open(i,j);
                    NumSitesOpen = NumSitesOpen + 1;
                }
            }
            FracOpen[k] = NumSitesOpen/(N*N);
        }
    }
    
    public double mean() {
        double CalcMean = 0;
        for(int k = 0; k < T1; k++) {
            CalcMean = CalcMean + FracOpen[k];
        }
        return CalcMean/T1;
    }
    
    public double stddev() {
        double var1 = 0;
        for(int k = 0; k  < T1; k++)
            var1 = var1 + (FracOpen[k] - mean())*(FracOpen[k] - mean());
        return Math.sqrt(var1/(T1-1));
    }
    
    public double confidenceLo() {
        return mean()-1.96*Math.sqrt(stddev())/T1;
    }
    
    public double confidenceHi() {
        return mean()+1.96*Math.sqrt(stddev())/T1;
    }
    
    public static void main(String[] args) {
        int i=0, j=0;
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats PercStats = new PercolationStats(N,T);
        StdOut.println("mean = " + PercStats.mean());
        StdOut.println("stddev = " + PercStats.stddev());
        StdOut.println("95% confidence interval = " + PercStats.confidenceLo() + ", " + PercStats.confidenceHi());


    }
}