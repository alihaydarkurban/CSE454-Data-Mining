/*
    ALİ HAYDAR KURBAN
    151044058
    DATA MINING HOMEWORK 3
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class BoxPlot
{
    private final double OnePointFive = 1.5;
    private final String RushHourStartsMorning = "\"07:00\"";
    private final String RushHourEndsMorning = "\"10:00\"";
    private final String RushHourStartsEvening = "\"16:00\"";
    private final String RushHourEndsEvening = "\"20:00\"";
    private final double MinRoad = 49;
    private final double MaxRoad = 301;
    private String filename;
    private ArrayList<ArrayList<String>> dataset;
    private ArrayList<Truck> Trucks;
    private double min;
    private double Q1;
    private double median;
    private double Q3;
    private double max;
    private double IQR;
    private double lowerLimit;
    private double upperLimit;

    public BoxPlot(String _filename)
    {
        dataset = new ArrayList<>();
        Trucks = new ArrayList<>();
        filename = _filename;
    }
    public void createDataSet()
    {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filename)))
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                ArrayList<String> items = new ArrayList<>();
                String[] arrayOfString = line.split("[, ?@]+");
                for(int i = 0; i < arrayOfString.length; ++i)
                {
                    items.add(arrayOfString[i]);
                }
                dataset.add(items);
            }
        } catch (IOException e)
        {
            System.err.format("IOException: %s%n", e);
        }

        for(ArrayList<String> lineOfDataset : dataset.subList(1,dataset.size()))
        {
            Truck truck = new Truck();
            truck.setId(Integer.parseInt(lineOfDataset.get(0)));
            truck.setDateOfTraveling(lineOfDataset.get(1));
            truck.setAmountOfRoad(Double.parseDouble(lineOfDataset.get(2)));
            truck.setLiterOfFuel(Double.parseDouble(lineOfDataset.get(3)));
            truck.setFuelPerKilometer(calculateFuelPerKilometer(truck.getLiterOfFuel(),truck.getAmountOfRoad()));
            Trucks.add(truck);
        }
        createFiveNumSummary();
    }
    public void findPointAnomalies()
    {
        System.out.println("Point Anomalies");
        System.out.println("Lower Limit : " + lowerLimit);
        System.out.println("Upper Limit : " + upperLimit);
        for(ArrayList<String> lineOfData : dataset.subList(1,dataset.size()))
        {
            double fuel_per_kilometer = calculateFuelPerKilometer(Double.parseDouble(lineOfData.get(3)), Double.parseDouble(lineOfData.get(2)));
            if(fuel_per_kilometer < lowerLimit)
            {
                int checkMorning1 = RushHourStartsMorning.compareTo(lineOfData.get(1));
                int checkMorning2 = RushHourEndsMorning.compareTo(lineOfData.get(1));
                int checkEvening1 = RushHourStartsEvening.compareTo(lineOfData.get(1));
                int checkEvening2 = RushHourEndsEvening.compareTo(lineOfData.get(1));

                if((checkMorning1 > 0 || checkMorning2 < 0) && (checkEvening1 > 0 || checkEvening2 < 0))
                {
                    int id = Integer.parseInt(lineOfData.get(0));
                    Truck pointAnomalyTruck = getTruckById(id);
                    if(pointAnomalyTruck != null)
                        printTruct(pointAnomalyTruck);

                    System.out.println("Warning! Id : " + id + " Truck spends very few fuel. It may not accord rota.");
                }

            }
            if(fuel_per_kilometer > upperLimit)
            {
                int checkMorning1 = RushHourStartsMorning.compareTo(lineOfData.get(1));
                int checkMorning2 = RushHourEndsMorning.compareTo(lineOfData.get(1));
                int checkEvening1 = RushHourStartsEvening.compareTo(lineOfData.get(1));
                int checkEvening2 = RushHourEndsEvening.compareTo(lineOfData.get(1));

                if((checkMorning1 > 0 || checkMorning2 < 0) && (checkEvening1 > 0 || checkEvening2 < 0))
                {
                    int id = Integer.parseInt(lineOfData.get(0));
                    Truck pointAnomalyTruck = getTruckById(id);
                    if (pointAnomalyTruck != null)
                        printTruct(pointAnomalyTruck);

                    System.out.println("Warning! Id : " + id + " Truck spends too much fuel. It may steal fuel.");
                }
            }
        }
    }
    public void findGroupAnomalies()
    {
        int index = 1;
        System.out.println("Group Anomalies");
        System.out.println("Lower Limit : " + lowerLimit);
        System.out.println("Upper Limit : " + upperLimit);
        System.out.println("Lower Road Limit : " + MinRoad);
        System.out.println("Upper Road Limit : " + MaxRoad);
        for(ArrayList<String> lineOfData : dataset.subList(1,dataset.size()))
        {
            double fuel_per_kilometer = calculateFuelPerKilometer(Double.parseDouble(lineOfData.get(3)), Double.parseDouble(lineOfData.get(2)));
            if (fuel_per_kilometer >= lowerLimit && fuel_per_kilometer <= upperLimit)
            {
                if(Double.parseDouble(lineOfData.get(2)) > MaxRoad)
                {
                    if(index < dataset.size() - 1 && index > 2)
                    {
                        var newLineOfData1 = dataset.get(index + 1);
                        var newLineOfData2 = dataset.get(index - 1);
                        if(Double.parseDouble(newLineOfData1.get(2)) > MaxRoad || Double.parseDouble(newLineOfData2.get(2)) > MaxRoad)
                        {
                            int id = Integer.parseInt(lineOfData.get(0));
                            Truck pointAnomalyTruck = getTruckById(id);
                            if (pointAnomalyTruck != null)
                                printTruct(pointAnomalyTruck);

                            System.out.println("Warning! Id : " + id + " Truck travels a lot.");
                        }
                    }
                }

                if(Double.parseDouble(lineOfData.get(2)) < MinRoad)
                {
                    if(index < dataset.size() - 1 && index > 2)
                    {
                        var newLineOfData1 = dataset.get(index + 1);
                        var newLineOfData2 = dataset.get(index - 1);
                        if(Double.parseDouble(newLineOfData1.get(2)) < MinRoad || Double.parseDouble(newLineOfData2.get(2)) < MinRoad)
                        {
                            int id = Integer.parseInt(lineOfData.get(0));
                            Truck pointAnomalyTruck = getTruckById(id);
                            if (pointAnomalyTruck != null)
                                printTruct(pointAnomalyTruck);

                            System.out.println("Warning! Id : " + id + " Truck travels very few.");
                        }
                    }
                }
            }
            index++;
        }
    }
    public void findConceptualAnomalies()
    {
        System.out.println("Conceptual Anomalies");
        System.out.println("Lower Limit : " + lowerLimit);
        System.out.println("Upper Limit : " + upperLimit);

        for(ArrayList<String> lineOfData : dataset.subList(1,dataset.size()))
        {
            double fuel_per_kilometer = calculateFuelPerKilometer(Double.parseDouble(lineOfData.get(3)), Double.parseDouble(lineOfData.get(2)));
            if((fuel_per_kilometer >= lowerLimit && fuel_per_kilometer <= upperLimit) || fuel_per_kilometer < lowerLimit)
            {
                int checkMorning1 = RushHourStartsMorning.compareTo(lineOfData.get(1));
                int checkMorning2 = RushHourEndsMorning.compareTo(lineOfData.get(1));
                int checkEvening1 = RushHourStartsEvening.compareTo(lineOfData.get(1));
                int checkEvening2 = RushHourEndsEvening.compareTo(lineOfData.get(1));

                if((checkMorning1 <= 0 && checkMorning2 >= 0) || (checkEvening1 <= 0 && checkEvening2 >= 0))
                {
                    int id = Integer.parseInt(lineOfData.get(0));
                    Truck pointAnomalyTruck = getTruckById(id);
                    if(pointAnomalyTruck != null)
                        printTruct(pointAnomalyTruck);

                    System.out.println("Warning! Id : " + id + " Truck spends very few fuel. It may not accord rota.");
                }

            }
            if(fuel_per_kilometer >= upperLimit * OnePointFive)
            {
                int checkMorning1 = RushHourStartsMorning.compareTo(lineOfData.get(1));
                int checkMorning2 = RushHourEndsMorning.compareTo(lineOfData.get(1));
                int checkEvening1 = RushHourStartsEvening.compareTo(lineOfData.get(1));
                int checkEvening2 = RushHourEndsEvening.compareTo(lineOfData.get(1));

                if((checkMorning1 <= 0 && checkMorning2 >= 0) || (checkEvening1 <= 0 && checkEvening2 >= 0))
                {
                    int id = Integer.parseInt(lineOfData.get(0));
                    Truck pointAnomalyTruck = getTruckById(id);
                    if (pointAnomalyTruck != null)
                        printTruct(pointAnomalyTruck);

                    System.out.println("Warning! Id : " + id + " Truck spends too much fuel. It may steal fuel.");
                }
            }
        }
    }
    private void createFiveNumSummary()
    {
        Collections.sort(Trucks);
        int size = Trucks.size();
        int sizeOfLetfPart = size / 2;
        int sizeOfRightPart = size / 2;

        min = Trucks.get(0).getFuelPerKilometer();
        max = Trucks.get(Trucks.size() - 1).getFuelPerKilometer();

        if(size % 2 == 1)
        {
            median = Trucks.get(size / 2).getFuelPerKilometer();
        }
        else
        {
            double val1Median = Trucks.get(size / 2).getFuelPerKilometer();
            double val2Median = Trucks.get((size / 2) - 1).getFuelPerKilometer();
            median = (val1Median + val2Median) / 2;
        }

        if(sizeOfLetfPart % 2 == 1 && sizeOfRightPart % 2 == 1)
        {
            Q1 = Trucks.get(sizeOfLetfPart / 2).getFuelPerKilometer();
            Q3 = Trucks.get(size - 1 - (sizeOfRightPart / 2)).getFuelPerKilometer();
        }

        else
        {
            double val1Q1 = Trucks.get(sizeOfLetfPart / 2).getFuelPerKilometer();
            double val2Q1 = Trucks.get((sizeOfLetfPart / 2) - 1).getFuelPerKilometer();
            Q1 = (val1Q1 + val2Q1) / 2;


            double val1Q3 = Trucks.get(size - (sizeOfLetfPart / 2)).getFuelPerKilometer();
            double val2Q3 = Trucks.get(size - 1 -(sizeOfLetfPart / 2)).getFuelPerKilometer();
            Q3 = (val1Q3 + val2Q3) / 2;
        }
        calculateBoxPlotLimits();
    }
    private void calculateBoxPlotLimits()
    {
        IQR = Q3 - Q1;
        lowerLimit = Q1 - (IQR * OnePointFive);
        upperLimit = Q3 + (IQR * OnePointFive);
    }
    private double calculateFuelPerKilometer(double lt, double km)
    {
        return lt / km;
    }
    private Truck getTruckById(int id)
    {
        for(Truck truck : Trucks)
        {
            if(truck.getId() == id)
                return truck;
        }
        return null;
    }
    private void printTrucks()
    {
        for(Truck truct : Trucks)
            System.out.println(truct.toString());
    }
    private void printTruct(Truck truct)
    {
        System.out.println(truct.toString());
    }
}
