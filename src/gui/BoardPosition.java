package gui;

public interface BoardPosition {
    public boolean isRed(Integer column, Integer row);
    public boolean isYellow(Integer column, Integer row);
    public boolean isRedWin(Integer column, Integer row);
    public boolean isYellowWin(Integer column, Integer row);
}