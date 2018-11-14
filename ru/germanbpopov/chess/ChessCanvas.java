package ru.germanbpopov.chess;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Map;
import java.util.HashMap;

class ChessCanvas extends JPanel implements MouseListener {
	
	private final double SCALE_X = 0.3333, SCALE_Y = 0.3333;
	
	private Presenter presenter;
	//private Graphics g = this.getGraphics();
	//private Graphics2D g2d = (Graphics2D)g;
	
	//ChessBoard state
	private Board currentBoardState;
	private boolean[] possibleMoves = new boolean[64]; //to check if move is legal
	private int cellIndexFrom; //to remember the previous selected cell
	private boolean playerIsWhite = true;
	private boolean playerTurn = true; // true = White's Turn; false = Black's turn
	//Images
	//Source images
	private BufferedImage src_ChessBoard = null;
	private BufferedImage src_WhiteKing = null;
	private BufferedImage src_WhiteQueen = null;
	private BufferedImage src_WhiteBishop = null;
	private BufferedImage src_WhiteKnight = null;
	private BufferedImage src_WhiteRook = null;
	private BufferedImage src_WhitePawn = null;
	private BufferedImage src_BlackKing = null;
	private BufferedImage src_BlackQueen = null;
	private BufferedImage src_BlackBishop = null;
	private BufferedImage src_BlackKnight = null;
	private BufferedImage src_BlackRook = null;
	private BufferedImage src_BlackPawn = null;
	private BufferedImage src_CellHighlight = null;
	private BufferedImage src_CellPossibleMove = null;
	
	private BufferedImage img_ChessBoard = null;
	private BufferedImage img_CellHighlight = null;
	private BufferedImage img_CellPossibleMove = null;
	//PieceTypes -> workImages link
	private Map<PieceType, BufferedImage> pieceWhiteImages = new HashMap<PieceType, BufferedImage>();
	private Map<PieceType, BufferedImage> pieceBlackImages = new HashMap<PieceType, BufferedImage>();
	//Flags
	private boolean cellSelectedFlag = false;
	
	
	//contructors
	public ChessCanvas(Presenter presenter) {
		//Border around the canvas
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
				
		this.presenter = presenter;
		addMouseListener(this);
		
		//Testing board;
		this.currentBoardState = new Board();
		this.currentBoardState.setInitialPiecesDisposition();
		
		//Importing images
		initializeImages();
		
		//link images with piecetypes
		pieceWhiteImages.put(PieceType.PAWN, this.src_WhitePawn);
		pieceWhiteImages.put(PieceType.ROOK, this.src_WhiteRook);
		pieceWhiteImages.put(PieceType.BISHOP, this.src_WhiteBishop);
		pieceWhiteImages.put(PieceType.KNIGHT, this.src_WhiteKnight);
		pieceWhiteImages.put(PieceType.QUEEN, this.src_WhiteQueen);
		pieceWhiteImages.put(PieceType.KING, this.src_WhiteKing);
		
		pieceBlackImages.put(PieceType.PAWN, this.src_BlackPawn);
		pieceBlackImages.put(PieceType.ROOK, this.src_BlackRook);
		pieceBlackImages.put(PieceType.BISHOP, this.src_BlackBishop);
		pieceBlackImages.put(PieceType.KNIGHT, this.src_BlackKnight);
		pieceBlackImages.put(PieceType.QUEEN, this.src_BlackQueen);
		pieceBlackImages.put(PieceType.KING, this.src_BlackKing);
	}
	
	//methods
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		//Draw (under construction)
		g2d.setColor(Color.WHITE);
		g2d.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
		
		
		
		//g.drawImage(chbdImage, 0, 0, this);
		double cnvHeight = this.getHeight();
		double chbdHeight = this.src_ChessBoard.getHeight();
		double scale = cnvHeight / chbdHeight;
		
		scaleAllImages(scale);
		drawBoard(g2d);
		
		
		//TEST PIECE -> IMAGE LINK
		//Piece pc_whiteKnight = new Piece(PieceType.KNIGHT, PieceID.WHITE_KNIGHT_0);
		
		//drawImage(g2d, getImageByPiece(pc_whiteKnight, true), 200, 200);
		//============================
		
		
		drawCurrentDisposition(g2d, scale);
		drawDead(g2d, scale);
		drawCheckMessage(g2d, scale);
		
		drawKeyPoints(g2d);
		// 
	}
	
	public void printMessage(String message) {
		Graphics g = this.getGraphics();
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.WHITE);
		g2d.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
		g2d.setColor(Color.BLACK);
		g2d.drawString(message, (int) (this.getWidth() / 2), (int) (this.getHeight() / 2));
	}
	
	public void drawImage(Graphics g, BufferedImage image, int x, int y) {
		Graphics2D g2d = (Graphics2D)g;
		try {
			g2d.drawImage(image, x, y, this);
		}
		catch(NullPointerException exc) {
			this.presenter.txaEcho("Could not draw image: " + exc.getMessage());
		}
	}
	
	public void drawBoard(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		try {
			g2d.drawImage(this.img_ChessBoard, (int) (this.getWidth() / 2 - this.img_ChessBoard.getWidth() / 2 ), (int) (this.getHeight() / 2 - this.img_ChessBoard.getHeight() / 2 ), this);
		}
		catch(NullPointerException exc) {
			this.presenter.txaEcho("Could not draw chessboard: " + exc.getMessage());
		}
	}
	
		
	//DRAW CURRENT DISPOSITION OF PIECES
	private void drawCurrentDisposition(Graphics g, double scale) {
		for (int i = 0; i < 64; i++) {
			if (this.currentBoardState.getBoardStateCell(i).getIsOcupied() ) {
				Piece pieceToDraw = this.currentBoardState.getBoardStateCell(i).getOcupiedBy();
				BufferedImage pieceImage = scaleImage(getImageByPiece(pieceToDraw, pieceToDraw.getIsWhite()), scale, scale);
				putImageIntoCell(g, pieceImage, transform(i));
			}
		}
	}
	
	private void drawDead(Graphics g, double scale) {
		int countWhite = 0;
		int countBlack = 0;
		int dX = (int) (scale * 300);
		int dY = (int) (scale * 150); 
		int initial_X = Math.round(this.getWidth() / 2 - this.img_ChessBoard.getWidth() / 2);
		int initial_Y = Math.round(this.getHeight() / 2 - this.img_ChessBoard.getHeight() / 2);
		int end_X = Math.round(this.getWidth() / 2 + this.img_ChessBoard.getWidth() / 2);
		int end_Y = Math.round(this.getHeight() / 2 + this.img_ChessBoard.getHeight() / 2);
		for (int i = 0; i < 32; i++) {
			Piece pieceToCheck = this.currentBoardState.getPieceByIndex(i);
			if (pieceToCheck.getIsDead()){
				BufferedImage img_Dead = scaleImage(getImageByPiece(pieceToCheck, pieceToCheck.getIsWhite()), scale / 2, scale / 2);
				if (pieceToCheck.getIsWhite()) {
					if (this.playerIsWhite) {
						drawImage(g, img_Dead, end_X + dX, dY + initial_Y + countWhite * dY);
					}
					else {
						drawImage(g, img_Dead, initial_X - dX, dY + initial_Y + countWhite * dY);
					}
					countWhite++;
				}
				else {
					if (this.playerIsWhite) {
						drawImage(g, img_Dead, initial_X - dX, dY + initial_Y + countBlack * dY);
					}
					else {
						drawImage(g, img_Dead, end_X + dX, dY + initial_Y + countBlack * dY);
					}
					countBlack++;
				}
			}
		}
	}
	
	private void drawPossibleMoves(Graphics g, int cellIndex) {
		getPossibleMoves(cellIndex, this.playerIsWhite);
		for (int i = 0; i < 64; i++) {
			if (this.possibleMoves[i]) {
				putImageIntoCell(g, img_CellPossibleMove, transform(i));
			}
		}
		
	}
	
	//draw Check
	private void drawCheckMessage(Graphics g, double scale) {
		g.setColor(Color.RED);
		if (this.currentBoardState.getWhiteChecked()) {
			g.drawString("White King Checked!", (int) (300 * scale), (int) (50 * scale) );
		}
		if (this.currentBoardState.getBlackChecked()) {
			g.drawString("Black King Checked!", (int) (300 * scale), (int) (50 * scale) );
		}
	}
	
	//MouseListener implementation
	@Override
	public void mouseClicked(MouseEvent e) {
		if ( (this.playerIsWhite && this.playerTurn)  || (!this.playerIsWhite && !this.playerTurn) ) { //check if it is your turn to play
			int initial_X = Math.round(this.getWidth() / 2 - this.img_ChessBoard.getWidth() / 2);
			int initial_Y = Math.round(this.getHeight() / 2 - this.img_ChessBoard.getHeight() / 2);
			int end_X = Math.round(this.getWidth() / 2 + this.img_ChessBoard.getWidth() / 2);
			int end_Y = Math.round(this.getHeight() / 2 + this.img_ChessBoard.getHeight() / 2);
			if (e.getX() > initial_X && e.getX() < end_X && e.getY() > initial_Y && e.getY() < end_Y) {
				int cellSelectedIndex = transform(getCellByCoordinates(e.getX(), e.getY()));
				if (this.cellSelectedFlag) {
					if (this.possibleMoves[cellSelectedIndex]) {
						this.presenter.movePiece(cellIndexFrom, cellSelectedIndex);
						this.cellSelectedFlag = false;
						this.playerTurn = !this.playerTurn; //set turn to another player
						this.presenter.getAiMove(!this.playerIsWhite); //call ai for its turn
					}
					else {
						this.presenter.txaEcho("Illegal move.");
						this.repaint();
						this.cellSelectedFlag = false;
					}
				}
				else {
					putImageIntoCell(this.getGraphics(), img_CellHighlight, transform(cellSelectedIndex));
					this.cellSelectedFlag = true;
					this.cellIndexFrom = cellSelectedIndex;
					Cell cellSelected = this.currentBoardState.getBoardStateCell(cellSelectedIndex);
					drawPossibleMoves(this.getGraphics(), cellSelectedIndex);
				}
				
			}
			else {
				this.cellSelectedFlag = false;
				this.repaint();
				this.revalidate();
				this.presenter.txaEcho("Mouse out of bounds");
			}
		}
		else {
			this.presenter.txaEcho("Not your turn");
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	//WORK WITH IMAGES
	public BufferedImage importImage(String path) {
		BufferedImage image = null;
		try {
			File file = new File(path); 
			FileInputStream fis = new FileInputStream(file);  
			image = ImageIO.read(fis);
		}
		catch(IOException exc) {
			this.presenter.txaEcho("Could not load image: " + path + exc.getMessage());
		}
		return image;
	}
	
	//scaleImage //OPTIMIZATION POSSIBLE???
	private BufferedImage scaleImage(BufferedImage image, double scaleX, double scaleY) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scaleX, scaleY);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		after = scaleOp.filter(image, null);
		return after;
	}
	
	private void scaleAllImages(double scale) {
		
		this.img_ChessBoard = scaleImage(this.src_ChessBoard, scale, scale);
		this.img_CellHighlight = scaleImage(this.src_CellHighlight, scale, scale);
		this.img_CellPossibleMove = scaleImage(this.src_CellPossibleMove, scale, scale);
				
	}
	
	//initialize all images
	private void initializeImages() {
		//set file separator symbol correctly
		String f_sep = System.getProperty("file.separator");
		String path_to_img = "ru" + f_sep + "germanbpopov" + f_sep + "chess" + f_sep + "img" + f_sep;
		//Load source images
		this.src_ChessBoard = importImage(path_to_img + "chessboard_large.png");
		this.src_WhiteKing = importImage(path_to_img + "white_king.png");
		this.src_WhiteQueen = importImage(path_to_img + "white_queen.png");
		this.src_WhiteBishop = importImage(path_to_img + "white_bishop.png");
		this.src_WhiteKnight = importImage(path_to_img + "white_knight.png");
		this.src_WhiteRook = importImage(path_to_img + "white_rook.png");
		this.src_WhitePawn = importImage(path_to_img + "white_pawn.png");
		this.src_BlackKing = importImage(path_to_img + "black_king.png");
		this.src_BlackQueen = importImage(path_to_img + "black_queen.png");
		this.src_BlackBishop = importImage(path_to_img + "black_bishop.png");
		this.src_BlackKnight = importImage(path_to_img + "black_knight.png");
		this.src_BlackRook = importImage(path_to_img + "black_rook.png");
		this.src_BlackPawn = importImage(path_to_img + "black_pawn.png");
		this.src_CellHighlight = importImage(path_to_img + "cell_highlight.png");
		this.src_CellPossibleMove = importImage(path_to_img + "cell_possibleMove.png");
		
		//initialaize work images
		this.img_ChessBoard = this.src_ChessBoard;
		
	}
	
	
	
	public void putImageIntoCell(Graphics g, BufferedImage image, int cellNumber) {
		int indexX = cellNumber % 8;
		int indexY = (int)(cellNumber / 8);
		int initial_X = Math.round(this.getWidth() / 2 - this.img_ChessBoard.getWidth() / 2);
		int initial_Y = Math.round(this.getHeight() / 2 - this.img_ChessBoard.getHeight() / 2); ////////MEGA IMPORTANT: THE PLUS SIGN REVERTS THE CHESS BOARD AROUND
		int fullCell = Math.round( this.img_ChessBoard.getWidth() / 8 );
		
		int x = initial_X + fullCell * indexX;
		int y = initial_Y + fullCell * indexY;
		
		drawImage(g, image, x, y);
	}
	
	private int getCellByCoordinates(int x, int y) {
		int answer;
		
		int initial_X = Math.round(this.getWidth() / 2 - this.img_ChessBoard.getWidth() / 2);
		int initial_Y = Math.round(this.getHeight() / 2 - this.img_ChessBoard.getHeight() / 2);
		int end_X = Math.round(this.getWidth() / 2 + this.img_ChessBoard.getWidth() / 2);
		int end_Y = Math.round(this.getHeight() / 2 + this.img_ChessBoard.getHeight() / 2);
		
		double step = ( (double)( end_X - initial_X ) / 8 );
		int indexX = (int)( (x - initial_X) / step);
		int indexY = (int)(y / step);
		
		answer = indexY * 8 + indexX;
		return answer;
	}
	
	
		
	//check if mouse is on a board
	private void drawKeyPoints(Graphics g){
		int initial_X = Math.round(this.getWidth() / 2 - this.img_ChessBoard.getWidth() / 2);
		int initial_Y = Math.round(this.getHeight() / 2 - this.img_ChessBoard.getHeight() / 2);
		int end_X = Math.round(this.getWidth() / 2 + this.img_ChessBoard.getWidth() / 2);
		int end_Y = Math.round(this.getHeight() / 2 + this.img_ChessBoard.getHeight() / 2);
		
		g.setColor(Color.RED);
		g.fillOval(initial_X, initial_Y, 5, 5);
		g.fillOval(initial_X, end_Y, 5, 5);
		g.fillOval(end_X, initial_Y, 5, 5);
		g.fillOval(end_X, end_Y, 5, 5);
	}
	
	//setters
	public void setCurrentBoardState(Board boardState) {
		this.currentBoardState = boardState;
		this.repaint();
	}
	
	public void setPlayerColor(boolean isWhite) {
		this.playerIsWhite = isWhite;
		this.repaint();
	}
	
	public void setPlayerTurn(boolean turn) {
		this.playerTurn = turn;
	}
	
	//getters
	private Board getCurrentBoardState() {
		return this.currentBoardState;
	}
	
	private BufferedImage getImageByPiece(Piece piece, boolean isWhite) {
		if (isWhite) {
			return this.pieceWhiteImages.get(piece.getPieceType());
		}
		else {
			return this.pieceBlackImages.get(piece.getPieceType());
		}
	}
	
	private void getPossibleMoves(int cellIndex, boolean playerIsWhite) {
		this.possibleMoves = this.presenter.getPossibleMoves(cellIndex, playerIsWhite);
	}
		
	//TRANSFORM INDEX OF A CELL INTO INDEX OF A CELL FOR VIEW
	private int transform(int cellIndex) {
		int indexX = cellIndex % 8;
		int indexY = (int) (cellIndex / 8);
		int tr = 0;
		if (this.playerIsWhite) {
			tr = 8 * (7 - indexY) + indexX;
		}
		else {
			tr = 8 * indexY + (7 - indexX);
		}
		return tr;
	}
}