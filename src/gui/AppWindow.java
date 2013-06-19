package gui;

import gameplay.Difficulty;
import gameplay.Play;
import gameplay.Position;
import gameplay.ZobristHashing;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

public class AppWindow {
	public static void main(String[] args) {
		EventQueue.invokeLater( new Runnable() {
			public void run() {
				try {
					new AppWindow();
				} catch( Exception e ) {
					e.printStackTrace();
				}
			}
		} );
	}

	private Board board;
	private Play play = new Play();
	private Difficulty difficulty = Difficulty.MEDIUM;
	private Position position;
	private List<Position> history = new ArrayList<Position>();

	public AppWindow() {
		JFrame frame = new JFrame("Connect 4");
		frame.setBounds( 100, 100, 450, 300 );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		frame.getContentPane().add(createBoard(), BorderLayout.CENTER);
		frame.getContentPane().add(createButtons(), BorderLayout.WEST);

		frame.setVisible( true );
	}

	private Board createBoard() {
		board = new Board( 7, 6, new BoardEventListener() {
			@Override
			public void squareClicked(Integer column, Integer row) {
				playerMove(column);
			}
		});
		startNewGame();
		return board;
	}

	private JPanel createButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		JButton button = new JButton("Play as red");
		panel.add(button);
		button.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startNewGame();
			}
		});
		JButton button1 = new JButton("Play as yellow");
		panel.add(button1);
		button1.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startNewGame();
				playMove();
			}
		});
		JButton button2 = new JButton("Computer move");
		panel.add(button2);
		button2.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playMove();
			}
		});
		JButton button3 = new JButton("Take back move");
		panel.add(button3);
		button3.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				takeBackMove();
			}
		});
		JButton button4 = new JButton(difficulty.toString());
		panel.add(button4);
		button4.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				difficulty = difficulty.getNext();
				((JButton) e.getSource()).setText(difficulty.toString());
			}
		});

		return panel;
	}

	private void startNewGame() {
		history.clear();
		ZobristHashing.clear();
		position = new Position();
		showPosition();
	}

	private void showPosition() {
		board.showPosition(position.getBoardDisplay());
	}

	private boolean updatePosition(Position newPosition) {
		if (newPosition != null) {
			history.add(position);
			position = newPosition;
			showPosition();
			return true;
		} else {
			return false;
		}
	}

	private void playMove() {
		if (!position.isGameOver()) {
			EventQueue.invokeLater( new Runnable() {
				public void run() {
					updatePosition(play.findNextMove(position, difficulty));
				}
			} );
		}
	}

	private void playerMove(Integer column) {
		if (!position.isGameOver()) {
			if (updatePosition(position.playMove(column))) {
				playMove();
			}
		}
	}

	private void takeBackMove() {
		if (history.size() > 0) {
			position = history.remove(history.size()-1);
			showPosition();
		}
	}

}
