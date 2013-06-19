package gameplay;

import static gameplay.PositionUtils.*;
import java.util.ArrayList;

public class PositionConsts {
	public static final int WIDTH = 7;
	public static final int HEIGHT = 6;
	public static final int SIZE = WIDTH*HEIGHT;
	public static final int WIN_SCORE = 2000;
	public static final int ZUGWANG_SCORE = 300;
	public static final int DOUBLE_THREAT_SCORE = 600;
	public static final long[] COLUMNS = createColumns();
	public static final long[] ROWS = createRows();
	public static final long[] VERTICAL_FOURS_IN_A_ROW = verticalFoursInARow();
	public static final long[] HORIZONTAL_ODD_FOURS_IN_A_ROW = horizontalOddFoursInARow();
	public static final long[] HORIZONTAL_EVEN_FOURS_IN_A_ROW = horizontalEvenFoursInARow();
	public static final long[] DIAGONAL_FOURS_IN_A_ROW = diagonalFoursInARow();
	
	public static final int[] columnIndices = createColumnIndices();

	private static long[] createColumns() {
		long[] columns = new long[WIDTH];
		for (int i = 0; i < WIDTH; i++) {
			long column = 0;
			for (int j = 0; j < HEIGHT; j++) {
				column += getPositionKey(i, j);
			}
			columns[i] = column;
		}
		return columns;
	}

	private static long[] createRows() {
        long[] rows = new long[HEIGHT];
        for (int i = 0; i < HEIGHT; i++) {
            long row = 0;
            for (int j = 0; j < WIDTH; j++) {
				row += getPositionKey(i, j);
            }
            rows[i] = row;
        }
        return rows;
    }

	private static int[] createColumnIndices() {		
		ArrayList<Integer> columnIndices = new ArrayList<Integer>();
		int middleColumn = WIDTH/2;
		columnIndices.add(middleColumn);
		for (int i = 1; i <= middleColumn; i++) {
			columnIndices.add(i + middleColumn);
			columnIndices.add(-i + middleColumn);
		}
		int[] result = new int[WIDTH];
		
		for (int i = 0; i < columnIndices.size(); i++) {
			result[i] = columnIndices.get(i);
		}
		
		return result;
	}

	private static long[] verticalFoursInARow() {
		ArrayList<Long> foursInARow = new ArrayList<Long>();
	
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT-3; j++) {
				long fourInARow = 0;
				for (int k = 0; k < 4; k++) {
					fourInARow += getPositionKey(i, j+k);
				}
				foursInARow.add(fourInARow);
			}
		}
	
		long[] result = new long[foursInARow.size()];
		for (int i = 0; i < foursInARow.size(); i++) {
			result[i] = foursInARow.get(i);
		}
		return result;
	}

	private static long[] horizontalOddFoursInARow() {
		ArrayList<Long> foursInARow = new ArrayList<Long>();
		
		for (int i = 0; i < HEIGHT; i=i+2) {
			for (int j = 0; j < WIDTH-3; j++) {
				long fourInARow = 0;
				for (int k = 0; k < 4; k++) {
					fourInARow += getPositionKey(j+k, i);
				}
				foursInARow.add(fourInARow);
			}
		}

		long[] result = new long[foursInARow.size()];
		for (int i = 0; i < foursInARow.size(); i++) {
			result[i] = foursInARow.get(i);
		}
		return result;
	}

	private static long[] horizontalEvenFoursInARow() {
		ArrayList<Long> foursInARow = new ArrayList<Long>();
		
		for (int i = 1; i < HEIGHT; i=i+2) {
			for (int j = 0; j < WIDTH-3; j++) {
				long fourInARow = 0;
				for (int k = 0; k < 4; k++) {
					fourInARow += getPositionKey(j+k, i);
				}
				foursInARow.add(fourInARow);
			}
		}

		long[] result = new long[foursInARow.size()];
		for (int i = 0; i < foursInARow.size(); i++) {
			result[i] = foursInARow.get(i);
		}
		return result;
	}

	private static long[] diagonalFoursInARow() {
		ArrayList<Long> foursInARow = new ArrayList<Long>();
		
		for (int i = 0; i < WIDTH-3; i++) {
			for (int j = 0; j < HEIGHT-3; j++) {
				long fourInARow1 = 0;
				long fourInARow2 = 0;
				for (int k = 0; k < 4; k++) {
					fourInARow1 += getPositionKey(i+k, j+k);
					fourInARow2 += getPositionKey(i+k, j+3-k);
				}
				foursInARow.add(fourInARow1);
				foursInARow.add(fourInARow2);
			}
		}
	
		long[] result = new long[foursInARow.size()];
		for (int i = 0; i < foursInARow.size(); i++) {
			result[i] = foursInARow.get(i);
		}
		return result;
	}

}
