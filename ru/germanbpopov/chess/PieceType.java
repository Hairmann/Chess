package ru.germanbpopov.chess;

/**
* <p>Provides the necessary piece types for describing the pieces. </p>
* <p>This enum contains piece types, assigned to pieces upon their creation.
* Piece type defines the way in which <code>getPossibleMoves</code> method of <code>Board</code> class works.</p>
*/

public enum PieceType {
	/**
	* <p>The piece is a pawn.</p>
	*/
	PAWN,
	/**
	* <p>The piece is a knight.</p>
	*/
	KNIGHT,
	/**
	* <p>The piece is a bishop.</p>
	*/
	BISHOP,
	/**
	* <p>The piece is a rook.</p>
	*/
	ROOK,
	/**
	* <p>The piece is a queen.</p>
	*/
	QUEEN,
	/**
	* <p>The piece is a king.</p>
	*/
	KING
}