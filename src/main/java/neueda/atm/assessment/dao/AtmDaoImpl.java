package neueda.atm.assessment.dao;
import neueda.atm.assessment.model.Atm;

import java.sql.*;
public class AtmDaoImpl {
	//Initializing and declaring the required variables.
    String sqlDriver=System.getenv("dbDriver");  
    Connection connection=null;
    String url=System.getenv("dbConnectionUrl");
    //String databaseName="atm"; 
    String userName=System.getenv("dbUserName");
    String password=System.getenv("dbPassword");
    ResultSet rs=null;
    PreparedStatement ps=null;
    String sqlBalanceUpdate=null;
	
	//Method to withdraw money from the account
	 public String withDrawl(Atm atm) throws SQLException{
		 		        
	        //Parsing the string value received from the jsp page.
	        int withdrawalAmount=Integer.parseInt(atm.getWithdrawlAmount());
	        int accountNumber=Integer.parseInt(atm.getAccountNumber());
	        int pin=Integer.parseInt(atm.getPin());     

	        try{
	            Class.forName(sqlDriver); //Registering the driver class
	            connection=DriverManager.getConnection(url,userName,password); //Connecting with MySQL database
	            Statement stmt=connection.createStatement();
	            String sql1="select opening_balance,over_draft from account where account_number=? and pin=?"; //Query for Account number and Pin number authentication
	            ps = connection.prepareStatement(sql1);
	            ps.setInt(1,accountNumber);
	            ps.setInt(2,pin);
	            rs = ps.executeQuery();
	            if(rs.next()) {	  //Checking whether there is any account with the mentioned account number and pin number          		               
	                    int openingBalance=rs.getInt(1);
	                    int overDraft=rs.getInt(2);
	                    
	                    //Checking whether there is sufficient balance in the account to be withdrawn     
	                    if (((openingBalance+overDraft)-withdrawalAmount>0)){ 
	                    	String atmBalanceCheck=atmBalanceUpdate(withdrawalAmount); //Calling atm method for money withdrawl 
	                    	 if(atmBalanceCheck!=null) {   //Checking whether there is sufficient balance in the ATM
	                    		 
	                    		 
	                    		 
	                    		 /*Checking whether there is sufficient balance in the Opening account to be withdrawn
	                    		  * and then accordingly if the balance is available then deducting
	                    		  the amount from the opening balance amount.  */	                    		 
	                    		 if(openingBalance-withdrawalAmount>0) {
	 	                            sqlBalanceUpdate = "Update account set opening_balance=? where account_number=?";
	 	                            ps = connection.prepareStatement(sqlBalanceUpdate);
	 	                            ps.setInt(1, openingBalance - withdrawalAmount);
	 	                            ps.setInt(2, accountNumber);
	 	                            ps.executeUpdate();
	 	                            return "Updated balance is "+(openingBalance-withdrawalAmount)+".  "+atmBalanceCheck;
	 	                        }
	                    		 
	                    		 
	                    		 
	                    		 
	                    		 /* If the balance is not sufficient in the opening balance account then checking
	                    		  * whether the amount can be withdrawn from overdraft and if the balance is withdrawn
	                    		  from the overdraft the accordingly updating the overdraft. */	                    		 	                    		  
	 	                        else if(openingBalance==0 && (overDraft-withdrawalAmount)>0){
	 	                            sqlBalanceUpdate = "Update account set over_draft=? where account_number=?";
	 	                            ps=connection.prepareStatement(sqlBalanceUpdate);
	 	                            ps.setInt(1,overDraft-withdrawalAmount);
	 	                            ps.setInt(2,accountNumber);
	 	                            ps.executeUpdate();
	 	                            return "Opening balance is 0 and overdraft balance is "+(overDraft-withdrawalAmount)+ ".  "+atmBalanceCheck;
	 	                        }
	                    		 
	                    		 
	                    		 /* If the balance is not sufficient in the opening account, then 
	                    		  * the balance is withdrawn both from the opening account
	                    		  and from overdraft. */
	 	                        else{
	 	                            int difference=withdrawalAmount-openingBalance;
	 	                            sqlBalanceUpdate="Update account set opening_balance=?,over_draft=? where account_number=?";
	 	                            ps=connection.prepareStatement(sqlBalanceUpdate);
	 	                            ps.setInt(1,0);
	 	                            ps.setInt(2,overDraft-difference);
	 	                            ps.setInt(3,accountNumber);
	 	                            ps.executeUpdate();
	 	                            return "Updated Opening account balance is 0 and over_draft balance is"+ (overDraft-difference)+".  "+atmBalanceCheck;

	 	                        }
	                    		 
	                    	 }
	                    	 else{
	                    		 return "Amount cannot be withdrawn.";
	 	                        
	 	                    }
	                    }                    
	                
	                else{
	                	return "Insufficient Balance in your account";
	                   
	                }

	            }
	            else{
	                return "Incorrect Account Number or Pin";
	            }
	            
	        }
	        catch(Exception e) {
	            System.out.println(e);
	        }
			return null;

	    }
	 
	 
	 
	 //Method to withdraw money from an ATM
	 public String atmBalanceUpdate(int withdrawalAmount) throws SQLException{
		 
	        int numberOfNotes[]=new int[4];
	        int noteAmount[]=new int[]{50,20,10,5};
	        int note[]=new int[4];
	       
	        try {
	        	
				Class.forName(sqlDriver);
				connection=DriverManager.getConnection(url,userName,password); //Connecting with MySQL database
	            Statement stmt=connection.createStatement();
	            rs=stmt.executeQuery("select * from atm_balance");  //Query to determine number of notes in the ATM Machine.
	            
	            //Fetching number of notes from the database and storing it in an array for further use. 
	            if(rs.next()){
	                for(int i=0;i<numberOfNotes.length;i++){
	                    numberOfNotes[i]=rs.getInt(i+2);
	                }
	            }
	            
	            int counter=0;
	            
	            //Fetching the minimum number of notes from an ATM
	            for(int j=0;j<numberOfNotes.length;j++) {
	                if (withdrawalAmount >= (noteAmount[j])) {
	                	System.out.print(withdrawalAmount);
	                    counter = withdrawalAmount / (noteAmount[j]);
	                    if (numberOfNotes[j]>=counter){
	                        note[j] = withdrawalAmount / (noteAmount[j]);
	                        withdrawalAmount = withdrawalAmount - note[j] * (noteAmount[j]);
	                        numberOfNotes[j] = numberOfNotes[j] - note[j];
	                    }

	                }
	            }
	            
	            //Updating the fetched notes into the database
	            if(withdrawalAmount==0){
	                sqlBalanceUpdate="Update atm_balance set fifty=?,twenty=?,ten=?,five=? where balance=?";
	                ps=connection.prepareStatement(sqlBalanceUpdate);
	                ps.setInt(1,numberOfNotes[0]);
	                ps.setInt(2,numberOfNotes[1]);
	                ps.setInt(3,numberOfNotes[2]);
	                ps.setInt(4,numberOfNotes[3]);
	                ps.setString(5, "atm");
	                ps.executeUpdate();
	                return "Number of Notes withdrawn from ATM of 50 are "+note[0]+", of 20 are "+note[1]+"," +
	                        " of 10 are "+note[2]+" and of 5 are "+note[3];
	            }
	            else{
	                return null;
	            }
	            
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        return null;
            
	        
	    }
	
	 //Method to determine balance in one's account
	 public String showBalance(Atm atm) throws SQLException {
	        int accountNumber=Integer.parseInt(atm.getAccountNumber());
	        int pin=Integer.parseInt(atm.getPin());
	        try {
				Class.forName(sqlDriver); //Registering the driver class
				connection=DriverManager.getConnection(url,userName,password); //Connecting with MySQL database
	            Statement stmt=connection.createStatement();
	            String sql1="select opening_balance,over_draft from account where account_number=? and pin=?"; //Query for Account number and Pin number authentication
	            ps = connection.prepareStatement(sql1);
	            ps.setInt(1,accountNumber);
	            ps.setInt(2,pin);
	            rs = ps.executeQuery();
	            if(rs.next()) {
	            	return "Opening Account Balance is "+rs.getInt(1)+" and Over Draft Balance is "+rs.getInt(2);
	            }
	            else {
	            	return "Invalid Account or Pin Number";
	            }
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
            
		return null;
		 
	 }

	 
	 
	 
}
