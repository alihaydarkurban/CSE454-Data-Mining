/*
    ALİ HAYDAR KURBAN
    151044058
    DATA MINING HOMEWORK 3
 */
public class TestOfBoxPlot
{
    private static void printSeparator()
    {
        System.out.println("===================================================================================================================================");
    }
    public static void main(String[] args)
    {
        BoxPlot boxPlot = new BoxPlot("Dataset.txt");
        boxPlot.createDataSet();

        printSeparator();
        boxPlot.findPointAnomalies();
        printSeparator();

        printSeparator();
        boxPlot.findGroupAnomalies();
        printSeparator();

        printSeparator();
        boxPlot.findConceptualAnomalies();
        printSeparator();

    }
}