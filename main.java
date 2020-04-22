import java.io.*;
import java.util.*;

public class main{
	public static void main(String[] args){
		System.out.println("Java based MOLPRO energy finder. Please make sure atomic units are used.");
		String jobn;
		int state;
		int col;
		Scanner s = new Scanner(System.in);								//Use scanner to read input
		findE fe = null;												//Find E object
		String key;
		if(args.length==0){												//Different amount of input arguments
			System.out.println("Please enter the job name: ");
			jobn=s.next();
			System.out.println("Please select state: ");
			state = s.nextInt();
			System.out.println("Please select collumn: ");
			col=s.nextInt();
			fe = new findE(jobn,state,col);
			try{
				fe.read();
			}catch(mpErrException mperr){								//Thrown if error is found.
				mperr.printStackTrace();
				return;
			}
			fe.write();
			System.out.println("Enter key to get value.");
			key=s.next();												//Read out the PES HashMap
			s.close();
			try{
				System.out.println(fe.getE(key));
			}catch(NullPointerException npe){
				System.out.println("ERROR! Invalid key!");
				npe.printStackTrace();
			}
		}
		
		else if(args.length==1){										//Repeat code above
			System.out.println("Job name set to: \""+args[0]+"\"");
			jobn=args[0];
			System.out.println("Please select state: ");
			state = s.nextInt();
			System.out.println("Please select collumn: ");
			col=s.nextInt();
			fe = new findE(jobn,state,col);
			try{
				fe.read();
			}catch(mpErrException mperr){
				mperr.printStackTrace();
				return;
			}
			fe.write();
			System.out.println("Enter key to get value.");
			key=s.next();
			s.close();
			try{
				System.out.println(fe.getE(key));
			}catch(NullPointerException npe){
				System.out.println("ERROR! Invalid key!");
				npe.printStackTrace();
			}
		}
		
		else if(args.length==2){
			System.out.println("Job name set to: \""+args[0]+"\"");
			jobn=args[0];
			System.out.println("Selected state: "+args[1]);
			System.out.println("Please select collumn: ");
			col=s.nextInt();
			fe = new findE(jobn,Integer.parseInt(args[1]),col);
			try{
				fe.read();
			}catch(mpErrException mperr){
				mperr.printStackTrace();
				return;
			}
			fe.write();
			System.out.println("Enter key to get value.");
			key=s.next();
			s.close();
			try{
				System.out.println(fe.getE(key));
			}catch(NullPointerException npe){
				System.out.println("ERROR! Invalid key!");
				npe.printStackTrace();
			}
		}
		
		else if(args.length==3){
			System.out.println("Job name set to: \""+args[0]+"\"");
			jobn=args[0];
			System.out.println("Selected state: "+args[1]);
			System.out.println("Selected collumn: "+args[2]);
			fe = new findE(jobn,Integer.parseInt(args[1]),Integer.parseInt(args[2]));
			try{
				fe.read();
			}catch(mpErrException mperr){
				mperr.printStackTrace();
				return;
			}
			fe.write();
			System.out.println("Enter key to get value.");
			key=s.next();
			s.close();
			try{
				System.out.println(fe.getE(key));
			}catch(NullPointerException npe){
				System.out.println("ERROR! Invalid key!");
				npe.printStackTrace();
			}
		}
		
		else{
			System.out.println("\u001B[1;31mError!\u001B[0m Wrong number of input arguments.");
			return;
		}
	}
}
