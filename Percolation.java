//Written by Margaret Wismer September 4, 2013
//Creates a percolation class with an N by N grid. 
//Test client randomly opens nodes in grid until the 
//sytem percolates which means there is an open path 
//between the top and bottom rows. 

public class Percolation {
    private boolean[][] openSite; //whether a node is open or closed. Open = true
    private WeightedQuickUnionUF uf;
    //second UF has only top virtual site to fix backwash problem
    //will not connect to a bottom virtual node.
    private WeightedQuickUnionUF uf1; 
    private int N1;
  
    //tests whether a node in the top row
    private boolean toprow(int i) {
        if (i == 1)
            return true;
        else
            return false;
    }
    
    //tests whether a node in the bottom row
    private boolean bottomrow(int i) {
        if (i == N1)
            return true;
        else 
            return false;
    }
    
    //tests whether node on left boundary of grid
    private boolean leftside(int j) {
        if (j == 1)
            return true;
        else
            return false;
    }
    
    //tests whether node on right boundary of grid
    private boolean rightside(int j) {
        if (j == N1)
            return true;
        else 
            return false;
    }
   
    //Percolation constructor
    public Percolation(int N) {
        N1 = N;
        openSite = new boolean[N+1][N+1];
        for(int i = 0; i < N+1; i++) {
            for(int j = 0; j < N+1; j++) {
                openSite[i][j] = false;
            }
        }
        if (N == 1) {
            
            uf = new WeightedQuickUnionUF(N*N+3);
            uf1 = new WeightedQuickUnionUF(N*N+2);
        }
        else {
            uf = new WeightedQuickUnionUF(N*N+2);
            uf1 = new WeightedQuickUnionUF(N*N+1);
        }
    }
    
    //N*N is location of virtual top node, N*N+1 is location 
    //virtual bottom node. 
    public boolean percolates() {
        return uf.connected(N1*N1, N1*N1+1);
    }
    
    public boolean isOpen(int i, int j) {
        if((i < 1) || (i > N1) || (j < 1) || (j > N1)) {
            throw new IndexOutOfBoundsException("index out of bounds ");
        }
        else
            return openSite[i][j];
    }
    
    //a node is Full when there is a path between it and the top virtual node. 
    public boolean isFull(int i, int j) {
        if((i < 1) || (i > N1) || (j < 1) || (j > N1)) {
            throw new IndexOutOfBoundsException("index out of bounds ");
        }
        else {
            int site = (i-1)*N1 + j - 1;
            return (uf1.connected(site, N1*N1) && openSite[i][j]);
        }
    }
    
    //this method is long because it has to determine whether a node is on the boundary, corner
    //and which boundary corner. 
    public void open(int i, int j) {
        if((i < 1) || (i > N1) || (j < 1) || (j > N1))
            throw new IndexOutOfBoundsException("index out of bounds ");
        if (N1 == 1) {
            i = 1;
            j = 1;
        }
        int site = N1*(i-1) + j-1; //grid needs to go from 1 to N1
        openSite[i][j] = true;
        if (N1 == 1) { //pathological case
            uf.union(site,N1*N1);
            uf.union(site,N1*N1+1);
            uf1.union(site,N1*N1);
            uf1.union(site,N1*N1+1);
            return;
        }
        //union site with adjacent open sites. 
        if (toprow(i)) { 
            if (!uf.connected(site, N1*N1)) {
                uf.union(site, N1*N1); //union to virtual top site. 
                uf1.union(site, N1*N1);
            }
            if (isOpen(i+1,j)) {
                uf.union(site, site + N1); //union to open node below site in 1D array
                uf1.union(site, site + N1);
            }
            if (leftside(j)) {
                if(isOpen(i,j+1))  {
                    uf.union(site, site + 1); //union to open node to right
                    uf1.union(site, site + 1);
                }
            }
            else if (rightside(j)) {
                if(isOpen(i,j-1)) {
                    uf.union(site, site - 1); //union to open node to left
                    uf1.union(site, site - 1);
                }
            }
            else {
                if(isOpen(i,j+1)) {
                    uf.union(site, site + 1); 
                    uf1.union(site, site + 1);
                }
                if(isOpen(i,j-1)) {
                    uf.union(site, site - 1);
                    uf1.union(site, site - 1);
                }
            }
        }
        else if (bottomrow(i)) {
            if(!uf.connected(site, N1*N1+1))
                uf.union(site, N1*N1+1); //union to virtual bottom site
            if(isOpen(i-1,j)) {
                uf.union(site, site-N1); //union to open node above site in 1D array
                uf1.union(site, site-N1);
            }
            if(leftside(j)) {
                if(isOpen(i,j+1)) {
                    uf.union(site, site+1); //union to node on right
                    uf1.union(site, site+1);
                }
            }
            else if(rightside(j)) {
                if(isOpen(i,j-1)) {
                    uf.union(site, site - 1); //union to node on left
                    uf1.union(site, site - 1);
                }
            }
            else {
                if(isOpen(i,j+1)) {
                    uf.union(site, site + 1); 
                    uf1.union(site, site + 1);
                }
                if(isOpen(i,j-1)) {
                    uf.union(site, site - 1);
                    uf1.union(site, site - 1);
                }
            }
        }
        else if (leftside(j)) {
            if (isOpen(i-1,j)) {
                uf.union(site, site-N1);
                uf1.union(site, site-N1);
            }
            if (isOpen(i+1,j)) {
                uf.union(site, site+N1);
                uf1.union(site, site+N1);
            }
            if (isOpen(i,j+1)) {
                uf.union(site, site+1);
                uf1.union(site, site+1);
            }
        }
        else if (rightside(j)) {
            if (isOpen(i-1,j)) {
                uf.union(site, site-N1);
                uf1.union(site, site-N1);
            }
            if (isOpen(i+1,j)) {
                uf.union(site, site+N1);
                uf1.union(site, site+N1);
            }
            if (isOpen(i,j-1)) {
                uf.union(site, site-1);
                uf1.union(site, site-1);
            }
        }
        //node is in center of grid which is most likely.
        else {
            if(isOpen(i,j+1)) {
                uf.union(site, site + 1); 
                uf1.union(site, site + 1);
            }
            if(isOpen(i,j-1)) {
                uf.union(site, site - 1); 
                uf1.union(site, site - 1);
            }
            if(isOpen(i-1,j)) {
                uf.union(site, site - N1);
                uf1.union(site, site - N1);
            }
            if(isOpen(i+1,j)) {
                uf.union(site, site + N1);
                uf1.union(site, site + N1);
            }
        }
    }
    
    public static void main(String[] args) {
        int i = 0,j = 0;
        int NumOpenSites = 0;
 //       StdOut.println("Hello World");
        int GridSize = Integer.parseInt(args[0]);
        Percolation PercTest = new Percolation(GridSize);
        while (!PercTest.percolates()) {
            i = StdRandom.uniform(GridSize) + 1;
            j = StdRandom.uniform(GridSize) + 1;
            if (!PercTest.isOpen(i,j)) {
                PercTest.open(i,j);
                NumOpenSites = NumOpenSites + 1;
            }
        }
        StdOut.println("Percolation is " + NumOpenSites);
    }
}