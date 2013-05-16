package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class Board extends Panel {
    private ArrayList<JPanel> squares = new ArrayList<JPanel>(); 
    private Integer columns;
    private Integer rows;
    private BoardEventListener eventListener;

    public Board(Integer columns, Integer rows, BoardEventListener eventListener) {
        super();
        this.eventListener = eventListener;
        this.columns = columns;
        this.rows = rows;
        displayBoard();            
    }

    private void displayBoard() {
        setLayout(new GridLayout(0, 1, 0, 0));
        for( Integer row = 0; row < rows; row++ ) {
            add(createRowPanel(row), 0);
        }
    }

    private JPanel createRowPanel(Integer row) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0, 0, 0));

        for( Integer column = 0; column < columns; column++ ) {
            panel.add(createSquare( column, row ));
        }
        return panel;
    }

    private JPanel createSquare(Integer column, Integer row) {
        JPanel square = new JPanel();
        square.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        square.addMouseListener( new SquareListener(column, row) );
        squares.add( square );
        return square;
    }

    public void showPosition(BoardPosition position) {
        for( int squareIndex = 0; squareIndex < squares.size(); squareIndex++ ) {
            int column = squareIndex % columns;
			int row = squareIndex / columns;
			if( position.isRedWin( column, row ) ) {
                squares.get( squareIndex ).setBackground( Color.MAGENTA );
            } else if( position.isYellowWin( column, row ) ) {
                squares.get( squareIndex ).setBackground( Color.GREEN );
            } else if( position.isRed( column, row ) ) {
                squares.get( squareIndex ).setBackground( Color.RED );
            } else if( position.isYellow( column, row ) ) {
                squares.get( squareIndex ).setBackground( Color.YELLOW );
            } else {
                squares.get( squareIndex ).setBackground( Color.LIGHT_GRAY );
            }
        }
    }

    private class SquareListener implements MouseListener {
        private Integer row;
        private Integer column;

        public SquareListener(Integer column, Integer row) {
            this.column = column;
            this.row = row;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if( eventListener != null ) {
                eventListener.squareClicked( column, row );
            } 
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseClicked(MouseEvent e) {}
    };
}