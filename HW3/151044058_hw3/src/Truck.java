/*
    ALİ HAYDAR KURBAN
    151044058
    DATA MINING HOMEWORK 3
 */
public class Truck implements Comparable<Truck>
{
    private int Id;
    private String  DateOfTraveling;
    private double AmountOfRoad;
    private double LiterOfFuel;
    private double FuelPerKilometer;

    public Truck() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setDateOfTraveling(String dateOfTraveling) {
        DateOfTraveling = dateOfTraveling;
    }

    public double getAmountOfRoad() {
        return AmountOfRoad;
    }

    public void setAmountOfRoad(double amountOfRoad) {
        AmountOfRoad = amountOfRoad;
    }

    public double getLiterOfFuel() {
        return LiterOfFuel;
    }

    public void setLiterOfFuel(double literOfFuel) {
        LiterOfFuel = literOfFuel;
    }

    public double getFuelPerKilometer() {
        return FuelPerKilometer;
    }

    public void setFuelPerKilometer(double fuelPerKilometer) {
        FuelPerKilometer = fuelPerKilometer;
    }

    @Override
    public String toString() {
        return "Truck { " +
                "Id = " + Id +
                ", DateOfTraveling = " + DateOfTraveling  +
                ", AmountOfRoad = " + AmountOfRoad +
                "(km), LiterOfFuel = " + LiterOfFuel +
                "(lt), FuelPerKilometer = " + FuelPerKilometer +
                "(lt/km) }";
    }

    @Override
    public int compareTo(Truck o) {
        if(this.getFuelPerKilometer() == o.getFuelPerKilometer())
            return 0;

        else if(this.getFuelPerKilometer() < o.getFuelPerKilometer())
            return -1;
        else
            return 1;
    }
}