package ru.germanbpopov.chess;

public class Cell {
	
	private int xPos, yPos;
	boolean isOcupied;
	Piece ocupiedBy;
	
	public Cell(int xPos, int yPos) {
		this.xPos = xPos;	
		this.yPos = yPos;
	}
	
	
	//methods
	//getters
	public int getX() {
		return this.xPos;
	}
	
	public int getY() {
		return this.yPos;
	}
	
	public boolean getIsOcupied() {
		return this.isOcupied;
	}
	
	public Piece getOcupiedBy() {
		return this.ocupiedBy;
	}
	
	public int getCellNumber() {
		int cellNumber = 8 * this.yPos + this.xPos;
		return cellNumber;
	}
	//setters
	public void setX(int xPos) {
		this.xPos = xPos;
	}
	
	public void setY(int yPos) {
		this.yPos = yPos;
	}
	
	public void setIsOcupied(boolean ocupied) {
		this.isOcupied = ocupied;
	}
	
	public void setOcupiedBy(Piece ocupant) {
		this.ocupiedBy = ocupant;
		if (ocupant != null) {
			ocupant.setCellIndex(xyToCellIndex(this.xPos, this.yPos));
		}
	}
	
	
	//private methods
	private int xyToCellIndex(int x, int y) {
		return (y * 8 + x);
	}
}