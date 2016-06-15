/*
 * This class is for solving 8puzzle. It uses class Board to store the boards 
 * of the game. The algorithm is 
 * based on A*-algorithm described by Segdewick in the book "Algorithms".
 */


import java.util.Stack;
 import edu.princeton.cs.algs4.MinPQ;
 import edu.princeton.cs.algs4.In;
 import edu.princeton.cs.algs4.StdOut;
 
public class Solver {
    private MinPQ<Pair> myHeap = new MinPQ<Pair>();
    private MinPQ<Pair> twinHeap = new MinPQ<Pair>(); //for the twin of the initial board;
    private int n = 0; //number of positions considered.
    private int moves; //number of moves needed to solve the problem 
    private Pair currentPair;

    public Solver(Board initial)   {        // find a solution to the initial board (using the A* algorithm)
       int priority = initial.manhattan();
       currentPair = new Pair(priority, initial, null);
       Pair parent;
       
       
       Board twin = initial.twin();
       int priority_twin = twin.manhattan();
       Pair pair_twin = new Pair(priority_twin, twin, null); 
       
       myHeap.insert(currentPair);
       twinHeap.insert(pair_twin);
       
       double max = Math.pow(10,8);
       while (n < max) {
       currentPair = myHeap.delMin(); //solving the original board; getting the board with the smallest priority
       Board current_board = currentPair.getBoard();
       moves = currentPair.getPriority() - current_board.manhattan();
       if (current_board.isGoal()) {  
           return;
       }
       //inserting neighbors into priority queue 
        Iterable<Board> s = current_board.neighbors(); //calculating neighbors.
        for (Board board : s){
            parent = currentPair.getParent();
            if (parent == null || ! board.equals(parent.getBoard())){
               priority = board.manhattan() + moves + 1;
               Pair pair = new Pair(priority, board,currentPair);
           myHeap.insert(pair);
           }
        }
        //Now trying to solve the twin board
       Pair twin_pair = twinHeap.delMin(); 
       Board twin_board = twin_pair.getBoard();
       if (twin_board.isGoal()) {
           moves = -1;
           return;
       }
       s = twin_board.neighbors();
        for (Board board : s){
            parent = twin_pair.getParent();
            if (parent == null || ! board.equals(parent.getBoard())){
             priority = board.manhattan() + moves + 1;
            Pair pair = new Pair(priority, board,twin_pair);
            twinHeap.insert(pair);
            } 
        }
        n++;
      }
       StdOut.println("Not found after " + n + " moves.");
    }
    public boolean isSolvable(){            // is the initial board solvable?
        return (moves > -1);
    }
    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        return moves;
    }
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable   
       Stack<Board> solution0 = new Stack<Board>(); 
       Stack<Board> solution = new Stack<Board>();
       Pair pair = currentPair;
       if (! isSolvable()) return null;   
       do {
           solution0.push(pair.getBoard());
           pair = pair.getParent();
       } while (pair != null);
       int n = solution0.size();
       
        for (int i = 0; i < n; i++){
            Board x = solution0.pop();
            solution.push(x);
        }
        return solution;
    }
    public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] blocks = new int[N][N];
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    //solve the puzzle
    Solver solver = new Solver(initial);

    //print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
}
}

 class Pair implements Comparable<Pair>{
        private int priority;
        private Board board;
        private Pair parent;
        public Pair( int priority, Board board, Pair parent) {
            this.priority = priority;
            this.board = board;
            this.parent = parent;
        }
        //overriding the compareTo method
        public int compareTo(Pair y) {
            return this.priority - y.priority;
        }
        public Board getBoard() {
            return board;
        }
        public int getPriority() {
            return priority;
        }
        public Pair getParent() {
            return parent;
        }
    }