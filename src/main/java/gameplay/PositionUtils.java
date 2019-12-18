package gameplay;

public class PositionUtils {

	public static int cardinality(long pieces) {
		return Long.bitCount(pieces);
	}

	public static long getPositionKey(int column, int row) {
		return (long) Math.pow(2, (column + row*PositionConsts.WIDTH));
	}

}
