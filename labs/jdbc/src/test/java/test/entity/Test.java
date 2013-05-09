package test.entity;

public class Test {

	public static void main(String[] args){
		String dtype = "SELECT 'C'||(SELECT CAST (COUNT(*) AS Char) FROM AD_Client)||'U'||";
		String ttype = dtype.replaceAll("\\|\\|", "+");
		System.out.println(ttype);
		ttype = dtype.replace("||", "+");
		System.out.println(ttype);
	}
}
