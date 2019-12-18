package info.util;

public class Filters {

	public static Boolean isStringValid(String string){
		return string.matches("\\A\\p{ASCII}*\\z");
	}
	
}
