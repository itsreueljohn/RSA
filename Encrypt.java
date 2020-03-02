import java.math.BigInteger;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

class Encrypt{

	static BigInteger two = new BigInteger("2");
	static int bitCount=1024;
	static int securityParameter=3;
	static BigInteger nval;
	static BigInteger eval;
	static BigInteger dval;
	static BigInteger cipherTextWrite[];
	static String plaintext="";


	static BigInteger modExp(BigInteger m, BigInteger e, BigInteger n){
		BigInteger r= e.mod(two).equals(BigInteger.ONE) ? m.mod(n):BigInteger.ONE;
		e= e.divide(two);

		while(e.compareTo(BigInteger.ZERO)>0){
			m=(m.multiply(m)).mod(n);

			if(e.mod(two).equals(BigInteger.ONE)) r= (r.multiply(m)).mod(n);
			e=e.divide(two);
		}
		return r;
	}

	static void encrypt(){

		BigInteger [] cipherText =new BigInteger[plaintext.length()];
		for(int i=0;i<plaintext.length();i++){
			cipherText[i]=BigInteger.valueOf((int)plaintext.charAt(i));
		}

		for(int i=0;i<cipherText.length;i++){
			cipherText[i] =modExp(cipherText[i],eval,nval);
		}
		cipherTextWrite= cipherText;

	}

	static void getInput()throws Exception{
		File keys = new File("public_key.txt");
		Scanner sc = new Scanner(keys);

		eval= new BigInteger(sc.nextLine());
		nval= new BigInteger(sc.nextLine());

		sc.close();

		File plaintextFile = new File("plaintext.txt");
		Scanner sf = new Scanner(plaintextFile);

		while(sf.hasNextLine()){
			plaintext+=sf.nextLine();
		}
		sf.close();
	}

	static void writeOutput() throws Exception{
		FileWriter ff = new FileWriter("ciphertext.txt");

		ff.write(cipherTextWrite.length+"");
		ff.write('\n');
		for(int i=0;i<cipherTextWrite.length;i++){
			ff.write(cipherTextWrite[i].toString());
			ff.write(",");
		}
		ff.flush();
		ff.close();
	}

	public static void main(String[]args)throws Exception{
		getInput();
		System.out.println("Encrypting :\n"+plaintext);
		encrypt();
		System.out.println("Encrypted!");
		writeOutput();
		System.out.println("Ciphertext written to ciphertext.txt");
	}
}
