package neueda.atm.assessment.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import neueda.atm.assessment.dao.AtmDaoImpl;
import neueda.atm.assessment.model.Atm;

/**
 * Servlet implementation class AtmServlet
 */
@WebServlet("/atm")
public class AtmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private AtmDaoImpl atmDaoImpl=new AtmDaoImpl();
    public AtmServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Code for redirecting to the Main page
		response.getWriter().append("Served at: ").append(request.getContextPath());
		RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/views/atmMachine.jsp");
		dispatcher.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Getting values from the jsp page
		String accountNumber=request.getParameter("accountNumber");
		String pin=request.getParameter("pin");
		String withdrawlAmount=request.getParameter("withdrawlAmount");
		Atm atm=new Atm();
		atm.setAccountNumber(accountNumber);
		atm.setPin(pin);
		String output = null;
		System.out.print("Withdrawl Amount is "+withdrawlAmount);
		
		//Condition executed when the customer chooses the option to withdraw balance
		if(withdrawlAmount!="") {
			atm.setWithdrawlAmount(withdrawlAmount);					
			try {
				output = atmDaoImpl.withDrawl(atm);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();			
		}		
		}
		
		//Condition executed when customer chooses the option to determine balance
		else {
			atm.setWithdrawlAmount(null);
			try {
				output=atmDaoImpl.showBalance(atm);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Setting output received from dao class to the jsp page
		request.setAttribute("output", output);
		RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/views/MessageDisplay.jsp");
		dispatcher.forward(request, response);
		
		
	}

}
