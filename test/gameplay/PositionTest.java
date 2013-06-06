package gameplay;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;

public class PositionTest {

	@Test
	public void testEmptyPositionHasNoZugwangOrDoubleThreats() throws Exception {
		Position position = new Position();
		assertEquals(Zugwang.NONE, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}

	@Test
	public void testEvenRedThreatHasNoZugwangOrDoubleThreats() throws Exception {
		Position position = new Position(makePieces(7, 8, 9), 0, Turn.RED, 0);
		assertEquals(Zugwang.NONE, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}

	@Test
	public void testOddRedThreatHasRedZugwang() throws Exception {
		Position position = new Position(makePieces(14, 15, 16), 0, Turn.RED, 0);
		assertEquals(Zugwang.RED, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}

	@Test
	public void testOddYellowThreatHasNoZugwangOrDoubleThreats() throws Exception {
		Position position = new Position(0, makePieces(14, 15, 16), Turn.RED, 0);
		assertEquals(Zugwang.NONE, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}

	@Test
	public void testEvenYellowThreatHasYellowZugwang() throws Exception {
		Position position = new Position(0, makePieces(7, 8, 9), Turn.RED, 0);
		assertEquals(Zugwang.YELLOW, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}

	@Test
	public void testTwoOddYellowThreatsHasYellowZugwang() throws Exception {
		Position position = new Position(0, makePieces(15, 16, 17), Turn.RED, 0);
		assertEquals(Zugwang.YELLOW, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}
	
	@Test
	public void testOddRedThreatAndEvenYellowThreatHasRedZugwang() throws Exception {
		Position position = new Position(makePieces(11, 16, 17, 18), makePieces(8, 9, 10, 15), Turn.RED, 0);
		assertEquals(Zugwang.RED, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}

	@Test
	public void testOddRedThreatAndOddYellowThreatHasNoZugwang() throws Exception {
		Position position = new Position(makePieces(15, 16, 17, 29), makePieces(18, 30, 31, 32), Turn.RED, 0);
		assertEquals(Zugwang.NONE, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}

	@Test
	public void testOddRedThreatAndTwoOddYellowThreatsHasNoZugwang() throws Exception {
		Position position = new Position(makePieces(15, 16, 17), makePieces(18, 30, 31, 32), Turn.RED, 0);
		assertEquals(Zugwang.NONE, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}
	
	@Test
	public void testTwoOddRedThreatsAndOddYellowThreatHasRedZugwang() throws Exception {
		Position position = new Position(makePieces(18, 30, 31, 32), makePieces(15, 16, 17), Turn.RED, 0);
		assertEquals(Zugwang.RED, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}
	
	@Test
	public void testOddRedThreatAndOddYellowAboveRedThreatHasRedZugwang() throws Exception {
		Position position = new Position(makePieces(14, 15, 16), makePieces(28, 29, 30), Turn.RED, 0);
		assertEquals(Zugwang.RED, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}

	@Test
	public void testOddRedAboveYellowThreatAndOddYellowThreatHasRedZugwang() throws Exception {
		Position position = new Position(makePieces(28, 29, 30), makePieces(14, 15, 16), Turn.RED, 0);
		assertEquals(Zugwang.RED, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}

	@Test
	public void testOddSharedThreatHasRedZugwang() throws Exception {
		Position position = new Position(makePieces(18, 22, 30, 38), makePieces(15, 16, 17, 46), Turn.RED, 0);
		assertEquals(Zugwang.RED, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}
	
	@Test
	public void testTwoOddSharedThreatsHasYellowZugwang() throws Exception {
		Position position = new Position(makePieces(15, 16, 17), makePieces(22, 24, 30, 36, 38), Turn.RED, 0);
		assertEquals(Zugwang.YELLOW, getZugwang(position));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(position));
	}	
	
	private Zugwang getZugwang(Position position) throws Exception {
		return (Zugwang) getField("zugwang").get(position);

	}

	private DoubleThreat getDoubleThreat(Position position) throws Exception {
		return (DoubleThreat) getField("doubleThreat").get(position);

	}

	private Field getField(String fieldName) throws Exception {
		for (Field field : Position.class.getDeclaredFields()) {
			if (field.getName().equals(fieldName)) {
				field.setAccessible(true);
				return field;
			}
		}
		return null;
	}
	
	private long makePieces(int... piecePositions) {
		long pieces = 0;
		for (int piecePosition : piecePositions) {
			pieces += Math.pow(2, piecePosition);
		}
		return pieces;
	}

}
