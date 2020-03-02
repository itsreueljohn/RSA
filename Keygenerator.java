import java.math.BigInteger;
import java.util.Random;
import java.io.FileWriter;

class Keygenerator{

	static BigInteger two = new BigInteger("2");
	static int bitCount=1024;
	static int securityParameter=3;
	static BigInteger nval;
	static BigInteger eval;
	static BigInteger dval;

	static boolean millerRabin(BigInteger n, int t){

		Random randomGenerator = new Random();
		int s;

		if(n.equals(two)) return true;

		if(n.compareTo(two)==-1 || (n.mod(two)==BigInteger.ZERO)){
			return false;
		}

		BigInteger temp = n.subtract(BigInteger.ONE);

		BigInteger r =temp;

		for(s=0;r.mod(two).equals(BigInteger.ZERO);s++){
			r= r.divide(two);
		}

		for(int i=0;i<t;i++){
			BigInteger a= new BigInteger(bitCount,randomGenerator);
			a=a.mod(temp.subtract(BigInteger.ONE)).add(BigInteger.ONE);// 2<=a<=n-2
			BigInteger y= modExp(a,r,n);

			if((!y.equals(BigInteger.ONE)) && (!y.equals(temp))){

				for(int j=1; j<=s-1 && (!y.equals(temp));j++){
					y=modExp(y,two,n);
					if(y.equals(BigInteger.ONE)) return false;
				}
				if(!(y.equals(temp))) return false;
			}
		}
		return true;
	}

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

	static BigInteger generatePrime(){

		while(true){
			Random x = new Random();
			BigInteger a = new BigInteger(bitCount,x);
			if(a.mod(two).equals(BigInteger.ZERO)) a= a.add(BigInteger.ONE);

				if(millerRabin(a,securityParameter)){
					return a;
				}
		}
	}

	static BigInteger mulInv(BigInteger e, BigInteger n){
		BigInteger t= BigInteger.ONE;
		BigInteger ans= BigInteger.ZERO;

		BigInteger a = n;
		BigInteger q= e.divide(n);
		BigInteger r= e.mod(n);

		BigInteger temp;
		while(r.compareTo(BigInteger.ZERO)==1){
			temp= t.subtract(q.multiply(ans));
			t=ans;
			ans=temp;
			e=n;
			n=r;
			q=e.divide(n);
			r=e.mod(n);
		}
			ans=ans.mod(a);
			if(ans.compareTo(BigInteger.ZERO)==-1) ans =ans.add(a);
			return ans;
	}

	static void generateKeys(){
		BigInteger p = generatePrime();
		BigInteger q = generatePrime();

		BigInteger n = p.multiply(q);
		BigInteger phiN= p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

		BigInteger e;
		Random randomGenerator;
		while(true){
			randomGenerator = new  Random();
			e = new BigInteger(bitCount,randomGenerator);
			if(e.gcd(phiN).equals(BigInteger.ONE) && e.compareTo(BigInteger.ONE)==1) break;
		}

		BigInteger d;
		d=mulInv(e,phiN);
		eval=e;
		dval=d;
		nval=n;
		System.out.println("e is "+e.intValue());
		System.out.println("d is "+d.intValue());
		System.out.println("nval is "+nval.intValue());
	}

	public static void writeToFile() throws Exception{
		FileWriter pb = new FileWriter("public_key.txt");
		FileWriter pr = new FileWriter("private_key.txt");

		pb.write(eval.toString());
		pb.write('\n');
		pb.write(nval.toString());
		pb.flush();
		pb.close();

		pr.write(dval.toString());
		pr.flush();
		pr.close();

	}
	public static void main(String[]args) throws Exception{
		generateKeys();
		writeToFile();

	}
}
