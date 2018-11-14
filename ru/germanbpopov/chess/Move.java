package ru.germanbpopov.chess;

public class Move {
	
	//Fields
	private Piece pieceToMove;
	private Cell cellFrom;
	private Cell cellTo;
	
	//Constructor
	public Move(Piece piece, Cell from, Cell to) {
		this.pieceToMove = piece;
		this.cellFrom = from;
		this.cellTo = to;
	}
}