import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class mainTester {

    static int sol1PassedTests = 0;
    static int sol1FailedTests = 0;
    static int sol2PassedTests = 0;
    static int sol2FailedTests = 0;

    public static void loadDBFile(GUI testerGUI, File dBFile) {
        try {
            BufferedReader input = null;
            input = new BufferedReader(new FileReader(dBFile.getAbsolutePath()));
            String line = null;
            String objectName;
            int objectX, objectY;
            ArrayList<String> inputPoints = new ArrayList<String>();

            while ((line = input.readLine()) != null) {
                String[] lineArray = line.split(" ");
                objectName = lineArray[0];
                objectX = Integer.parseInt(lineArray[1].split("=")[1]);
                objectY = Integer.parseInt(lineArray[2].split("=")[1]);
                inputPoints.add(line);

                testerGUI.insertDataFromDBFile(objectName, objectX, objectY);

            }
            testerGUI.updateInputList(inputPoints.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] testRect(GUI testerGUI, int leftTopX, int leftTopY, int rightBottomX, int rightBottomY, int solutionNum) {
        String[] results;
        if (solutionNum == 1) {
            results = testerGUI.studentSolution.firstSolution(leftTopX, leftTopY, rightBottomX, rightBottomY);
        } else {
            results = testerGUI.studentSolution.secondSolution(leftTopX, leftTopY, rightBottomX, rightBottomY);
        }
        return results;
    }

    /////////////////////////////////////////////////////////////////////////
    ////           IMPORTANT MESSAGE  - DONT TOUCH
    /////////////////////////////////////////////////////////////////////////
    public static void displayImportantMessage(GUI testerGUI){
        JOptionPane.showMessageDialog(testerGUI, "Click OK to begin test suit.");
    }

    public static String evaluateSolution(GUI testerGUI, int solutionNum) {
        BufferedReader reader;
        String conclusion = "";
        ArrayList<String> failedTestsCases = new ArrayList();
        ArrayList<String> failedTestsRectDetails = new ArrayList();
        ArrayList<String> actualValue = new ArrayList();
        ArrayList<String> expectedValue = new ArrayList();
        ArrayList<String> namesInAns = new ArrayList<>();
        boolean assertionError = false;
        int testsPassed = 0;
        int testsFailed = 0;
        int testNum;
        int leftTopX = 0, leftTopY = 0, rightBottomX = 0, rightBottomY = 0;
        String testExpectedAnswer;
        try {
            reader = new BufferedReader(new FileReader("./testResources/testsCases.txt"));
            String line = reader.readLine();
            while (line != null) {
                assertionError = false;
                String[] lineSplit = line.split(":");
                testNum = Integer.parseInt(lineSplit[0]);
                String[] coords = lineSplit[1].split(",");
                leftTopX = Integer.parseInt(coords[0]);
                leftTopY = Integer.parseInt(coords[1]);
                rightBottomX = Integer.parseInt(coords[2]);
                rightBottomY = Integer.parseInt(coords[3]);
                testExpectedAnswer = lineSplit[2].replace("[", "");
                testExpectedAnswer = testExpectedAnswer.replace("]", "");
                String[] ExpectedAnsSplit = testExpectedAnswer.split(",");
                String[] studentAnsArray = testRect(testerGUI, leftTopX, leftTopY, rightBottomX, rightBottomY, solutionNum);
                String studentAnsStr = Arrays.toString(studentAnsArray);
                if (testExpectedAnswer.length() > 0) {
                    for (String data : ExpectedAnsSplit) {
                        String name = data.trim();
                        name = name.split(" ")[0];
                        namesInAns.add(name);
                    }
                    for (String name : namesInAns) {
                        if (!studentAnsStr.contains(name)) {
                            assertionError = true;
                            break;
                        }
                    }
                } else {
                    if (!studentAnsStr.equals(lineSplit[2])) {
                        assertionError = true;
                    }
                }
                if (!assertionError) {
                    testsPassed++;
                } else {
                    testsFailed++;
                    failedTestsCases.add(line);
                    failedTestsRectDetails.add("Rectangel details: LeftTopPoint("+leftTopX+","+leftTopY+") " + "RightBottomPoint("+rightBottomX+","+rightBottomY+")");
                    expectedValue.add(testExpectedAnswer);
                    actualValue.add(studentAnsStr);
                }
                // read next line
                namesInAns.clear();
                line = reader.readLine();
            }
            reader.close();
            conclusion = "Tests Passed: " + testsPassed + "\n" + "Tests Failed: " + testsFailed;
            System.out.println("Tests Passed: " + testsPassed);
            System.out.println("Tests Failed: " + testsFailed);
            if (testsFailed > 0) {
                for (int i = 0; i < failedTestsCases.size(); i++) {
                    System.out.println("----------------------");
                    System.out.println(failedTestsCases.get(i));
                    System.out.println(failedTestsRectDetails.get(i));
                    System.out.println("Expected: " + expectedValue.get(i) + " Actual: " + actualValue.get(i));
                    setFailedColors(testerGUI);
                }
            }else{
                setSuccessColors(testerGUI);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(solutionNum == 1){
            sol1FailedTests = testsFailed;
            sol1PassedTests = testsPassed;
        }else{
            sol2FailedTests = testsFailed;
            sol2PassedTests = testsPassed;
        }
        return conclusion;
    }

    /////////////////////////////////////////////////////////////////////////
    ////           GUI SETUP  - DONT TOUCH
    /////////////////////////////////////////////////////////////////////////
    public static void setupTesterGUI(GUI testerGUI){
        Color yellow = new Color(255, 255, 102);
        testerGUI.setSize(1000, 900);
        testerGUI.setResizable(false);
        testerGUI.setTitle("Test Suit by PRKR");
        testerGUI.titledBorder7.setBorder(BorderFactory.createMatteBorder(6, 6, 6, 6, yellow));
        testerGUI.titledBorder7.setTitle("DS202 Assignment #5 Tester");
        testerGUI.titledBorder4.setTitle("Tests Results!");
        testerGUI.titledBorder4.setBorder(BorderFactory.createMatteBorder(6, 6, 6, 6, yellow));
        testerGUI.inputListPanel.setBackground(yellow);
        testerGUI.outputListPanel.setBackground(yellow);
        testerGUI.mainPanel.setBackground(new Color(224, 224, 224));
        testerGUI.inputMenu.setVisible(false);
        testerGUI.workingModes.setVisible(false);
        testerGUI.setVisible(true);
        testerGUI.loadPictureMenuItem.addActionListener(new GUI_loadPictureMenuItem_actionAdapter(testerGUI));
        testerGUI.loadTXTMenuItem.addActionListener(new GUI_loadTXTMenuItem_actionAdapter(testerGUI));
    }

    public static void setSuccessColors(GUI testerGUI){
        Color green = new Color(153, 255, 51);
        testerGUI.titledBorder7.setBorder(BorderFactory.createMatteBorder(6, 6, 6, 6, green));
        testerGUI.titledBorder4.setBorder(BorderFactory.createMatteBorder(6, 6, 6, 6, green));
        testerGUI.mainPanel.setBackground(green);
        testerGUI.inputListPanel.setBackground(green);
        testerGUI.outputListPanel.setBackground(green);
    }

    public static void setFailedColors(GUI testerGUI){
        Color red = new Color(205, 102, 102);
        testerGUI.titledBorder7.setBorder(BorderFactory.createMatteBorder(6, 6, 6, 6, red));
        testerGUI.titledBorder4.setBorder(BorderFactory.createMatteBorder(6, 6, 6, 6, red));
        testerGUI.mainPanel.setBackground(red);
        testerGUI.inputListPanel.setBackground(red);
        testerGUI.outputListPanel.setBackground(red);


    }

    /////////////////////////////////////////////////////////////////////////
    ////           Tester Main Method  - DONT TOUCH
    /////////////////////////////////////////////////////////////////////////
    /**
     * TEST CASE STRACTURE:
     *      testNum:leftTopX,leftTopY,rightBottomX,rightBottomY:[expected answer]
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        GUI testerGUI = new GUI();
        setupTesterGUI(testerGUI);
        ArrayList<String> conclusion = new ArrayList<String>();
        File picFile = new File("./testResources/pic1.jpg");
        File dbFile = new File("./testResources/DB1.txt");
        displayImportantMessage(testerGUI);
        loadDBFile(testerGUI, dbFile);
        testerGUI.picturePanel.loadPicture(picFile.getAbsolutePath());
        System.out.println("First Solution Results: ");
        conclusion.add("First Solution Results: \n");
        conclusion.add(evaluateSolution(testerGUI, 1) + "\n\n");
        System.out.println();
        conclusion.add("Second Solution Results:  \n");
        System.out.println("Second Solution Results: ");
        conclusion.add(evaluateSolution(testerGUI, 2) + "\n\n");
        conclusion.add("Test suit complete. \nfailed tests details can be seen in IntelliJ/Eclipse terminal.");
        testerGUI.updateOutputList(conclusion.toArray(new String[0]));
        if(sol1FailedTests ==0 && sol2FailedTests == 0){
            JOptionPane.showMessageDialog(testerGUI,"All "+sol1PassedTests+ " passed! \nGreat job.");
        }else{
            String fail1message ="";
            String fail2message ="";
            if(sol1FailedTests>0){
                fail1message = sol1FailedTests +" tests failed in first solution errors.";
            }
            if(sol2FailedTests>0){
                fail2message = sol2FailedTests +" tests failed in second solution errors.";
            }
            JOptionPane.showMessageDialog(testerGUI,"There are tests errors.\n" + fail1message + "\n" + fail2message);
        }

    }

}
