import static org.junit.Assert.*;

import org.junit.Test;

public class MarketTest {

	@Test
	public void getTraderTest() {
		Market test = new Market();
		for (int i=0; i < test.traders.length; i++){
			AssertTrue(test.traders[i]!=null);
		}
	}
	
	private void AssertTrue(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void cycleTest() {
		AssertTrue(Market.cycleNo < 7168); //approx max number of cycles
	}
	
	@Test
	public void updateMarketTest() {
		
	}
	
	@Test
	public void requestOfferTest() {
		
	}
	
	@Test 
	public void updateTimeTest(){
		
	}
}