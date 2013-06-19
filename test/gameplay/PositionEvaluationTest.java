package gameplay;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;

public class PositionEvaluationTest {

	@Test
	public void testEmptyPositionEvaluationHasNoZugwangOrDoubleThreats() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, 0, 0);
		assertEquals(Zugwang.NONE, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}

	@Test
	public void testEvenRedThreatHasNoZugwangOrDoubleThreats() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, makePieces(7, 8, 9), 0);
		assertEquals(Zugwang.NONE, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}

	@Test
	public void testOddRedThreatHasRedZugwang() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, makePieces(14, 15, 16), 0);
		assertEquals(Zugwang.RED, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}

	@Test
	public void testOddYellowThreatHasNoZugwangOrDoubleThreats() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, 0, makePieces(14, 15, 16)); 
		assertEquals(Zugwang.NONE, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}

	@Test
	public void testEvenYellowThreatHasYellowZugwang() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, 0, makePieces(7, 8, 9));
		assertEquals(Zugwang.YELLOW, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}

	@Test
	public void testTwoOddYellowThreatsHasYellowZugwang() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, 0, makePieces(15, 16, 17)); 
		assertEquals(Zugwang.YELLOW, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}
	
	@Test
	public void testOddRedThreatAndEvenYellowThreatHasRedZugwang() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, makePieces(11, 16, 17, 18), makePieces(8, 9, 10, 15));
		assertEquals(Zugwang.RED, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}

	@Test
	public void testOddRedThreatAndOddYellowThreatHasNoZugwang() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, makePieces(15, 16, 17, 29), makePieces(18, 30, 31, 32));
		assertEquals(Zugwang.NONE, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}

	@Test
	public void testOddRedThreatAndTwoOddYellowThreatsHasNoZugwang() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, makePieces(15, 16, 17), makePieces(18, 30, 31, 32));
		assertEquals(Zugwang.NONE, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}
	
	@Test
	public void testTwoOddRedThreatsAndOddYellowThreatHasRedZugwang() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, makePieces(18, 30, 31, 32), makePieces(15, 16, 17));
		assertEquals(Zugwang.RED, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}
	
	@Test
	public void testOddRedThreatAndOddYellowAboveRedThreatHasRedZugwang() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, makePieces(14, 15, 16), makePieces(28, 29, 30));
		assertEquals(Zugwang.RED, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}

	@Test
	public void testOddRedAboveYellowThreatAndOddYellowThreatHasRedZugwang() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, makePieces(28, 29, 30), makePieces(14, 15, 16));
		assertEquals(Zugwang.RED, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}

	@Test
	public void testOddSharedThreatHasRedZugwang() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, makePieces(18, 22, 30, 38), makePieces(15, 16, 17, 46));
		assertEquals(Zugwang.RED, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}
	
	@Test
	public void testTwoOddSharedThreatsHasYellowZugwang() throws Exception {
		PositionEvaluation positionEvaluation = new PositionEvaluation(Turn.RED, makePieces(15, 16, 17), makePieces(22, 24, 30, 36, 38));
		assertEquals(Zugwang.YELLOW, getZugwang(positionEvaluation));
		assertEquals(DoubleThreat.NONE, getDoubleThreat(positionEvaluation));
	}	
	
	private Zugwang getZugwang(PositionEvaluation positionEvaluation) throws Exception {
		return (Zugwang) getField("zugwang").get(positionEvaluation);

	}

	private DoubleThreat getDoubleThreat(PositionEvaluation positionEvaluation) throws Exception {
		return (DoubleThreat) getField("doubleThreat").get(positionEvaluation);

	}

	private Field getField(String fieldName) throws Exception {
		for (Field field : PositionEvaluation.class.getDeclaredFields()) {
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
