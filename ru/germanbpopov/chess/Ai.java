package ru.germanbpopov.chess;

import java.util.Random;
import java.util.LinkedList;

public class Ai {
	private final boolean CRAZY_BOT = true;
	private final Random rnd = new Random();
	
	//for move:
	int cellIndexFrom;
	int cellIndexTo;
	
	private Model model;
	private Board board;
	
	//constructor
	Ai(Model model) {
		this.model = model;
	}
	
	//public methods
	//setters
	public void setBoard(Board board) {
		this.board = board;
	}
	
	//getters
	public int getCellIndexFrom() {
		return this.cellIndexFrom;
	}
	
	public int getCellIndexTo() {
		return this.cellIndexTo;
	}
	
	
	public void performRandomMove(boolean isWhite) {
		boolean[] possibleMoves = null;
		LinkedList<Integer> availableMoves = new LinkedList<Integer>();
		int count = 0; // for counting the number of possible moves from cell
		int pieceIndex, cellIndex = -1;
		Piece piece;
		
		//random from
		if (isWhite) {
			while (count == 0) { //to check if there are any possible moves from the picked piece
				pieceIndex = 0 + rnd.nextInt(16);
				piece = this.board.getPieceByIndex(pieceIndex);
				if (piece.getIsDead() == false) {
					cellIndex = findPieceOnBoard(piece);
					possibleMoves = this.model.getPossibleMoves(cellIndex, isWhite);
					for (int i = 0; i < 64; i++) {
						if (possibleMoves[i]) count++;
					}
				}
			}
		}
		else {
			while (count == 0) { 
				pieceIndex = 16 + rnd.nextInt(16);
				piece = this.board.getPieceByIndex(pieceIndex);
				if (piece.getIsDead() == false) {
					cellIndex = findPieceOnBoard(piece);
					possibleMoves = this.model.getPossibleMoves(cellIndex, isWhite);
					for (int i = 0; i < 64; i++) {
						if (possibleMoves[i]) count++;
					}
				}
			}
		}
		this.cellIndexFrom = cellIndex;
		
		//select a random destination from ONLY the available ones
		for (int i = 0; i < possibleMoves.length; i++) {
			if (possibleMoves[i]) availableMoves.add( i );
		}
		this.cellIndexTo = availableMoves.get( rnd.nextInt( availableMoves.size() ) );
		
	}
	
	//private methods
	private int findPieceOnBoard(Piece piece) {
		int answer = -1;
		for (int i = 0; i < 64; i++) {
			if (this.board.getBoardStateCell(i).getIsOcupied()) {
				if (piece.equals(this.board.getBoardStateCell(i).getOcupiedBy())) {
					answer = i;
					return answer;
				}
			}
		}
		return answer;
	} 
}