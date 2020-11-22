/*
 * Facebook Tagger Main Interface.
 */
public interface MyInterface {
	void insertDataFromDBFile(String objectName, int objectX, int objectY);
	String[] firstSolution(int leftTopX, int leftTopY, int rightBottomX, int rightBottomY );
	String[] secondSolution(int leftTopX, int leftTopY, int rightBottomX, int rightBottomY );
}

