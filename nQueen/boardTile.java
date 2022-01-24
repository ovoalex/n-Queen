package nQueen;

//getters and setters for board tile
public class boardTile {
	
	private boolean hasQueen;
	private int x;
	private int y;
	private int value;
	
	public boardTile(){
		hasQueen = false;
	}
	
	public boardTile(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public boardTile(boardTile copy){
		this.x = copy.x;
		this.y = copy.y;
		this.hasQueen = copy.hasQueen;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public boolean hasQueen(){
		return hasQueen;
	}
	
	public void placeQ(){
		this.hasQueen = true;
	}
	
	public void removeQ(){
		this.hasQueen = false;
	}
	
	public String toString(){
		return "("+x+","+y+") "+value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
