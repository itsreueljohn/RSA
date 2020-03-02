import java.math.BigInteger;
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

class Decrypt{

	static BigInteger two = new BigInteger("2");
	static int bitCount=1024;
	static int securityParameter=3;
	static BigInteger nval;
	static BigInteger eval;
	static BigInteger dval;

	static BigInteger ciphertextarr [];

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

	static void readInput()throws Exception{
		FileReader pub = new FileReader ("public_key.txt");
		FileReader ciph  = new FileReader ("ciphertext.txt");
		FileReader priv  = new FileReader ("private_key.txt");

		Scanner pubScanner = new Scanner (pub);
		Scanner ciphScanner = new Scanner (ciph);
		Scanner privScanner = new Scanner (priv);

		eval= new BigInteger(pubScanner.nextLine());
		nval= new BigInteger(pubScanner.nextLine());

		dval=new BigInteger(privScanner.nextLine());

		ciphertextarr = new BigInteger[Integer.parseInt(ciphScanner.nextLine())];
		String ciphertext= ciphScanner.nextLine();

		int index =0;
		for(int i=0;i<ciphertextarr.length;i++){
			ciphertextarr[i] = new BigInteger(ciphertext.substring(index,ciphertext.indexOf(",",index+1)));
			index=ciphertext.indexOf(",",index+1)+1;
		}

	}

	static String decrypt(){

		char []plaintext = new char[ciphertextarr.length];
		for(int i=0;i<ciphertextarr.length;i++){
			BigInteger j =modExp(ciphertextarr[i],dval,nval);
			plaintext[i]=(char)j.intValue();
		}

		return new String(plaintext);
	}

	static void writeOutput(String plaintext) throws Exception{
		FileWriter ff = new FileWriter("deciphertext.txt");
		ff.write(plaintext);
		ff.flush();
		ff.close();
	}

	public static void main(String[]args)throws Exception{
		readInput();
		writeOutput(decrypt());
		System.out.println("Successfully decrypted to deciphertext.txt!");
	}
}
