package ru.germanbpopov.chess;

public class Piece {
	
	//fields
	private int value;
	private boolean isWhite;
	private boolean isDead;
	private boolean madeFirstMove;
	private int cellIndex;
	private PieceType type;
	
	
	//constructor
	public Piece (PieceType type) {
		this.type = type;
		this.isDead = false;
	}
	
	//methods
	//getters
	public PieceType getPieceType(){
		return this.type;
	}
		
	public boolean getIsDead() {
		return this.isDead;
	}
	
	public boolean getIsWhite() {
		return this.isWhite;
	}
	
	public int getCellIndex() {
		return this.cellIndex;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public boolean getMadeFirstMove() {
		return this.madeFirstMove;
	}
	
	//setters
	public void setCellIndex(int cellIndex) {
		this.cellIndex = cellIndex;
	}
	
	public void setIsDead(boolean dead) {
		this.isDead = dead;
		this.cellIndex = -1;
	}
	
	public void setIsWhite(boolean white) {
		this.isWhite = white;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void setMadeFirstMove(boolean madeFirstMove) {
		this.madeFirstMove = madeFirstMove;
	}
	//misc
	
}