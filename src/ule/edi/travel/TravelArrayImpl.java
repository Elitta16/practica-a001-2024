package ule.edi.travel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;

import ule.edi.model.*;

public class TravelArrayImpl implements Travel {

    private static final Double DEFAULT_PRICE = 100.0;
    private static final Byte DEFAULT_DISCOUNT = 25;
    private static final Byte CHILDREN_EXMAX_AGE = 18;
    private Date travelDate;
    private int nSeats;

    private Double price;    // precio de entradas
    private Byte discountAdvanceSale;   // descuento en venta anticipada (0..100)

    private Seat[] seats;

    public TravelArrayImpl(Date date, int nSeats) {
        this.travelDate = date;
        this.nSeats = nSeats;
        this.price = DEFAULT_PRICE;
        this.discountAdvanceSale = DEFAULT_DISCOUNT;
        this.seats = new Seat[nSeats];
    }

    public TravelArrayImpl(Date date, int nSeats, Double price, Byte discount) {
        this.travelDate = date;
        this.nSeats = nSeats;
        this.price = price;
        this.discountAdvanceSale = discount;
        this.seats = new Seat[nSeats];
    }

    @Override
    public Byte getDiscountAdvanceSale() {
        return this.discountAdvanceSale;
    }

    @Override
    public int getNumberOfSoldSeats() {
        int soldSeats = 0;
        for (Seat seat : seats) {
            if (seat != null && seat.getHolder() != null) {
                soldSeats++;
            }
        }
        return soldSeats;
    }

    @Override
    public int getNumberOfNormalSaleSeats() {
        int normalSaleSeats = 0;
        for (Seat seat : seats) {
            if (seat != null && seat.getHolder() != null && !seat.getAdvanceSale()) {
                normalSaleSeats++;
            }
        }
        return normalSaleSeats;
    }

    @Override
    public int getNumberOfAdvanceSaleSeats() {
        int advanceSaleSeats = 0;
        for (Seat seat : seats) {
            if (seat != null && seat.getHolder() != null && seat.getAdvanceSale()) {
                advanceSaleSeats++;
            }
        }
        return advanceSaleSeats;
    }

    @Override
    public int getNumberOfSeats() {
        return this.nSeats;
    }

    @Override
    public int getNumberOfAvailableSeats() {
        return this.nSeats - getNumberOfSoldSeats();
    }

    @Override
    public Seat getSeat(int pos) {
        if (pos > 0 && pos <= nSeats) {
            return seats[pos - 1];
        }
        return null;
    }

    @Override
public Person refundSeat(int pos) {
    if (pos >= 1 && pos <= nSeats && this.seats[pos-1] != null) {
        
            Person holder = this.seats[pos-1].getHolder();
            this.seats[pos - 1] = null; // Liberar el asiento
            return holder;
        
    }else{
    return null; // Asiento no ocupado o posición fuera de rango
    }
}

    @Override
    public List<Integer> getAvailableSeatsList() {
        List<Integer> availableSeats = new ArrayList<>();
        for (int i = 0; i < nSeats; i++) {
            if (this.seats[i] == null) {
                availableSeats.add(i + 1);
            }
        }
        return (availableSeats);
    }

    @Override
    public List<Integer> getAdvanceSaleSeatsList() {
        List<Integer> advanceSaleSeats = new ArrayList<>();
        for (int i = 0; i < nSeats; i++) {
            if (seats[i] != null && seats[i].getAdvanceSale()) {
                advanceSaleSeats.add(i + 1);
            }
        }
        return advanceSaleSeats;
    }

    @Override
    public int getMaxNumberConsecutiveSeats() {
    	int maximo=0;
    	int maxtemp=0;
    	for (int i =0; i < nSeats; i++) {
    		if (seats[i] ==null) {
    			maxtemp = maxtemp+1;
                maximo = Math.max(maximo , maxtemp);
            }else{
                maxtemp = 0;
            }
    	}
        return maximo;
    }

    public boolean isAdvanceSale(Person p) {
        for(int i = 0; i < this.nSeats;i++){
            if(this.seats[i] != null && this.seats[i].getHolder().equals(p) && this.seats[i].getAdvanceSale()){
                 return true;
            }
        }
        return false;
    }
    //un for que recorre los asientos y un if this.seats[i]&& lo mismp pero con getholder().equals(p)&& this.seats
//[i].getAdvanceSale()) return true 
    @Override
    public Date getTravelDate() {
        return this.travelDate;
    }

    @Override
public boolean sellSeatPos(int pos, String nif, String name, int edad, boolean isAdvanceSale) {
    if (pos >= 1 && pos <= nSeats && seats[pos - 1] == null) {
        // Verificar si ya existe un titular con el mismo NIF
        for (Seat seat : seats) {
            if (seat != null && seat.getHolder().getNif().equals(nif)) {
                // Ya existe un titular con el mismo NIF, no se puede vender el asiento
                return false;
            }
        }
        // No hay un titular con el mismo NIF, se puede vender el asiento
        seats[pos - 1] = new Seat(isAdvanceSale, new Person(nif, name, edad));
        return true;
    }
    return false; // Posición inválida o asiento ocupado
}

    @Override
    public int getNumberOfChildren() {
        int children = 0;
        for (Seat seat : seats) {
            if (seat != null && seat.getHolder() != null && seat.getHolder().getAge() < CHILDREN_EXMAX_AGE) {
                children++;
            }
        }
        return children;
    }

    @Override
    public int getNumberOfAdults() {
        int adults = 0;
        for (Seat seat : seats) {
            if (seat != null && seat.getHolder() != null && seat.getHolder().getAge() >= CHILDREN_EXMAX_AGE) {
                adults++;
            }
        }
        return adults;
    }

    @Override
    public Double getCollectionTravel() {
        double totalCollection = 0.0;
        for (Seat seat : seats) {
            if (seat != null && seat.getHolder() != null) {
                totalCollection += getSeatPrice(seat);
            }
        }
        return totalCollection;
    }

    @Override
    public int getPosPerson(String nif) {
        for (int i = 0; i < nSeats; i++) {
            Seat seat = seats[i];
            if (seat != null && seat.getHolder() != null && seat.getHolder().getNif().equals(nif)) {
                return i + 1;
            }
        }
        return -1;
    }

    @Override
    public int sellSeatFrontPos(String nif, String name, int age, boolean isAdvanceSale) {
        for (int i = 0; i < nSeats; i++) {
            if (seats[i] == null) {
                Person holder = new Person(nif, name, age);
                Seat seat = new Seat(isAdvanceSale, holder);
                seats[i] = seat;
                return i + 1;
            }
        }
        return -1;
    }

    @Override
    public int sellSeatRearPos(String nif, String name, int age, boolean isAdvanceSale) {
        for (int i = nSeats - 1; i >= 0; i--) {
            if (seats[i] == null) {
                Person holder = new Person(nif, name, age);
                Seat seat = new Seat(isAdvanceSale, holder);
                seats[i] = seat;
                return i + 1;
            }
        }
        return -1;
    }
    @Override
public double getPrice() {
    return this.price;
}

@Override
public Double getSeatPrice(Seat seat) {
    if (seat != null) {
        if (seat.getAdvanceSale()) {
            return this.price * (1 - this.discountAdvanceSale / 100.0);
        } else {
            return this.price;
        }
    }
    return 0.0; // o algún otro valor predeterminado
}

}
