package neueda.atm.assessment.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import neueda.atm.assessment.model.Atm;

class WithDrawalTest {
	
	//Test case of Account Number and Pin authentication
	@Test
	void testWithDrawl() throws SQLException {
		Atm atm=new Atm();
		atm.setAccountNumber("1234");
		atm.setPin("12");
		atm.setWithdrawlAmount("100");
		AtmDaoImpl atmDao=new AtmDaoImpl();			
		assertEquals("Incorrect Account Number or Pin",atmDao.withDrawl(atm));
				
	}
	
	//Test case to to check - cannot dispense more money than it holds
	@Test
	void testWithDrawl1() throws SQLException {
		Atm atm=new Atm();
		atm.setAccountNumber("123456789");
		atm.setPin("1234");
		atm.setWithdrawlAmount("10000");
		AtmDaoImpl atmDao=new AtmDaoImpl();	
		assertEquals("Insufficient Balance in your account",atmDao.withDrawl(atm));				
	}
	
	/* Test case to check if the money s not present
	 * in opening balance account then the money can
	  be withdrawn from overdraft as per OverDraft limit */ 
	@Test
	void testWithDrawl2() throws SQLException {
		Atm atm=new Atm();
		atm.setAccountNumber("123456789");
		atm.setPin("1234");
		atm.setWithdrawlAmount("10");
		AtmDaoImpl atmDao=new AtmDaoImpl();		
		assertEquals("Opening balance is 0 and overdraft balance is 20.  Number of Notes withdrawn from ATM of 50 are 0, of 20 are 0, of 10 are 1 and of 5 are 0",atmDao.withDrawl(atm));
				
	}
	
	/*Test case to whether there is sufficient balance in the Opening account to be withdrawn
	  * and then accordingly if the balance is available then deducting
	  the amount from the opening balance amount.  */
	@Test
	void testWithDrawl4() throws SQLException {
		Atm atm=new Atm();
		atm.setAccountNumber("987654321");
		atm.setPin("4321");
		atm.setWithdrawlAmount("50");
		AtmDaoImpl atmDao=new AtmDaoImpl();	
		assertEquals("Updated balance is 1015.  Number of Notes withdrawn from ATM of 50 are 0, of 20 are 2, of 10 are 1 and of 5 are 0",atmDao.withDrawl(atm));
				
	}
	/* Test cases to make sure that the
	 * only that much amount which is a
	 multiple of 5 is withdrawn from the account*/
	@Test
	void testWithDrawl5() throws SQLException {
		Atm atm=new Atm();
		atm.setAccountNumber("987654321");
		atm.setPin("4321");
		atm.setWithdrawlAmount("71");
		AtmDaoImpl atmDao=new AtmDaoImpl();	
		assertEquals("Amount cannot be withdrawn.",atmDao.withDrawl(atm));
				
	}
	/* Test case to check that the amount is debited
	 * only when sufficient amount is available
	in the ATM Machine */
	@Test
	void testAtmBalanceUpdate() throws SQLException {
		AtmDaoImpl atmDao=new AtmDaoImpl();
		assertEquals(null,atmDao.atmBalanceUpdate(5000));				
	}
	/* Test case to make sure the
	 * amount is debited from an ATM machine
	 when the respective notes are available*/
	@Test
	void testAtmBalanceUpdate1() throws SQLException {
		AtmDaoImpl atmDao=new AtmDaoImpl();
		assertEquals(null,atmDao.atmBalanceUpdate(501));				
	}
	
	
	// Test case to make sure that minimum number of notes are debited from an ATM Machine
	
	@Test
	void testAtmBalanceUpdate2() throws SQLException {
		AtmDaoImpl atmDao=new AtmDaoImpl();
		assertEquals("Number of Notes withdrawn from ATM of 50 are 0, of 20 are 0, of 10 are 1 and of 5 are 1",atmDao.atmBalanceUpdate(15));				
	}
	

}
