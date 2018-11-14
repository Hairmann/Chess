package ru.germanbpopov.chess;

import java.awt.Color;
import java.awt.Graphics;

public class Presenter {
	
	private View view;
	private Model model;
	
	public Presenter(View view, Model model) {
		this.view = view;
		this.model = model;
	}
	
	
	
	//methods
	//work with View
	
	public void txaEcho(String message){
		this.view.postToLog(message);
	}
	
	//work with Model
	
	public void movePiece(int cellIndexFrom, int cellIndexTo){
		this.model.movePiece(cellIndexFrom, cellIndexTo);
		this.view.setChessBoard(this.model.getBoard());
		txaEcho(cellIndexToString(cellIndexFrom) + " -> " + cellIndexToString(cellIndexTo) + "  " + this.model.getTestText());
	}
	
	public void getAiMove(boolean aiIsWhite) {
		this.model.getAiMove(aiIsWhite);
		this.view.setChessBoard(this.model.getBoard());
		// txaEcho(cellIndexToString(cellIndexFrom) + " -> " + cellIndexToString(cellIndexTo)); // N/A: cellIndexes are in Model class only 
		this.view.setPlayerTurn(!aiIsWhite); //tell view, that player is allowed to move (by inverting the input boolean)
	}
	
	//FOR TESTING ONLY
	public void setRandomBoard() {
		this.model.randomizeBoard();
		this.view.setChessBoard(this.model.getBoard());
	}
	
	public void setInitialBoard() {
		this.model.initializeBoard();
		this.view.setChessBoard(this.model.getBoard());
	}
	
	public void getModelBoardHash() {
		txaEcho(this.model.getBoard().toString());
	}
	
	//
	public boolean[] getPossibleMoves(int cellIndex, boolean playerIsWhite) {
		return this.model.getPossibleMoves(cellIndex, playerIsWhite);
	}
	
	public Board getBoard() {
		return this.model.getBoard();
	}
	
	public void setPlayerColor(boolean isWhite) {
		this.view.setPlayerColor(isWhite);
	}
	
	public void setPlayerTurn(boolean isWhite) {
		this.view.setPlayerTurn(isWhite);
	}
	
	//PRIVATE METHODS
	//Transform cellIndex to cell literal name
	private String cellIndexToString(int cellIndex) {
		int indexX = cellIndex % 8;
		int indexY = (int) (cellIndex / 8) + 1;
		String answer = "";
		if (indexX == 0) answer = "a";
		if (indexX == 1) answer = "b";
		if (indexX == 2) answer = "c";
		if (indexX == 3) answer = "d";
		if (indexX == 4) answer = "e";
		if (indexX == 5) answer = "f";
		if (indexX == 6) answer = "g";
		if (indexX == 7) answer = "h";
		answer = answer + Integer.toString(indexY);
		return answer;
	}
}