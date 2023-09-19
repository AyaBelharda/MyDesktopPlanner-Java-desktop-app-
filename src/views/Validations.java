package views;

import java.time.LocalDate;
import java.time.LocalTime;

public class Validations {
	//used to perform tests on data
	public boolean validUsername(String userName) {
		if(userName == null) {
			return false;
		}
		return userName.length() > 5;
	}
	
	public boolean validPasWord(String passWord) {
		if(passWord == null) {
			return false;
		}
		return passWord.length() > 7 && !passWord.equals(passWord.toLowerCase());
	}
	
	public boolean validResponse(String rep) {
		if(rep == null) {
			return false;
		}
		return rep.length() > 0;
	}
	
	public boolean validDay(LocalDate date) {
		
		return LocalDate.now().isBefore(date);
	}
	
	public boolean validHour(String hour, String min) {
		 int debH;
		 try {
			 debH = Integer.parseInt(hour);
		 } catch(Exception e ) {
			return false;
		 }
		 
		 int debM;
		 try {
			 debM = Integer.parseInt(min);
		 } catch(Exception e ) {
			 return false;
		 }
		 
		 try {
			 LocalTime.of(debH, debM);
		 } catch(Exception e ) {
			 return false;
		 } 
		 
		 return true;
	}
	
	public boolean validCreneau(LocalTime time1, LocalTime time2) {
		return time1.isBefore(time2);
	}
}
