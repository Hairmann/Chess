package ru.germanbpopov.chess;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.text.Document;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dialog.ModalityType;
import java.awt.Color;

public class View {
	
	private Presenter presenter;
	private ChessCanvas chess_canvas;
	private JTextArea txa_log;
	private JFrame guiFrame;
	private JPanel guiPanel;
	private GridBagConstraints gbc = new GridBagConstraints();
	
	public View() {
		
	}
	
	
	//Methods
	public void createUI() {
		this.guiFrame = new JFrame("Gerborpop ChessMaster 0.0");
		this.guiFrame.setSize(800, 600);
		this.guiPanel = new JPanel();
		this.guiPanel.setSize(800, 600);
		//GRID BAG LAYOUT
		this.guiFrame.setLayout(new GridLayout(1, 1));
		this.guiPanel.setLayout(new GridBagLayout());
		gbc.insets = new Insets(5, 5, 5, 5);
		
		//Chess Canvas
		setGBC(gbc, 0, 0, 2, 1);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		setChessCanvas(new ChessCanvas(this.presenter));
		this.chess_canvas.setSize(800, 400);
		this.presenter.setInitialBoard();
		this.guiPanel.add(chess_canvas, gbc); 
		
		//Separator
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		JSeparator sep_topDown = new JSeparator(SwingConstants.HORIZONTAL);
		setGBC(gbc, 0, 1, 2, 1);
		this.guiPanel.add(sep_topDown, gbc);
		
		
		
		//Button New Game
		JButton btn_newGame = new JButton("New Game");
			//via lambda expression
		btn_newGame.addActionListener((ActionEvent e) -> {
			this.chess_canvas.printMessage("START A NEW GAME");
		});
		setGBC(gbc, 0, 2, 1, 1);
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		this.guiPanel.add(btn_newGame, gbc);
		
		
		//Button White 
		JButton btn_white = new JButton("as White");
			//via lambda expression
		btn_white.addActionListener((ActionEvent e) -> {
			this.presenter.setPlayerColor(true);
			this.presenter.setPlayerTurn(true);
		});
		setGBC(gbc, 0, 3, 1, 1);
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		this.guiPanel.add(btn_white, gbc);
		
		
		//Button Black
		JButton btn_black = new JButton("as Black");
			//via lambda expression
		btn_black.addActionListener((ActionEvent e) -> {
			this.presenter.setPlayerColor(false);
			this.presenter.setPlayerTurn(false);
			this.presenter.getAiMove(true);
		});
		setGBC(gbc, 0, 4, 1, 1);
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		this.guiPanel.add(btn_black, gbc);
		
		
		//Button Save
		JButton btn_save = new JButton("Randomize Disposition");
			//via lambda expression
		btn_save.addActionListener((ActionEvent e) -> {
			this.presenter.setRandomBoard();
		});
		setGBC(gbc, 0, 5, 1, 1);
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		this.guiPanel.add(btn_save, gbc);
		
		
		//Button Load
		JButton btn_load = new JButton("Initial Disposition");
			//via lambda expression
		btn_load.addActionListener((ActionEvent e) -> {
			this.presenter.setInitialBoard();
		});
		setGBC(gbc, 0, 6, 1, 1);
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		this.guiPanel.add(btn_load, gbc);
		
		
		//JLabel for txa_log
		JLabel lbl_log = new JLabel("Log:");
		setGBC(gbc, 1, 2, 1, 1);
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		this.guiPanel.add(lbl_log, gbc);
		
		
		//TextArea Log
		this.txa_log = new JTextArea();
		this.txa_log.setEditable(false);
			//document listener!!
		/*Ayayayayay
		//recursion: txaEcho fires DocListener, which calls txaEcho
		this.txa_log.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				
				try {
					//presenter.txaEcho(txa_log.getDocument().getText(0, 5));
				}
				catch (BadLocationException exc) {
					presenter.txaEcho("Couldnot perform echoing");
				}
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				
			}
		});
		*/
		
		/*
		this.txa_log.addActionListener((ActionEvent e) -> {
			this.presenter.txaEcho(this.txa_log.getText());
		});
		*/
		
		JScrollPane scr_log = new JScrollPane(this.txa_log);
		scr_log.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setGBC(gbc, 1, 3, 1, 4);
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.BOTH;
		this.guiPanel.add(scr_log, gbc);
			
			//draw chessBoard
		//this.chess_canvas.drawImage(this.chess_canvas.importImage("chessboard_background.jpg"));
		
		this.guiFrame.add(guiPanel);
		
		//setVisible
		stabilizeView();
		postToLog("\n\n\n\n> Welcome to \"Gerborpop's ChessMaster\" ");
		postToLog("version 0.0, of 30.08.2018.");
		postToLog("x size of canvas: " + this.chess_canvas.getWidth() + ", y size of canvas: " + this.chess_canvas.getHeight());
				
		this.guiFrame.setSize(800, 600);
		this.guiFrame.setLocationRelativeTo(null);
		
		JScrollBar scr_log_vert = scr_log.getVerticalScrollBar();
		scr_log_vert.setValue(scr_log_vert.getMaximum());
		
		this.guiFrame.setVisible(true);
		//CLOSING WINDOW
		this.guiFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.guiFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				closingDialog();
			}
		});
		
	}
	
	//Check exit DIALOG
	public void closingDialog(){
		GridBagConstraints gbc_dialog = new GridBagConstraints();
		JDialog dlg_exit = new JDialog(this.guiFrame);
		dlg_exit.setTitle("Exit?");
		dlg_exit.setLayout(new GridBagLayout());
		
		gbc_dialog.insets = new Insets(5, 10, 5, 5);
		
		setGBC(gbc_dialog, 0, 0, 2, 1);
		gbc_dialog.anchor = GridBagConstraints.CENTER;
		JLabel lbl_exit = new JLabel("Are you sure you want to exit?");
		dlg_exit.add(lbl_exit, gbc_dialog);
		
		JButton btn_yes = new JButton("Yes");
		btn_yes.addActionListener((ActionEvent e) -> {
			System.exit(0);
		});
		setGBC(gbc_dialog, 0, 1, 1, 1);
		gbc_dialog.anchor = GridBagConstraints.LINE_START;
		dlg_exit.add(btn_yes, gbc_dialog);
		
		JButton btn_no = new JButton("No");
		btn_no.addActionListener((ActionEvent e) -> {
			dlg_exit.dispose();
		});
		setGBC(gbc_dialog, 1, 1, 1, 1);
		gbc_dialog.anchor = GridBagConstraints.LINE_END;
		dlg_exit.add(btn_no, gbc_dialog);
		
		dlg_exit.pack();
		dlg_exit.setResizable(false);
		dlg_exit.setModalityType(ModalityType.APPLICATION_MODAL);
		dlg_exit.setLocationRelativeTo(null);
		dlg_exit.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dlg_exit.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dlg_exit.dispose();
			}
		});
		dlg_exit.setVisible(true);
	}
	
	// set the GBC object
	private void setGBC(GridBagConstraints gbc, int gridx, int gridy, int gridwidth, int gridheight) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
	}
	
	//draw on canvas
	public void printOnCanvas(String message) {
		this.chess_canvas.printMessage(message);
	}
	
	//postMessage to Log
	public void postToLog(String message) {
		String outMessage = "> " + message + "\n";
		this.txa_log.append(outMessage);
	}
	
	public void revalidateChessCanvas(){
		this.chess_canvas.revalidate();
	}
	
	//setters
	public void setChessCanvas(ChessCanvas chess_canvas) {
		this.chess_canvas = chess_canvas;
	}
	
	public void setChessBoard(Board board) {
		this.chess_canvas.setCurrentBoardState(board);
	}
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	public void setPlayerColor(boolean isWhite) {
		this.chess_canvas.setPlayerColor(isWhite);
	}
	
	public void setPlayerTurn(boolean turn) {
		this.chess_canvas.setPlayerTurn(turn);
	}
	
	//CRUTCH// stablizie the jframe
	private void stabilizeView() {
		for (int i = 1; i < 101; i++) {
			postToLog("Stabilizing app frame, " + i + "% done;");
		}
	}
	
	
}