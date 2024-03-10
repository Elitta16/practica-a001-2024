package ule.edi.travel;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.*;

import ule.edi.model.*;

public class TravelArrayImplTests {
	private DateFormat dformat = null;
	private TravelArrayImpl e;
	private TravelArrayImpl ep;
    private Date date;
    private int nSeats;
    private TravelArrayImpl travel;
	private Double price;
    private Byte discount;
    
	
	private Date parseLocalDate(String spec) throws ParseException {
        return dformat.parse(spec);
	}

	public TravelArrayImplTests() {
		dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}
	
	@Before
	public void testBefore() throws Exception{
		e = new TravelArrayImpl(parseLocalDate("24/02/2020 17:00:00"), 110);
		ep = new TravelArrayImpl(parseLocalDate("24/02/2020 17:00:00"), 4);
	}

	@Test
	public void assertTrueToBeTrue() throws Exception {
		Assert.assertTrue(true);
	}
	
	@Test
	public void testEventoVacio() throws Exception {
	Assert.assertTrue(e.getNumberOfAvailableSeats()==110);
	Assert.assertEquals(110, e.getNumberOfAvailableSeats());
	Assert.assertEquals(0, e.getNumberOfAdults());
	Assert.assertEquals(0, e.getNumberOfChildren());
	Assert.assertEquals(100.0,0.0, e.getPrice());	
	}
	
	// // test 2 constructor
	@Test
	public void test2Constructor() throws Exception{
	TravelArrayImpl  e2 = new TravelArrayImpl(parseLocalDate("24/02/2020 17:00:00"), 110, 200.0, (byte) 20);
	Assert.assertEquals(parseLocalDate("24/02/2020 17:00:00"), e2.getTravelDate());

	Assert.assertEquals( 200.0,0.0, e2.getPrice());
	Assert.assertEquals((byte)20,(byte) e2.getDiscountAdvanceSale());
	}
	
	
	@Test
	public void test2ConstructorCollect() throws Exception{
	TravelArrayImpl e2 = new TravelArrayImpl(parseLocalDate("24/02/2018 17:00:00"), 110, 200.0, (byte) 20);
	Assert.assertTrue(e2.sellSeatPos(1, "10203040A","Alice", 34,false));	//venta normal
	Assert.assertTrue(e2.sellSeatPos(2, "10203040B","Alice", 34,true));	//venta anticipada
	Assert.assertEquals(2, e2.getNumberOfSoldSeats());	

	Assert.assertEquals(360.0,0.0,e2.getCollectionTravel());
	}
	
	// // test getDiscountAdvanceSale
	
	@Test
	public void testGetDiscountAdvanceSale() throws Exception {
		
	Assert.assertTrue(e.getDiscountAdvanceSale()==25);
	}
	
	// // test getDate
	
	@Test
	public void testGetDate() throws Exception {
	Assert.assertEquals(parseLocalDate("24/02/2020 17:00:00"), e.getTravelDate());
	Assert.assertEquals(110,e.getNumberOfAvailableSeats());
	Assert.assertEquals(0, e.getNumberOfAdults());
	Assert.assertEquals(0, e.getNumberOfSoldSeats());	
	}
	
	// // test getNumber....
	@Test
	public void testsellSeatPos1Adult() throws Exception{	
	Assert.assertEquals(0, e.getNumberOfAdults());
	Assert.assertTrue(e.sellSeatPos(4, "10203040A","Alice", 18,false));	//venta normal
	Assert.assertEquals(1,e.getNumberOfAdults());  
	Assert.assertEquals(0,e.getNumberOfAdvanceSaleSeats());	
	Assert.assertEquals(1,e.getNumberOfNormalSaleSeats());  
	Assert.assertEquals(1,e.getNumberOfSoldSeats());	
	Assert.assertEquals(110,e.getNumberOfSeats());  
	}

	// // TEST OF sellSeatPos
	@Test
	public void testsellSeatPosPosCero() throws Exception{		
	Assert.assertEquals(false,e.sellSeatPos(0, "10203040A","Alice", 34,false));	//venta normal  
	}
	
	@Test
	public void testsellSeatPosPosMayorMax() throws Exception{		
	Assert.assertEquals(false,e.sellSeatPos(e.getNumberOfAvailableSeats()+1, "10203040A","Alice", 34,false));	//venta normal  
	}

	@Test
	public void testsellSeatPosPosOcupada() throws Exception{		
	Assert.assertEquals(true, e.sellSeatPos(5, "10203040A","Alice", 34,false));	//venta normal  
	Assert.assertEquals(false, e.sellSeatPos(5, "10203040A","Alice", 34,false));	//venta normal  
	}
	
	// //TEST OF GET COLLECTION
	
	@Test
	public void testgetCollectionAnticipadaYnormal() throws Exception{
	Assert.assertEquals(true, e.sellSeatPos(1, "1010", "AA", 10, true));
	Assert.assertEquals(true, e.sellSeatPos(4, "10101", "AA", 10, false));
	Assert.assertTrue(e.getCollectionTravel()==175.0);					
	}
	
	// // TEST List
	@Test
	public void testGetListEventoCompleto() throws Exception{		
	Assert.assertEquals(true, ep.sellSeatPos(1, "10203040A","Alice", 34,true));	//venta normal  
	Assert.assertEquals(true, ep.sellSeatPos(2, "10203040B","Alice", 34,true));	//venta normal  
	Assert.assertEquals(true, ep.sellSeatPos(3, "10203040C","Alice", 34,false));	//venta normal  
	Assert.assertEquals(true, ep.sellSeatPos(4, "10203040D","Alice", 34,false));	//venta normal  
	Assert.assertEquals("[]", ep.getAvailableSeatsList().toString());
	Assert.assertEquals("[1, 2]", ep.getAdvanceSaleSeatsList().toString());
	}
	
	// //TEST DE GETPRICE
	
	@Test
	public void testgetPrice() throws Exception{
	Assert.assertEquals(true,e.sellSeatPos(1, "1010", "AA", 10, true));
	Assert.assertEquals(true,e.sellSeatPos(4, "10101", "AA", 10, false));
	Assert.assertEquals(100.0,0.0,e.getSeatPrice(e.getSeat(4)));
	Assert.assertEquals(75.0,0.0,e.getSeatPrice(e.getSeat(1)));
	}
	
	// //tests REFUND 
		
	@Test
	public void testREFUNDCero() throws Exception{
	Assert.assertEquals(true,e.sellSeatPos(1, "1010", "AA", 10, true));	
	Assert.assertEquals(null,e.refundSeat(0));
	}
			
	@Test
	public void testrefundOk() throws Exception{
	Person p = new Person("1010", "AA",10);
	Assert.assertEquals(true, e.sellSeatPos(1, "1010", "AA", 10, true));	
	Assert.assertEquals(p,e.refundSeat(1));
	}
		
	// // TEST GetPosPerson
	@Test
	public void testGetPosPersonLleno() throws Exception{		
	Assert.assertEquals(true,ep.sellSeatPos(1, "10203040","Alic", 34,true));	//venta anticipada  
	Assert.assertEquals(true,ep.sellSeatPos(3, "10203040A","Alice", 34,false));	//venta normal  
	Assert.assertEquals(true,ep.sellSeatPos(4, "10203040B","Alice", 34,false));	//venta normal  
	Assert.assertEquals(-1,ep.getPosPerson("10205040"));
	Assert.assertEquals(false,ep.isAdvanceSale(new Person("10203040A","Alice", 34)));
	Assert.assertEquals(true,ep.isAdvanceSale(new Person("10203040","Alic", 34)));
	Assert.assertEquals(false,ep.isAdvanceSale(new Person("10202531", "Ana", 31)));
	Assert.assertEquals(3,ep.getPosPerson("10203040A"));
	}		

	@Test
public void testGetNumberOfSoldSeats() throws Exception {
    assertEquals(0, e.getNumberOfSoldSeats());
    e.sellSeatPos(1, "1010", "AA", 10, true);
    assertEquals(1, e.getNumberOfSoldSeats());
    e.sellSeatPos(2, "2020", "BB", 20, false);
    assertEquals(2, e.getNumberOfSoldSeats());
}

@Test
public void testGetNumberOfNormalSaleSeats() throws Exception {
    assertEquals(0, e.getNumberOfNormalSaleSeats());
    e.sellSeatPos(1, "1010", "AA", 10, true);
    assertEquals(0, e.getNumberOfNormalSaleSeats()); // Solo se vendió en venta anticipada
    e.sellSeatPos(2, "2020", "BB", 20, false);
    assertEquals(1, e.getNumberOfNormalSaleSeats());
}

@Test
public void testGetNumberOfAdvanceSaleSeats() throws Exception {
    assertEquals(0, e.getNumberOfAdvanceSaleSeats());
    e.sellSeatPos(1, "1010", "AA", 10, true);
    assertEquals(1, e.getNumberOfAdvanceSaleSeats());
    e.sellSeatPos(2, "2020", "BB", 20, false);
    assertEquals(1, e.getNumberOfAdvanceSaleSeats()); // Solo se vendió en venta anticipada
}

@Test
public void testGetNumberOfChildren() throws Exception {
    assertEquals(0, e.getNumberOfChildren());
    e.sellSeatPos(1, "1010", "AA", 10, true); // Venta anticipada
    assertEquals(1, e.getNumberOfChildren());
    e.sellSeatPos(2, "2020", "BB", 20, false); // Venta normal
    assertEquals(1, e.getNumberOfChildren()); // Aún hay un niño
    e.sellSeatPos(3, "3030", "CC", 30, false); // Adulto
    assertEquals(1, e.getNumberOfChildren()); // No cambia
	}

    @Test
    public void testSellSeatPosValid() {
        assertTrue(travel.sellSeatPos(1, "123456789G", "Pepe Perez", 30, false));
    }

    @Test
    public void testSellSeatPosInvalidPosition() {
        assertFalse(travel.sellSeatPos(0, "123456789G", "Pepe Perez", 30, false));
    }


    @Test
    public void testSellSeatPosAlreadyOccupied() {
        assertTrue(travel.sellSeatPos(1, "123456789G", "Pepe Perez", 30, false));
        assertFalse(travel.sellSeatPos(1, "987654321B", "Maria Montes", 25, false));
    }

    @Test
    public void testSellSeatPosWithAdvanceSale() {
        assertTrue(travel.sellSeatPos(1, "123456789G", "Pepe Perez", 30, true));
    }

    @Test
    public void testSellSeatPosWithAdvanceSaleAndSameNIF() {
        assertTrue(travel.sellSeatPos(1, "123456789G", "Pepe Perez", 30, true));
        assertFalse(travel.sellSeatPos(2, "123456789G", "Maria Montes", 25, true));
    }

    @Test
    public void testSellSeatPosWithAdvanceSaleAndDifferentNIF() {
        assertTrue(travel.sellSeatPos(1, "123456789G", "Pepe Perez", 30, true));
        assertTrue(travel.sellSeatPos(2, "987654321B", "Maria Montes", 25, true));
    }


    @Test
    public void testSellSeatFrontPos() {
        assertEquals(1, travel.sellSeatFrontPos("524973646P", "Pepe Perez", 30, false));
        assertEquals(2, travel.sellSeatFrontPos("364795581N", "Maria Montes", 25, false));
        assertEquals(3, travel.sellSeatFrontPos("567890123I", "Alicia Fernandez", 40, false));
        assertEquals(4, travel.sellSeatFrontPos("417803910J", "Carlos Fidalgo", 50, false));
        assertEquals(5, travel.sellSeatFrontPos("663454765T", "Almudena Seco", 35, false));
        assertEquals(-1, travel.sellSeatFrontPos("572906954S", "Calos Morales", 60, false)); // No hay asientos disponibles
    }

    @Test
    public void testSellSeatRearPos() {
        assertEquals(5, travel.sellSeatRearPos("524973646P", "Pepe Perez", 30, false));
        assertEquals(4, travel.sellSeatRearPos("364795581N", "Maria Montes", 25, false));
        assertEquals(3, travel.sellSeatRearPos("567890123I", "Alicia Fernandez", 40, false));
        assertEquals(2, travel.sellSeatRearPos("562974293D", "Mario Guerra", 50, false));
        assertEquals(1, travel.sellSeatRearPos("663454765T", "Almudena Seco", 35, false));
        assertEquals(-1, travel.sellSeatRearPos("572906954S", "Calos Morales", 60, false)); // No hay asientos disponibles
    }


    @Test
    public void testGetSeatWithValidPosition() {
        // Venta de un asiento en la posición 3
        travel.sellSeatPos(3, "524973646P", "Pepe Perez", 30, false);
        
        // Verificar que el asiento devuelto en la posición 3 es correcto
        assertEquals("Pepe Perez", travel.getSeat(3).getHolder().getName());
    }

    @Test
    public void testGetSeatWithInvalidPosition() {
        // Verificar que el método devuelve null para una posición inválida (0)
        assertNull(travel.getSeat(0));
        
        // Verificar que el método devuelve null para una posición inválida (mayor que el número de asientos)
        assertNull(travel.getSeat(6));
    }


    @Test
    public void testGetSeatPrice_NullSeat() {
        Seat seat = null;
        assertEquals(0.0, travel.getSeatPrice(seat), 0.0);
    }

    @Test
    public void testMaxNumberConsecutiveSeatsNoSeatsSold() {
        assertEquals(5, travel.getMaxNumberConsecutiveSeats());
    }

    @Test
    public void testMaxNumberConsecutiveSeatsAllSeatsSold() {
        travel.sellSeatPos(1, "524973646P", "Pepe Perez", 30, false);
        travel.sellSeatPos(2, "364795581N", "Maria Montes", 25, false);
        travel.sellSeatPos(3, "567890123I", "Alicia Fernandez", 40, false);
        travel.sellSeatPos(4, "674168418M", "Bob Esponja", 35, false);
        travel.sellSeatPos(5, "666666666L", "Eva Fuentes", 50, false);
        
        // No hay asientos consecutivos disponibles
        assertEquals(0, travel.getMaxNumberConsecutiveSeats());
    }


    @Test
    public void testMaxNumberConsecutiveSeatsLastSeatSold() {
        travel.sellSeatPos(5, "661417111C", "Eva Fuentes", 50, false);
        
        // Solo el último asiento vendido, por lo que hay 4 asientos consecutivos disponibles
        assertEquals(4, travel.getMaxNumberConsecutiveSeats());
    }
    
	@Before
    public void setUp() {
        travel = new TravelArrayImpl(new Date(), 5);
    }


    @Test
    public void testGetAvailableSeatsList() {
        // Venta de asientos en las posiciones 2, 4 y 5
        travel.sellSeatPos(2, "524973646P", "Pepe Perez", 30, false);
        travel.sellSeatPos(4, "934581364X", "Lucia Liu", 25, false);
        travel.sellSeatPos(5, "555555555C", "Alana Sanchez", 40, false);

        // Obtener la lista de asientos disponibles
        List<Integer> availableSeats = travel.getAvailableSeatsList();

        // Verificar que la lista contiene las posiciones de los asientos disponibles
        assertTrue(availableSeats.contains(1));
        assertTrue(availableSeats.contains(3));
        assertFalse(availableSeats.contains(2));
        assertFalse(availableSeats.contains(4));
        assertFalse(availableSeats.contains(5));
    }

    @Test
    public void testGetAdvanceSaleSeatsList() {
        // Venta de asientos en las posiciones 2, 4 y 5
        travel.sellSeatPos(2, "524973646P", "Pepe Perez", 30, true);
        travel.sellSeatPos(4, "934581364X", "Lucia Liu", 25, true);
        travel.sellSeatPos(5, "555555555C", "Alana Sanchez", 40, false);

        // Obtener la lista de asientos de venta anticipada
        List<Integer> advanceSaleSeats = travel.getAdvanceSaleSeatsList();

        // Verificar que la lista contiene las posiciones de los asientos de venta anticipada
        assertTrue(advanceSaleSeats.contains(2));
        assertTrue(advanceSaleSeats.contains(4));
        assertFalse(advanceSaleSeats.contains(1));
        assertFalse(advanceSaleSeats.contains(3));
        assertFalse(advanceSaleSeats.contains(5));
    }

	@Test
public void testEventoNoVacio() throws Exception {
    e.sellSeatPos(1, "1010", "AA", 10, true);
    assertFalse(e.getNumberOfAvailableSeats() == 110);
}

@Test
public void testGetDiscountAdvanceSaleFalse() throws Exception {
    assertFalse(e.getDiscountAdvanceSale() != 25);
}

@Test
public void testgetCollectionSinVentas() throws Exception {
    assertTrue(e.getCollectionTravel() == 0.0);
}

// test de person 
@Test
    public void testConstructorAndGetters() {
        Person person = new Person("525465567L", "Raul Vega", 30);
        assertEquals("525465567L", person.getNif());
        assertEquals("Raul Vega", person.getName());
        assertEquals(30, person.getAge());
    }

    @Test
    public void testSetters() {
        Person person = new Person("525465567L", "Raul Vega", 30);
        person.setName("Julia Medina");
        person.setNif("718141059Y");
        person.setAge(25);
        assertEquals("718141059Y", person.getNif());
        assertEquals("Julia Medina", person.getName());
        assertEquals(25, person.getAge());
    }

    @Test
    public void testToString() {
        Person person = new Person("525465567L", "Raul Vega", 30);
        assertEquals("{ NIF: 525465567L  Name : Raul Vega, Age:30}", person.toString());
    }

    @Test
    public void testEquals_SameObject() {
        Person person = new Person("525465567L", "Raul Vega", 30);
        assertTrue(person.equals(person));
    }

    @Test
    public void testEquals_NullObject() {
        Person person = new Person("525465567L", "Raul Vega", 30);
        assertFalse(person.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        Person person = new Person("525465567L", "Raul Vega", 30);
        assertFalse(person.equals("Raul Vega"));
    }

    @Test
    public void testEquals_DifferentNif() {
        Person person1 = new Person("525465567L", "Raul Vega", 30);
        Person person2 = new Person("987654321Y", "Raul Vega", 30);
        assertFalse(person1.equals(person2));
    }

    @Test
    public void testEquals_SameNif() {
        Person person1 = new Person("525465567L", "Raul Vega", 30);
        Person person2 = new Person("525465567L", "Julia Medina", 25);
        assertTrue(person1.equals(person2));
    }

    @Test
    public void testEquals_DifferentObjects() {
        Person person1 = new Person("525465567L", "Raul Vega", 30);
        Person person2 = new Person("525465567L", "Raul Vega", 30);
        assertTrue(person1.equals(person2));
    }
}
    
	


   
   

