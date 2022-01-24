package nQueen;

public class chessBoard {

	private boardTile[][] board;
	private int SIZE;
	
	public chessBoard(int n){
		this.board = new boardTile[n][n];
		this.SIZE = n;
		initchessBoard();
	}
	
	public chessBoard(chessBoard copy){
		this.SIZE = copy.getSize();
		this.board = copychessBoard(copy);
	}
	//copy board tile in chess board
	private boardTile[][] copychessBoard(chessBoard copy) {
		boardTile[][] toReturn = new boardTile[SIZE][SIZE];
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				toReturn[i][j] = new boardTile(copy.board[i][j]);
			}
		}
		return toReturn;
	}

	private void initchessBoard() {
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				board[i][j] = new boardTile(i,j);
			}
		}
	}
	//get tiles from board
	public boardTile getTile(int x, int y){
		return board[x][y];
	}
	//get queen coords from the board tile
	public boardTile getTile(qSolver.Coords coords){
		return board[coords.x][coords.y];
	}
	
	public int getSize(){
		return this.SIZE;
	}
	//convert to string to print out chess board pieces with queens in it
	public String toString(){
		StringBuilder toReturn = new StringBuilder("");
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(getTile(i,j).hasQueen())
					toReturn.append("[Q]");
				else
					toReturn.append("[_]");
			}
			toReturn.append("\n");
		}
		return toReturn.toString();
	}

}
