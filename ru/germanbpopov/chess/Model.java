package ru.germanbpopov.chess;

public class Model {
	
	private Board board;
	private Ai ai;
	
	private String testText = "";
		
	public Model(){
		this.board = new Board();
		this.ai = new Ai(this);
		this.ai.setBoard(this.board);
	}
	
	
	//Methods
	//initiate old board for a new game
	/**
	* <p>Initializes the board. Sets all the pieces to theirs' starting positions.</p>
	*/
	public void initializeBoard() {
		this.board.setInitialPiecesDisposition();
	}
	
	public void randomizeBoard() {
		this.board.setRandomPiecesDisposition();
	}
	
	//getters
	public Board getBoard() {
		return this.board;
	}
	
	
	//setters
	public void setBoard(Board board) {
		this.board = board;
	}
	
	public void movePiece(int cellIndexFrom, int cellIndexTo){
		Piece piece = getCellByIndex(cellIndexFrom).getOcupiedBy();
		
		if (!piece.getMadeFirstMove()) { //check if piece eveer made a move
			piece.setMadeFirstMove(true); //if it did set its moved flag to true
			
			//proper king's castling
			int indexX_from = cellIndexFrom % 8;
			int indexX_to = cellIndexTo % 8;
			if ( piece.getPieceType() == PieceType.KING && Math.abs(indexX_from - indexX_to) > 1 ) { //check if it was King, which castled
				switch (cellIndexTo) {
					case 2:
						getCellByIndex(3).setIsOcupied(true);
						getCellByIndex(3).setOcupiedBy( getCellByIndex(0).getOcupiedBy() ); //swap the rook
						getCellByIndex(0).setIsOcupied(false);
						getCellByIndex(0).setOcupiedBy(null);
						break;
					case 6:
						getCellByIndex(5).setIsOcupied(true);
						getCellByIndex(5).setOcupiedBy( getCellByIndex(7).getOcupiedBy() ); //swap the rook
						getCellByIndex(7).setIsOcupied(false);
						getCellByIndex(7).setOcupiedBy(null);
						break;
					case 58:
						getCellByIndex(59).setIsOcupied(true);
						getCellByIndex(59).setOcupiedBy( getCellByIndex(56).getOcupiedBy() ); //swap the rook
						getCellByIndex(56).setIsOcupied(false);
						getCellByIndex(56).setOcupiedBy(null);
						break;
					case 62:
						getCellByIndex(61).setIsOcupied(true);
						getCellByIndex(61).setOcupiedBy( getCellByIndex(63).getOcupiedBy() ); //swap the rook
						getCellByIndex(63).setIsOcupied(false);
						getCellByIndex(63).setOcupiedBy(null);
						break;
				}
			}
		}
		
		if (getCellByIndex(cellIndexTo).getIsOcupied()) {
			getCellByIndex(cellIndexTo).getOcupiedBy().setIsDead(true);
		}
		
		getCellByIndex(cellIndexTo).setIsOcupied(true);
		getCellByIndex(cellIndexTo).setOcupiedBy(piece);
		getCellByIndex(cellIndexFrom).setIsOcupied(false);
		getCellByIndex(cellIndexFrom).setOcupiedBy(null);
		
		checkForCheck(); //checking for check to a king
	}
	
	//return the Cell object from its int-Index
	private Cell getCellByIndex(int index) {
		return this.board.getBoardStateCell(index);
	}
	
	
	private void checkForCheck() {
		int cellWhiteKingIndex = this.board.getPieceByIndex(15).getCellIndex();
		int cellBlackKingIndex = this.board.getPieceByIndex(31).getCellIndex();
		Cell cellWhiteKing;
		Cell cellBlackKing;
		
		boolean[] possibleMoves = new boolean[64];
		
		Board virtualBoard = (Board) this.board.clone();
		cellWhiteKing = virtualBoard.getBoardStateCell( cellWhiteKingIndex );
		cellBlackKing = virtualBoard.getBoardStateCell( cellBlackKingIndex );
		
		Piece virtualPawnBlack = new Piece(PieceType.PAWN);
		virtualPawnBlack.setIsWhite(false);
		Piece virtualPawnWhite = new Piece(PieceType.PAWN);
		virtualPawnWhite.setIsWhite(true);
		
		cellWhiteKing.setOcupiedBy(virtualPawnWhite);
		cellBlackKing.setOcupiedBy(virtualPawnBlack);
		
		for (int i = 0; i < 16; i++) {
			Piece piece = virtualBoard.getPieceByIndex(i);
			if (!piece.getIsDead()) {
				int cellIndex = piece.getCellIndex();
				possibleMoves = virtualBoard.getPossibleMoves(cellIndex, true);
				if (possibleMoves[cellBlackKingIndex] == true) {
					this.board.setCheck(false); //sets true for whiteChecked, if pieceChecked is white
					this.board.setCheckedBy(piece); //sets board's field to know, which piece has made check
					break;
				} 
				else {
					this.board.setUncheck(false); //sets false for whiteChecked, if pieceChecked is white
					this.board.setCheckedBy(null); //sets board's field to know, which piece has made check
				}
			}
		}
		
		for (int i = 16; i < 32; i++) {
			Piece piece = virtualBoard.getPieceByIndex(i);
			if (!piece.getIsDead()) {
				int cellIndex = piece.getCellIndex();
				possibleMoves = virtualBoard.getPossibleMoves(cellIndex, false);
				if (possibleMoves[cellWhiteKingIndex] == true) {
					this.board.setCheck(true); //sets true for whiteChecked, if pieceChecked is white
					this.board.setCheckedBy(piece); //sets board's field to know, which piece has made check
					break;
				}
				else {
					this.board.setUncheck(true); //sets false for whiteChecked, if pieceChecked is white
					this.board.setCheckedBy(null); //sets board's field to know, which piece has made check
				}				
			}
		}
		
	}
	
	private boolean checkForAbas() {
		boolean answer = false;
		//FOR WHITES
		if (this.board.getWhiteChecked()) {
			//Find out if the king has any moves
			Piece whiteKing = this.board.getPieceByIndex(15);
			boolean[] wkPossibleMoves = this.board.getPossibleMoves(whiteKing.getCellIndex(), true);
			boolean hasNoMoves = true;
			for (int i = 0; i < 64; i++) {
				if (wkPossibleMoves[i] == true) {
					hasNoMoves = false;
					break;
				}
			}
			//if he has no moves
			if (hasNoMoves) {
				//Check if the piece checking the king can be beaten
				//Afterwards check if the king can cover behind another piece
			}
		}
		
		//FOR BLACKS
		if (this.board.getBlackChecked()) {
			
		}
		return answer;
	}
	
	//test
	public String getTestText() {
		return this.testText;
	}
	
	public boolean[] getPossibleMoves(int cellIndex, boolean playerIsWhite) {
		return this.board.getPossibleMoves(cellIndex, playerIsWhite);
	}
	
	//ZZZZZZZZZZZZZZ| AI WORK METHODS |ZZZZZZZZZZZZZ//
	public void getAiMove(boolean aiIsWhite){
		this.ai.performRandomMove(aiIsWhite);
		int cellIndexFrom = this.ai.getCellIndexFrom();
		int cellIndexTo = this.ai.getCellIndexTo();
		
		movePiece(cellIndexFrom, cellIndexTo);
	}
}