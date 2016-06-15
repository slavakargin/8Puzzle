import java.util.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int [] myBlocks;
    private int N = 0;
    private Stack<Board> neighbors = new Stack<Board>();
    public Board(int[][] blocks) {           // construct a board from an N-by-N array of blocks
      N = blocks.length;                     // (where blocks[i][j] = block in row i, column j)
      myBlocks = new int[N * N];
      for (int p = 0; p < N * N; p++) {
             int i = p / N;
            int j = p % N;
            myBlocks[p] = blocks[i][j];
            }
    }
    public int dimension() {                // board dimension N
        return N;
    }
    public int hamming() {                  // number of blocks out of place
      int h = 0;
      for (int i = 0; i < N * N - 1; i++) { // we do not include 0 as a block
              if (!(myBlocks[i] == (i + 1) % (N * N))) {
                  h++;
              }
          }
      return h;    
    }
    
    private static int mod(int a, int b) { // implements a (mod b)
       return (a % b + b) % b;
    }
    
    public int manhattan() {                // sum of Manhattan distances between blocks and goal
      int h = 0;
      for (int s = 0; s < N * N; s++) {
                  int x = myBlocks[s]; 
                  if (x != 0) {
                  int incr = Math.abs(s % N  - (x - 1) % N) + Math.abs((s / N - (x - 1) / N) % N);
                  h = h + incr; 
                  }
              }            
      return h;    
    }
    public boolean isGoal() {               // is this board the goal board?
      boolean h = true;
      for (int i = 0; i < N * N; i++) {
              if (!(myBlocks[i] == (i + 1) % (N * N))) {
                  h = false;
                  break;      
              }
          }
      return h;     
    }
    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        int[][] twinBlocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
            twinBlocks[i][j] = myBlocks[i * N + j];
            }
        }
        if (twinBlocks[0][0] == 0) {
            int x = twinBlocks[0][1];
            twinBlocks[0][1] = twinBlocks[1][0];
            twinBlocks[1][0] = x;
        } else if (twinBlocks[0][1] == 0) {
            int x = twinBlocks[0][0];
            twinBlocks[0][0] = twinBlocks[1][0];
            twinBlocks[1][0] = x; 
        } else {
            int x = twinBlocks[0][0];
            twinBlocks[0][0] = twinBlocks[0][1];
            twinBlocks[0][1] = x;  
        }
        return new Board(twinBlocks);       
    }
    public boolean equals(Object y) {       // does this board equal y?
        if (y == null) {
        return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this.N != that.N) return false;
        for (int p = 0; p < N * N; p++){
            if (this.myBlocks[p] != that.myBlocks[p]) return false;
        }
        return true;
    }
    public Iterable<Board> neighbors() {    // all neighboring boards
        neighbors = new Stack<Board>(); // This is what we are going to return
        int[][] W = new int[N][N]; // this will be one of the neighbors.
        Board newBoard;// This is what we will push on stack
        
        for (int i = 0; i < N * N; i++) { 
            int s = i / N;
            int t = i % N;
            W[s][t] = myBlocks[i];
        }
        int p = 0;
        while (myBlocks[p] != 0) {
            if (p > N * N - 1) throw new
                IllegalArgumentException("No 0 detected in the input");
            p++;
        }    
        if (p == 0) {
         newBoard = moveUp(p,W);
         neighbors.push(newBoard);
         newBoard = moveLeft(p,W);
         neighbors.push(newBoard);
         return neighbors;
        }
        if (p == N-1) {
         newBoard = moveUp(p,W);
         neighbors.push(newBoard);
         newBoard = moveRight(p,W);
         neighbors.push(newBoard);
         return neighbors;
        }
        if (p == N * (N - 1)){
         newBoard = moveDown(p,W);
         neighbors.push(newBoard);
         newBoard = moveLeft(p,W);
         neighbors.push(newBoard);
         return neighbors;
        }
        if (p == N * N -1){
         newBoard = moveDown(p,W);
         neighbors.push(newBoard);
         newBoard = moveRight(p,W);
         neighbors.push(newBoard);
         return neighbors;
        }
        if (p > 0 && p < N - 1) {
         newBoard = moveUp(p,W);
         neighbors.push(newBoard);
         newBoard = moveLeft(p,W);
         neighbors.push(newBoard);
         newBoard = moveRight(p,W);
         neighbors.push(newBoard);
         return neighbors;
        }
        if (p > N * (N - 1) && p < N * N - 1) {
         newBoard = moveDown(p,W);
         neighbors.push(newBoard);
         newBoard = moveLeft(p,W);
         neighbors.push(newBoard);
         newBoard = moveRight(p,W);
         neighbors.push(newBoard);
         return neighbors;
        }
        if (p % N == 0 && p > 0 && p < N * (N - 1)) {
         newBoard = moveUp(p,W);
         neighbors.push(newBoard);
         newBoard = moveLeft(p,W);
         neighbors.push(newBoard);
         newBoard = moveDown(p,W);
         neighbors.push(newBoard);
         return neighbors;
        }
        if (p % N == N - 1 && p > N - 1 && p < N * N - 1) {
         newBoard = moveUp(p,W);
         neighbors.push(newBoard);
         newBoard = moveDown(p,W);
         neighbors.push(newBoard);
         newBoard = moveRight(p,W);
         neighbors.push(newBoard);
         return neighbors;
        }
        
         newBoard = moveUp(p,W);
         neighbors.push(newBoard);
         newBoard = moveDown(p,W);
         neighbors.push(newBoard);
         newBoard = moveRight(p,W);
         neighbors.push(newBoard);
         newBoard = moveLeft(p,W);
         neighbors.push(newBoard);
        return neighbors;
    }
    
      
    private Board moveUp(int p, int[][] W) {
            int s = p / N;
            int t = p % N;
        int x = W[s][t];
        int y = W[s + 1][t];
        W[s][t] = y;
        W[s + 1][t] = x;
        Board newBoard = new Board(W);
        W[s][t] = x;
        W[s + 1][t] = y;
        return newBoard;
    }
    
    private Board moveDown(int p, int[][] W) {
            int s = p / N;
            int t = p % N;
        int x = W[s][t];
        int y = W[s - 1][t];
        W[s][t] = y;
        W[s - 1][t] = x;
        Board newBoard = new Board(W);
        W[s][t] = x;
        W[s - 1][t] = y;
        return newBoard;
    }
    
        private Board moveLeft(int p, int[][] W) {
            int s = p / N;
            int t = p % N;
        int x = W[s][t];
        int y = W[s][t + 1];
        W[s][t] = y;
        W[s][t + 1] = x;
        Board newBoard = new Board(W);
        W[s][t] = x;
        W[s][t + 1] = y;
        return newBoard;
    }
        
     private Board moveRight(int p, int[][] W) {
            int s = p / N;
            int t = p % N;
        int x = W[s][t];
        int y = W[s][t - 1];
        W[s][t] = y;
        W[s][t - 1] = x;
        Board newBoard = new Board(W);
        W[s][t] = x;
        W[s][t - 1] = y;
        return newBoard;
    }
     public String toString()   {            // string representation of this board (in the output format specified below)
          StringBuilder s = new StringBuilder();
     
          s.append(N + "\n");
          for (int p = 0; p < N * N; p++) {
            int j = p % N;
            s.append(String.format("%2d ", myBlocks[p]));
            if (j == N - 1) {
                s.append("\n");
              }
            }
    return s.toString();
    }
    
     public static void main(String[] args) {// unit tests (not graded)
         //test constructor
         int N = 3;
         int[][] blocks = new int[N][N];
         for (int p = 0; p < N * N; p++){
             int i = p / N;
             int j = p % N;
             blocks[i][j] = mod(p + 5,N * N); 
         }
         Board myBoard = new Board(blocks);   
         String s = myBoard.toString();

         StdOut.println(s);
         StdOut.println(myBoard.hamming());
         StdOut.println(myBoard.manhattan());
         StdOut.println(myBoard.isGoal());
         Board twinBoard = myBoard.twin();
         s = twinBoard.toString();
         StdOut.println(s);
         StdOut.println(myBoard.equals(twinBoard));
         Board copy = twinBoard.twin();
         s = copy.toString();
         StdOut.println(s);
         StdOut.println(myBoard.equals(copy));
         for (Board b : myBoard.neighbors()) {
             s = b.toString();
             StdOut.print(s);
         }
         //test for immutability
        Board x = myBoard;
        s = x.toString();
        StdOut.println(s);
        x = twinBoard;
        s = myBoard.toString();
        StdOut.println(s);
     }
}
