package ru.germanbpopov.chess;

import java.util.Random;

/**
* <p>Class for describing a chess board.</p>
* <p>This class includes fields and methods used for describing the chessboard. 
* Chessboard is represented as private array of Cells, each under its unique number. 
* Pieces on board, as well as beaten pieces, are contained in another private array. Index of a piece is a unique ID, used to identify it in game.
* Pieces under indices 0-7 and 16-23 are pawns (white and black respective); 8-9 and 24-25 - knights; 10-11 and 26-27 - bishops; 12-13 and 28-29 - rooks; 14 and 30 - queen; 15 and 31 - king.
* The Board class also contains methods for finding moves available for each piece, setting the piece status (dead / alive), other manipulations with a chessboard.</p>
*/

public class Board implements Cloneable {
	
	//Piece value CONSTANTS
	private final int VALUE_PAWN = 100;
	private final int VALUE_KNIGHT = 300;
	private final int VALUE_BISHOP = 300;
	private final int VALUE_ROOK = 500;
	private final int VALUE_QUEEN = 900;
	
	
	//randomizer
	private Random rnd = new Random();
	
	private Cell[] boardState = new Cell[64];
	private Piece[] pieces = new Piece[32];
	
	private boolean whiteChecked = false;
	private boolean blackChecked = false;
	Piece checkedBy = null;
	
	public Board() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				boardState[i * 8 + j] = new Cell(j, i);
			}
		}
		
		//Initialize pieces
		for (int i = 0; i < this.pieces.length; i++) {
			if (i < 8) {
				pieces[i] = new Piece(PieceType.PAWN);
				pieces[i].setIsWhite(true);
			}
			if (i >= 8 && i < 10) {
				pieces[i] = new Piece(PieceType.KNIGHT);
				pieces[i].setIsWhite(true);
			}
			if (i >= 10 && i < 12) {
				pieces[i] = new Piece(PieceType.BISHOP);
				pieces[i].setIsWhite(true);
			}
			if (i >= 12 && i < 14) {
				pieces[i] = new Piece(PieceType.ROOK);
				pieces[i].setIsWhite(true);
			}
			if (i == 14) {
				pieces[i] = new Piece(PieceType.QUEEN);
				pieces[i].setIsWhite(true);
			}
			if (i == 15) {
				pieces[i] = new Piece(PieceType.KING);
				pieces[i].setIsWhite(true);
			}
			if (i >= 16 && i < 24) {
				pieces[i] = new Piece(PieceType.PAWN);
				pieces[i].setIsWhite(false);
			}
			if (i >= 24 && i < 26) {
				pieces[i] = new Piece(PieceType.KNIGHT);
				pieces[i].setIsWhite(false);
			}
			if (i >= 26 && i < 28) {
				pieces[i] = new Piece(PieceType.BISHOP);
				pieces[i].setIsWhite(false);
			}
			if (i >= 28 && i < 30) {
				pieces[i] = new Piece(PieceType.ROOK);
				pieces[i].setIsWhite(false);
			}
			if (i == 30) {
				pieces[i] = new Piece(PieceType.QUEEN);
				pieces[i].setIsWhite(false);
			}
			if (i == 31) {
				pieces[i] = new Piece(PieceType.KING);
				pieces[i].setIsWhite(false);
			}
		}
		
		//Set piece values
		setPieceValues();
	}
	
	//getters
	public Cell[] getBoardState(){
		return this.boardState;
	}
	
	public Cell getBoardStateCell(int i){
		return this.boardState[i];
	}
	
	public Piece[] getPieces() {
		return this.pieces;
	}
	
	public Piece getPieceByIndex(int index) {
		return this.pieces[index];
	}
	
	public boolean getWhiteChecked() {
		return this.whiteChecked;
	}
	
	public boolean getBlackChecked() {
		return this.blackChecked;
	}
	
	public Piece getCheckedBy() {
		return this.checkedBy;
	}
	
	public int getBoardValue(boolean isWhite) {
		int boardValue = 0;
		//piecesEvaluation();
		if (isWhite) {
			for (int i = 0; i < 16; i++) {
				if (!pieces[i].getIsDead()) {
					boardValue = boardValue + pieces[i].getValue();
				}
			}
		}
		else {
			for (int i = 16; i < 32; i++) {
				if (!pieces[i].getIsDead()) {
					boardValue = boardValue + pieces[i].getValue();
				}
			}
		}
		return boardValue;
	}
	//setters
		//!!!!!!!! SET INITIAL BOARD STATE !!!!!!!
	public void setInitialPiecesDisposition() {
		clear();
		
		for (int i = 0; i < 16; i++) {
			this.boardState[i].setIsOcupied(true);
		}
		for (int i = 48; i < 64; i++) {
			this.boardState[i].setIsOcupied(true);
		}
		
		this.boardState[0].setOcupiedBy(this.pieces[12]);
		this.boardState[1].setOcupiedBy(this.pieces[8]);
		this.boardState[2].setOcupiedBy(this.pieces[10]);
		this.boardState[3].setOcupiedBy(this.pieces[14]);
		this.boardState[4].setOcupiedBy(this.pieces[15]);
		this.boardState[5].setOcupiedBy(this.pieces[11]);
		this.boardState[6].setOcupiedBy(this.pieces[9]);
		this.boardState[7].setOcupiedBy(this.pieces[13]);
		this.boardState[8].setOcupiedBy(this.pieces[0]);
		this.boardState[9].setOcupiedBy(this.pieces[1]);
		this.boardState[10].setOcupiedBy(this.pieces[2]);
		this.boardState[11].setOcupiedBy(this.pieces[3]);
		this.boardState[12].setOcupiedBy(this.pieces[4]);
		this.boardState[13].setOcupiedBy(this.pieces[5]);
		this.boardState[14].setOcupiedBy(this.pieces[6]);
		this.boardState[15].setOcupiedBy(this.pieces[7]);
		
		this.boardState[48].setOcupiedBy(this.pieces[16]);
		this.boardState[49].setOcupiedBy(this.pieces[17]);
		this.boardState[50].setOcupiedBy(this.pieces[18]);
		this.boardState[51].setOcupiedBy(this.pieces[19]);
		this.boardState[52].setOcupiedBy(this.pieces[20]);
		this.boardState[53].setOcupiedBy(this.pieces[21]);
		this.boardState[54].setOcupiedBy(this.pieces[22]);
		this.boardState[55].setOcupiedBy(this.pieces[23]);
		this.boardState[56].setOcupiedBy(this.pieces[28]);
		this.boardState[57].setOcupiedBy(this.pieces[24]);
		this.boardState[58].setOcupiedBy(this.pieces[26]);
		this.boardState[59].setOcupiedBy(this.pieces[30]);
		this.boardState[60].setOcupiedBy(this.pieces[31]);
		this.boardState[61].setOcupiedBy(this.pieces[27]);
		this.boardState[62].setOcupiedBy(this.pieces[25]);
		this.boardState[63].setOcupiedBy(this.pieces[29]);
		
	}
	
	public void setRandomPiecesDisposition() {
		clear();
		
		boolean[] mask = new boolean[64];
		for (boolean i : mask) {
			i = false;
		}
		int randomCell = this.rnd.nextInt(64);
		mask[randomCell] = true;
		for (int i = 0; i < 32; i++) {
			while(mask[randomCell]) {
				randomCell = this.rnd.nextInt(64);
			}
			mask[randomCell] = true;
			//ocupy random cell
			this.boardState[randomCell].setIsOcupied(true);
			this.boardState[randomCell].setOcupiedBy(this.pieces[i]);
			//this.pieces[i].setCurrentPosition(this.boardState[randomCell]);
		}
	}
	
	public void setCheck(boolean isWhite) {
		if (isWhite) this.whiteChecked = true;
		else this.blackChecked = true;
	}
	
	public void setUncheck(boolean isWhite) {
		if (isWhite) this.whiteChecked = false;
		else this.blackChecked = false;
	}
	
	public void setCheckedBy(Piece piece) {
		this.checkedBy = piece;
	}
	
	
	//private
	private void clear() {
		for (Cell c : boardState) {
			c.setIsOcupied(false);
			c.setOcupiedBy(null);
		}
	}
	
	private void setPieceValues() {
		for (int i = 0; i < pieces.length; i++) {
			switch (pieces[i].getPieceType()) {
				case PAWN:
					pieces[i].setValue( VALUE_PAWN );
					break;
				case KNIGHT:
					pieces[i].setValue( VALUE_KNIGHT );
					break;
				case BISHOP:
					pieces[i].setValue( VALUE_BISHOP );
					break;
				case ROOK:
					pieces[i].setValue( VALUE_ROOK );
					break;
				case QUEEN:
					pieces[i].setValue( VALUE_QUEEN );
					break;
			}
		}
	}
	
	private int piecesEvaluation(boolean isWhite) {
		int piecesEval = 0;
		if (isWhite) {
			for (int i = 0; i < 16; i++) {
				if (!pieces[i].getIsDead()) {
					switch (pieces[i].getPieceType()) {
						case PAWN:
							piecesEval += pieces[i].getValue();
							break;
						case KNIGHT:
							pieces[i].setValue( VALUE_KNIGHT );
							break;
						case BISHOP:
							pieces[i].setValue( VALUE_BISHOP );
							break;
						case ROOK:
							pieces[i].setValue( VALUE_ROOK );
							break;
						case QUEEN:
							pieces[i].setValue( VALUE_QUEEN );
							break;
					}
				}
			}
		}
		else {
			for (int i = 16; i < 32; i++) {
				if (!pieces[i].getIsDead()) {
					switch (pieces[i].getPieceType()) {
						case PAWN:
							pieces[i].setValue( VALUE_PAWN );
							break;
						case KNIGHT:
							pieces[i].setValue( VALUE_KNIGHT );
							break;
						case BISHOP:
							pieces[i].setValue( VALUE_BISHOP );
							break;
						case ROOK:
							pieces[i].setValue( VALUE_ROOK );
							break;
						case QUEEN:
							pieces[i].setValue( VALUE_QUEEN );
							break;
					}
				}
			}
		}
		return piecesEval;
	}
	
	
	@Override
	public Board clone() {   //DEEP CLONING
		Board clone = null;
		try {
			clone = (Board) super.clone();
			clone.boardState = new Cell[64];
			//filling in new Pieces
			clone.pieces = new Piece[32];
			for (int i = 0; i < 32; i++) {
				clone.pieces[i] = new Piece(this.pieces[i].getPieceType());
				
				//Dead?
				if (this.pieces[i].getIsDead()) clone.pieces[i].setIsDead(true);
				else clone.pieces[i].setIsDead(false);
				//White?
				if (this.pieces[i].getIsWhite()) clone.pieces[i].setIsWhite(true);
				else clone.pieces[i].setIsWhite(false);
				//Made first move?
				if (this.pieces[i].getMadeFirstMove()) clone.pieces[i].setMadeFirstMove(true);
				else clone.pieces[i].setMadeFirstMove(false);
				//value
				clone.pieces[i].setValue(this.pieces[i].getValue());
				clone.pieces[i].setCellIndex( this.pieces[i].getCellIndex() );
			}
			
			
			//filling in new Cells
			for (int i = 0; i < 64; i++) {
				clone.boardState[i] = new Cell(this.boardState[i].getX(), this.boardState[i].getY());
				if (this.boardState[i].getIsOcupied()) {
					clone.boardState[i].setIsOcupied(true);
				}
				else {
					clone.boardState[i].setIsOcupied(false);
				}
			}
			
			for (int i = 0; i < 32; i++) {
				if (!clone.pieces[i].getIsDead()) {
					int index = clone.pieces[i].getCellIndex();
					clone.boardState[index].setOcupiedBy(clone.pieces[i]);
				}
			}
			
		}
		catch (CloneNotSupportedException exc) {
			System.out.println("Error while cloning board: " + exc.getMessage());
		}
		return clone;
	}
	
	
	
	
	
	//POSSSIBLE MOOOOOOOOOOOOOOOOOOOVES
	public boolean[] getPossibleMoves(int cellIndex, boolean playerIsWhite) {
		boolean[] possibleMoves = new boolean[64];
		for (boolean i : possibleMoves) { i = false; }
		if (this.boardState[cellIndex].getIsOcupied()) {
			int indexX = cellIndex % 8;
			int indexY = (int) (cellIndex / 8);
			int step = 1;
			Piece piece = this.boardState[cellIndex].getOcupiedBy(); //piece in question
			if ((piece.getIsWhite() == true && playerIsWhite == true) || (piece.getIsWhite() == false && playerIsWhite == false)) { //check if player and piece under question are of the same color
				switch (piece.getPieceType()) {
					case PAWN:
						if (piece.getIsWhite()) { //WHITE PAWNS
							if (checkOutOfBounds(indexX, indexY + 1)) {
								possibleMoves[xyToCellIndex(indexX, indexY + 1)] = checkPossibleMove(indexX, indexY + 1, piece);
								if (!piece.getMadeFirstMove()) {
									if (checkPossibleMove(indexX, indexY + 1, piece) && checkPossibleMove(indexX, indexY + 2, piece)) {
										possibleMoves[xyToCellIndex(indexX, indexY + 2)] = true;
									}
								}
							}
							//check available to beat
							if (checkOutOfBounds(indexX + 1, indexY + 1)) {
								if (!checkEnemyPieceMet(indexX + 1, indexY + 1, piece)) {
									if (this.boardState[xyToCellIndex(indexX + 1, indexY + 1)].getOcupiedBy().getPieceType() != PieceType.KING) {
										possibleMoves[xyToCellIndex(indexX + 1, indexY + 1)] = true;
									}
								}
							}
							if (checkOutOfBounds(indexX - 1, indexY + 1)) {
								if (!checkEnemyPieceMet(indexX - 1, indexY + 1, piece)) {
									if (this.boardState[xyToCellIndex(indexX - 1, indexY + 1)].getOcupiedBy().getPieceType() != PieceType.KING) {
										possibleMoves[xyToCellIndex(indexX - 1, indexY + 1)] = true;
									}
								}
							}
						}
						else { //BLACK PAWNS
							if (checkOutOfBounds(indexX, indexY - 1)) {
								possibleMoves[xyToCellIndex(indexX, indexY - 1)] = checkPossibleMove(indexX, indexY - 1, piece);
								if (!piece.getMadeFirstMove()) {
									if (checkPossibleMove(indexX, indexY - 1, piece) && checkPossibleMove(indexX, indexY - 2, piece)) {
										possibleMoves[xyToCellIndex(indexX, indexY - 2)] = true;
									}
								}
							}
							//check available to beat
							if (checkOutOfBounds(indexX + 1, indexY - 1)) {
								if (!checkEnemyPieceMet(indexX + 1, indexY - 1, piece)) {
									if (this.boardState[xyToCellIndex(indexX + 1, indexY - 1)].getOcupiedBy().getPieceType() != PieceType.KING) {
										possibleMoves[xyToCellIndex(indexX + 1, indexY - 1)] = true;
									}
								}
							}
							if (checkOutOfBounds(indexX - 1, indexY - 1)) {
								if (!checkEnemyPieceMet(indexX - 1, indexY - 1, piece)) {
									if (this.boardState[xyToCellIndex(indexX - 1, indexY - 1)].getOcupiedBy().getPieceType() != PieceType.KING) {
										possibleMoves[xyToCellIndex(indexX - 1, indexY - 1)] = true;
									}
								}
							}
						}
						break;
					case KNIGHT:
						if (checkOutOfBounds(indexX + 1, indexY + 2)) {
							possibleMoves[xyToCellIndex(indexX + 1, indexY + 2)] = checkPossibleMove(indexX + 1, indexY + 2, piece);
						}
						if (checkOutOfBounds(indexX - 1, indexY + 2)) {
							possibleMoves[xyToCellIndex(indexX - 1, indexY + 2)] = checkPossibleMove(indexX - 1, indexY + 2, piece);
						}
						if (checkOutOfBounds(indexX + 2, indexY + 1)) {
							possibleMoves[xyToCellIndex(indexX + 2, indexY + 1)] = checkPossibleMove(indexX + 2, indexY + 1, piece);
						}
						if (checkOutOfBounds(indexX - 2, indexY + 1)) {
							possibleMoves[xyToCellIndex(indexX - 2, indexY + 1)] = checkPossibleMove(indexX - 2, indexY + 1, piece);
						}
						if (checkOutOfBounds(indexX + 1, indexY - 2)) {
							possibleMoves[xyToCellIndex(indexX + 1, indexY - 2)] = checkPossibleMove(indexX + 1, indexY - 2, piece);
						}
						if (checkOutOfBounds(indexX - 1, indexY - 2)) {
							possibleMoves[xyToCellIndex(indexX - 1, indexY - 2)] = checkPossibleMove(indexX - 1, indexY - 2, piece);
						}
						if (checkOutOfBounds(indexX + 2, indexY - 1)) {
							possibleMoves[xyToCellIndex(indexX + 2, indexY - 1)] = checkPossibleMove(indexX + 2, indexY - 1, piece);
						}
						if (checkOutOfBounds(indexX - 2, indexY - 1)) {
							possibleMoves[xyToCellIndex(indexX - 2, indexY - 1)] = checkPossibleMove(indexX - 2, indexY - 1, piece);
						}
						break;
					case BISHOP:
						step = 1;
						while (checkOutOfBounds(indexX + step, indexY + step) && checkPossibleMove(indexX + step, indexY + step, piece) && checkEnemyPieceMet(indexX + step - 1, indexY + step - 1, piece)) {
							possibleMoves[xyToCellIndex(indexX + step, indexY + step)] = true;
							step++;
						}
						step = 1;
						while (checkOutOfBounds(indexX - step, indexY - step) && checkPossibleMove(indexX - step, indexY - step, piece) && checkEnemyPieceMet(indexX - step + 1, indexY - step + 1, piece)) {
							possibleMoves[xyToCellIndex(indexX - step, indexY - step)] = true;
							step++;
						}
						step = 1;
						while (checkOutOfBounds(indexX + step, indexY - step) && checkPossibleMove(indexX + step, indexY - step, piece) && checkEnemyPieceMet(indexX + step - 1, indexY - step + 1, piece)) {
							possibleMoves[xyToCellIndex(indexX + step, indexY - step)] = true;
							step++;
						}
						step = 1;
						while (checkOutOfBounds(indexX - step, indexY + step) && checkPossibleMove(indexX - step, indexY + step, piece) && checkEnemyPieceMet(indexX - step + 1, indexY + step - 1, piece)) {
							possibleMoves[xyToCellIndex(indexX - step, indexY + step)] = true;
							step++;
						}
						break;
					case ROOK:
						step = 1;
						//left
						
						while (checkOutOfBounds(indexX - step, indexY) && checkPossibleMove(indexX - step, indexY, piece) && checkEnemyPieceMet(indexX - step + 1, indexY, piece)) {
							possibleMoves[xyToCellIndex(indexX - step, indexY)] = true;
							step++;
						}
						//right
						step = 1;
						while (checkOutOfBounds(indexX + step, indexY) && checkPossibleMove(indexX + step, indexY, piece) && checkEnemyPieceMet(indexX + step - 1, indexY, piece)) {
							possibleMoves[xyToCellIndex(indexX + step, indexY)] = true;
							step++;
						}
						//up
						step = 1;
						while (checkOutOfBounds(indexX, indexY + step) && checkPossibleMove(indexX, indexY + step, piece) && checkEnemyPieceMet(indexX, indexY + step - 1, piece)) {
							possibleMoves[xyToCellIndex(indexX, indexY + step)] = true;
							step++;
						}
						//down
						step = 1;
						while (checkOutOfBounds(indexX, indexY - step) && checkPossibleMove(indexX, indexY - step, piece) && checkEnemyPieceMet(indexX, indexY - step + 1, piece)) {
							possibleMoves[xyToCellIndex(indexX, indexY - step)] = true;
							step++;
						}
						break;
					case QUEEN:
						step = 1;
						//diagonals
						while (checkOutOfBounds(indexX + step, indexY + step) && checkPossibleMove(indexX + step, indexY + step, piece) && checkEnemyPieceMet(indexX + step - 1, indexY + step - 1, piece)) {
							possibleMoves[xyToCellIndex(indexX + step, indexY + step)] = true;
							step++;
						}
						step = 1;
						while (checkOutOfBounds(indexX - step, indexY - step) && checkPossibleMove(indexX - step, indexY - step, piece) && checkEnemyPieceMet(indexX - step + 1, indexY - step + 1, piece)) {
							possibleMoves[xyToCellIndex(indexX - step, indexY - step)] = true;
							step++;
						}
						step = 1;
						while (checkOutOfBounds(indexX + step, indexY - step) && checkPossibleMove(indexX + step, indexY - step, piece) && checkEnemyPieceMet(indexX + step - 1, indexY - step + 1, piece)) {
							possibleMoves[xyToCellIndex(indexX + step, indexY - step)] = true;
							step++;
						}
						step = 1;
						while (checkOutOfBounds(indexX - step, indexY + step) && checkPossibleMove(indexX - step, indexY + step, piece) && checkEnemyPieceMet(indexX - step + 1, indexY + step - 1, piece)) {
							possibleMoves[xyToCellIndex(indexX - step, indexY + step)] = true;
							step++;
						}
						//straight lines
						//left
						step = 1;
						while (checkOutOfBounds(indexX - step, indexY) && checkPossibleMove(indexX - step, indexY, piece) && checkEnemyPieceMet(indexX - step + 1, indexY, piece)) {
							possibleMoves[xyToCellIndex(indexX - step, indexY)] = true;
							step++;
						}
						//right
						step = 1;
						while (checkOutOfBounds(indexX + step, indexY) && checkPossibleMove(indexX + step, indexY, piece) && checkEnemyPieceMet(indexX + step - 1, indexY, piece)) {
							possibleMoves[xyToCellIndex(indexX + step, indexY)] = true;
							step++;
						}
						//up
						step = 1;
						while (checkOutOfBounds(indexX, indexY + step) && checkPossibleMove(indexX, indexY + step, piece) && checkEnemyPieceMet(indexX, indexY + step - 1, piece)) {
							possibleMoves[xyToCellIndex(indexX, indexY + step)] = true;
							step++;
						}
						//down
						step = 1;
						while (checkOutOfBounds(indexX, indexY - step) && checkPossibleMove(indexX, indexY - step, piece) && checkEnemyPieceMet(indexX, indexY - step + 1, piece)) {
							possibleMoves[xyToCellIndex(indexX, indexY - step)] = true;
							step++;
						}
						break;
					case KING:
						if (checkOutOfBounds(indexX - 1, indexY)) {
							possibleMoves[xyToCellIndex(indexX - 1, indexY)] = checkPossibleMove(indexX - 1, indexY, piece);
						}
						if (checkOutOfBounds(indexX + 1, indexY)) {
							possibleMoves[xyToCellIndex(indexX + 1, indexY)] = checkPossibleMove(indexX + 1, indexY, piece);
						}
						if (checkOutOfBounds(indexX - 1, indexY - 1)) {
							possibleMoves[xyToCellIndex(indexX - 1, indexY - 1)] = checkPossibleMove(indexX - 1, indexY - 1, piece);
						}
						if (checkOutOfBounds(indexX, indexY - 1)) {
							possibleMoves[xyToCellIndex(indexX, indexY - 1)] = checkPossibleMove(indexX, indexY - 1, piece);
						}
						if (checkOutOfBounds(indexX + 1, indexY - 1)) {
							possibleMoves[xyToCellIndex(indexX + 1, indexY - 1)] = checkPossibleMove(indexX + 1, indexY - 1, piece);
						}
						if (checkOutOfBounds(indexX - 1, indexY + 1)) {
							possibleMoves[xyToCellIndex(indexX - 1, indexY + 1)] = checkPossibleMove(indexX - 1, indexY + 1, piece);
						}
						if (checkOutOfBounds(indexX, indexY + 1)) {
							possibleMoves[xyToCellIndex(indexX, indexY + 1)] = checkPossibleMove(indexX, indexY + 1, piece);
						}
						if (checkOutOfBounds(indexX + 1, indexY + 1)) {
							possibleMoves[xyToCellIndex(indexX + 1, indexY + 1)] = checkPossibleMove(indexX + 1, indexY + 1, piece);
						}
						//Castling
						if (piece.getIsWhite()) {
							if (!piece.getMadeFirstMove()) {
								possibleMoves[ 2 ] = checkCastling( 2 );
								possibleMoves[ 6 ] = checkCastling( 6 );
							}
						}
						if (!piece.getIsWhite()) {
							if (!piece.getMadeFirstMove()) {
								possibleMoves[ 58 ] = checkCastling( 58 );
								possibleMoves[ 62 ] = checkCastling( 62 );
							}
						}
						break;
				}
			}
		}
		return possibleMoves;
	}
	
	private boolean checkCastling(int cellIndex) {
		boolean answer = false;
		if (cellIndex == 2) {
			if ( this.boardState[0].getIsOcupied() ) {
				Piece rook = this.boardState[0].getOcupiedBy();
				if (!rook.getMadeFirstMove() ) {
					if (!checkBlock(1, 0) && !checkBlock(2, 0) && !checkBlock(3, 0)) {
						answer = true;
					}
				}
			}
		}
		if (cellIndex == 6) {
			if ( this.boardState[7].getIsOcupied() ) {
				Piece rook = this.boardState[7].getOcupiedBy();
				if (!rook.getMadeFirstMove() ) {
					if (!checkBlock(5, 0) && !checkBlock(6, 0)) {
						answer = true;
					}
				}
			}
		}
		if (cellIndex == 62) {
			if ( this.boardState[63].getIsOcupied() ) {
				Piece rook = this.boardState[63].getOcupiedBy();
				if (!rook.getMadeFirstMove() ) {
					if (!checkBlock(6, 7) && !checkBlock(5, 7)) {
						answer = true;
					}
				}
			}
		}
		if (cellIndex == 58) {
			if ( this.boardState[56].getIsOcupied() ) {
				Piece rook = this.boardState[56].getOcupiedBy();
				if (!rook.getMadeFirstMove() ) {
					if (!checkBlock(1, 7) && !checkBlock(2, 7) && !checkBlock(3, 7)) {
						answer = true;
					}
				}
			}
		}
		return answer;
	}
	
	//Check out of board bounds
	private boolean checkOutOfBounds(int indexX, int indexY) {
		if (indexX < 0 || indexX > 7 || indexY < 0 || indexY > 7) return false;
		else return true;
	}
	
	//transform X-index and Y-index of the cell into its int-Index on board
	private int xyToCellIndex(int x, int y) {
		return (y * 8 + x);
	}
	
	//CHECK MOVE POSSIBILITY
	private boolean checkPossibleMove(int indexX, int indexY, Piece piece) {
		boolean answer = false;
		//If piece is pawn
		if (piece.getPieceType() == PieceType.PAWN) {
			if (!checkBlock(indexX, indexY)) {
				answer = true;
			}
		}
		else {
			
			if (checkBlock(indexX, indexY)) {
				Piece pieceOcupant = this.boardState[xyToCellIndex(indexX, indexY)].getOcupiedBy();
				if (!checkColorMatch(piece, pieceOcupant)) {
					if (pieceOcupant.getPieceType() != PieceType.KING) {
						answer = true;
					}
				}
			}
			else {
				answer = true;
			}
		}
		return answer;
	}
	
	private boolean checkBlock(int indexX, int indexY) {
		Cell cellToCheck = this.boardState[xyToCellIndex(indexX, indexY)];
		if (cellToCheck.getIsOcupied()) {
			return true;
		}
		else return false;
	}
	
	private boolean checkColorMatch(Piece pieceOne, Piece pieceTwo) {
		boolean answer = false;
		if (pieceOne.getIsWhite() && pieceTwo.getIsWhite()) answer = true;
		if (!pieceOne.getIsWhite() && !pieceTwo.getIsWhite()) answer = true;
		return answer;
	}
	
	/**
	* <p>Checks if enemy piece has been met on the path of a piece.
	* <p>For Bishops, Rooks, Queens... Checks if enemy piece was met on path. <i style="color: red;">This method is inversed!</i></p>
	* @param indexX The x-asis index of the cell checked
	* @param indexY The y-asis index of the cell checked
	* @param piece The piece, for which the checking is performed
	* @return true, if no pieces were met in the specified cell, false, if there is an enemy piece in the specified cell. <i style="color: red;">This method is inversed!</i>
	*/
	private boolean checkEnemyPieceMet(int indexX, int indexY, Piece piece) { //It is needed to input indexes of a previous checked cell. 
		boolean answer = true;
		if (checkBlock(indexX, indexY)) {
			Piece pieceOcupant = this.boardState[xyToCellIndex(indexX, indexY)].getOcupiedBy();
			if (!checkColorMatch(piece, pieceOcupant)) {
				answer = false;
			}
		}
		return answer;
	}
}

//OLD INITIATION WITH PIECE IDS
//Initialize board in fields
		//...PAWNS...
		/*
	private Piece wh_Pwn0 = new Piece(PieceType.PAWN, PieceID.WHITE_PAWN_0);
	private Piece wh_Pwn1 = new Piece(PieceType.PAWN, PieceID.WHITE_PAWN_1);
	private Piece wh_Pwn2 = new Piece(PieceType.PAWN, PieceID.WHITE_PAWN_2);
	private	Piece wh_Pwn3 = new Piece(PieceType.PAWN, PieceID.WHITE_PAWN_3);
	private	Piece wh_Pwn4 = new Piece(PieceType.PAWN, PieceID.WHITE_PAWN_4);
	private	Piece wh_Pwn5 = new Piece(PieceType.PAWN, PieceID.WHITE_PAWN_5);
	private	Piece wh_Pwn6 = new Piece(PieceType.PAWN, PieceID.WHITE_PAWN_6);
	private	Piece wh_Pwn7 = new Piece(PieceType.PAWN, PieceID.WHITE_PAWN_7);
	private	Piece bl_Pwn0 = new Piece(PieceType.PAWN, PieceID.BLACK_PAWN_0);
	private	Piece bl_Pwn1 = new Piece(PieceType.PAWN, PieceID.BLACK_PAWN_1);
	private	Piece bl_Pwn2 = new Piece(PieceType.PAWN, PieceID.BLACK_PAWN_2);
	private	Piece bl_Pwn3 = new Piece(PieceType.PAWN, PieceID.BLACK_PAWN_3);
	private	Piece bl_Pwn4 = new Piece(PieceType.PAWN, PieceID.BLACK_PAWN_4);
	private	Piece bl_Pwn5 = new Piece(PieceType.PAWN, PieceID.BLACK_PAWN_5);
	private	Piece bl_Pwn6 = new Piece(PieceType.PAWN, PieceID.BLACK_PAWN_6);
	private	Piece bl_Pwn7 = new Piece(PieceType.PAWN, PieceID.BLACK_PAWN_7);
		//....ROOKS.....
	private	Piece wh_Rook0 = new Piece(PieceType.ROOK, PieceID.WHITE_ROOK_0);
	private	Piece wh_Rook1 = new Piece(PieceType.ROOK, PieceID.WHITE_ROOK_1);
	private	Piece bl_Rook0 = new Piece(PieceType.ROOK, PieceID.BLACK_ROOK_0);
	private	Piece bl_Rook1 = new Piece(PieceType.ROOK, PieceID.BLACK_ROOK_1);
		//....KNIGHTS.....
	private	Piece wh_Knight0 = new Piece(PieceType.KNIGHT, PieceID.WHITE_KNIGHT_0);
	private	Piece wh_Knight1 = new Piece(PieceType.KNIGHT, PieceID.WHITE_KNIGHT_1);
	private	Piece bl_Knight0 = new Piece(PieceType.KNIGHT, PieceID.BLACK_KNIGHT_0);
	private	Piece bl_Knight1 = new Piece(PieceType.KNIGHT, PieceID.BLACK_KNIGHT_1);
		//....BISHOPS.....
	private	Piece wh_Bishop0 = new Piece(PieceType.BISHOP, PieceID.WHITE_BISHOP_0);
	private	Piece wh_Bishop1 = new Piece(PieceType.BISHOP, PieceID.WHITE_BISHOP_1);
	private	Piece bl_Bishop0 = new Piece(PieceType.BISHOP, PieceID.BLACK_BISHOP_0);
	private	Piece bl_Bishop1 = new Piece(PieceType.BISHOP, PieceID.BLACK_BISHOP_1);
		//....QUEENS.....
	private	Piece wh_Queen = new Piece(PieceType.QUEEN, PieceID.WHITE_QUEEN);
	private	Piece bl_Queen = new Piece(PieceType.QUEEN, PieceID.BLACK_QUEEN);
		//....KINGS.....
	private	Piece wh_King = new Piece(PieceType.KING, PieceID.WHITE_KING);
	private	Piece bl_King = new Piece(PieceType.KING, PieceID.BLACK_KING);
	*/