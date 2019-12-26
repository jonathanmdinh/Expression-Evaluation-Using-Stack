import java.util.*;
import java.math.*;
import java.io.*;


public class StackExpressionEvaluation {
	public static String evaluate(String expression) 
	{ 
		char[] exprssn = expression.toCharArray(); //Returns an Array of chars after converting a String into sequence of characters.
		Stack<Double> val = new Stack<Double>(); //Stack for Double values (for decimals)
		Stack<Character> ops = new Stack<Character>(); //Stack for Characters.
		boolean isNegative = false; //negative flag
		
		
		for (int i = 0; i < exprssn.length; i++) //goes through each character in the inputted string
		{ 
			if (exprssn[i] == ' ') //ignores whitespace
				continue; 
			
			if((i==0 && exprssn[0]=='-') || (i == 1 && exprssn[1] == '-')) //if there's a negative in the first or second place in string.
			{
				isNegative = true;
				continue;
			}

			if (exprssn[i] >= '0' && exprssn[i] <= '9') //if character isa digit
			{
				StringBuffer sbuffer = new StringBuffer(); 
				while (i < exprssn.length && ((exprssn[i] >= '0' && exprssn[i] <= '9') || (exprssn[i] == '.'))) //append characters until it reaches a character that's not a digit or decimal
					sbuffer.append(exprssn[i++]); 
				i--;
				if (isNegative == true) 
				{
					val.push(Double.parseDouble(sbuffer.toString()) * -1); //implement the negative flag
					isNegative = false;
				}
				else val.push(Double.parseDouble(sbuffer.toString()));
			} 
			
			if(exprssn[i]== '-' && !ops.isEmpty() && (exprssn[i-1]==ops.peek() || (exprssn[i-1] == ' ' && exprssn[i-2]==ops.peek()))) 
			{
				isNegative=true;
				continue;
			}

			else if (exprssn[i] == '(') 
				ops.push(exprssn[i]); 
			

			else if (exprssn[i] == ')') //Solve for the operations in parentheses until an open parentheses in operator stack is reached, meaning everything in parentheses is calculated
			{ 
				while (ops.peek() != '(') 
					val.push(evaluate(ops.pop(), val.pop(), val.pop())); //results will be pushed into value stack until last number is the answer.
				ops.pop(); 
			} 

		
			else if (exprssn[i] == '+' || exprssn[i] == '-' || exprssn[i] == '*' || exprssn[i] == '/') 
			{
				while (!ops.empty() && prec(exprssn[i], ops.peek()))  //Check for precendence. If 'i' has less precedence than the top of ops stack, solve until it doesn't.
					val.push(evaluate(ops.pop(), val.pop(), val.pop())); //Finishes the conversion from infix notation to to postfix notation.
				    ops.push(exprssn[i]); 
			} 
		} 

		while (!ops.empty()) //Solve and pop ops and vals until operations stack is empty
			val.push(evaluate(ops.pop(), val.pop(), val.pop()));
			String result = String.valueOf(round(val.pop(), 2));
       		return result; //Last number in value stack is the answer.
	} 

	public static double round(double value, int places) { //function that rounds decimals to two decimal places.
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
    public static boolean prec(char op1, char op2) //Compares 'i' with top of ops stack. Returns true if 'i' has less precedence than the top of ops stack. 
	{ 
		if (op1 == '(' || op2 == '(')
			return false; 
		if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) 
			return false; 
		else
			return true; 
	} 

    public static double evaluate(char op, double b, double a) 
    {
        double result = 0; 
		if (op == '+') { 
            result = a + b; 
        }
		if (op == '-') {
            result = a - b;
        }
		if (op == '*') {
			result = a * b;
        } 
		if (op == '/') {
			if (b == 0) throw new UnsupportedOperationException("Cannot divide by zero");
            result = a / b;    
        }  
        return result;
	}   
	public static void main(String[] args) throws Exception
	{ 
		
		  String stroutput="";
		  String ans="";
		  try(BufferedReader br= new BufferedReader(new FileReader("inputfile.txt"))){
			  String line=br.readLine();
			  while(line !=null) {
				  stroutput += line + " = "; //add equals sign to string
				  ans=evaluate(line); //string equal to the answer
				  stroutput += ans+"\n"; //goto next line
				  ans=""; //reset answer for the next line
				  line= br.readLine();
			  }
		  }
		  catch(Exception e) {
			  
		  }
		  try(BufferedWriter bw= new BufferedWriter(new FileWriter("project1_output.txt"))){
			  bw.write(stroutput);
			  System.out.println(stroutput);
		  }
		  catch (Exception e) {}
  
	}
  
}

//HOWTO recognize and evaluate negative numbers.
//unary negative has an operator before it, or will be the first (non-space) character in the string.
//set boolean flag. If a negative sign appears, boolean negative = true.
//if true, make digit negative
