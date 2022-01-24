package nQueen;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
	chessBoard board;
	int value;
	List<qSolver.Coords> queens;
	//to get current nodes from the chess board
	public Node(chessBoard board, int value, ArrayList<qSolver.Coords> queens){
		this.board = board;
		this.value = value;
		this.queens = queens;
	}
	//to copy the node
	public Node(Node copy){
		this.board = new chessBoard(copy.board);
		this.value = copy.value;
		this.queens = new ArrayList<qSolver.Coords>(copy.queens);
	}
	//update the chess board
	public void update(chessBoard board, int value, ArrayList<qSolver.Coords> queens){
		this.board = board;
		this.value = value;
		this.queens = queens;
	}
	//setting value on chess board
	public void setValue(int value){
		this.value = value;
	}
	//convert to string
	public String toString(){
		return (this.board.toString() + '\n' + this.value);
	}
	
	@Override
	public int compareTo(Node o) {
		return Integer.compare(this.value, o.value);
	}
}
