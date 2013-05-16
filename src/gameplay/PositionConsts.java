package gameplay;
import java.util.ArrayList;
import java.util.BitSet;


public class PositionConsts {
	public final static int WIDTH = 7;
	public final static int HEIGHT = 6;
	public final static int SIZE = WIDTH*HEIGHT;
	public final static int WIN_SCORE = 10000;
	public final static int ZUGWANG_SCORE = 5000;
	public final static BitSet[] COLUMNS = createColumns();
    public final static BitSet[] ROWS = createRows();
	public final static ArrayList<BitSet> FOURS_IN_A_ROW = createFoursInARow();
	public final static ArrayList<Integer> columnIndices = createColumnIndices();

	private static BitSet[] createColumns() {
		BitSet[] columns = new BitSet[WIDTH];
		for (int i = 0; i < WIDTH; i++) {
			BitSet column = new BitSet(SIZE);
			for (int j = 0; j < HEIGHT; j++) {
				column.set(getPositionIndex(i, j));
			}
			columns[i] = column;
		}
		return columns;
	}

	private static BitSet[] createRows() {
        BitSet[] columns = new BitSet[HEIGHT];
        for (int i = 0; i < HEIGHT; i++) {
            BitSet column = new BitSet(SIZE);
            for (int j = 0; j < WIDTH; j++) {
                column.set(getPositionIndex(j, i));
            }
            columns[i] = column;
        }
        return columns;
    }

	private static ArrayList<Integer> createColumnIndices() {		
		ArrayList<Integer> columnIndices = new ArrayList<Integer>();
		int middleColumn = WIDTH/2;
		columnIndices.add(middleColumn);
		for (int i = 1; i <= middleColumn; i++) {
			columnIndices.add(i + middleColumn);
			columnIndices.add(-i + middleColumn);
		}
		return columnIndices;
	}

	private static ArrayList<BitSet> createFoursInARow() {
		ArrayList<BitSet> foursInARow = new ArrayList<BitSet>();

		// Add vertical fours in a row
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT-3; j++) {
				BitSet fourInARow = new BitSet(SIZE);
				for (int k = 0; k < 4; k++) {
					fourInARow.set(getPositionIndex(i, j+k));
				}
				foursInARow.add(fourInARow);
			}
		}
		
		// Add horizontal fours in a row
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH-3; j++) {
				BitSet fourInARow = new BitSet(SIZE);
				for (int k = 0; k < 4; k++) {
					fourInARow.set(getPositionIndex(j+k, i));
				}
				foursInARow.add(fourInARow);
			}
		}
		
		// Add diagonal fours in a row
		for (int i = 0; i < WIDTH-3; i++) {
			for (int j = 0; j < HEIGHT-3; j++) {
				BitSet fourInARow1 = new BitSet(SIZE);
				BitSet fourInARow2 = new BitSet(SIZE);
				for (int k = 0; k < 4; k++) {
					fourInARow1.set(getPositionIndex(i+k, j+k));
					fourInARow2.set(getPositionIndex(i+k, j+3-k));
				}
				foursInARow.add(fourInARow1);
				foursInARow.add(fourInARow2);
			}
		}

		return foursInARow;
	}
			
	private static int getPositionIndex(int column, int row) {
		return column + row*WIDTH;
	}
}
