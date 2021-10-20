import com.thetransactioncompany.jsonrpc2.client.*;
import com.thetransactioncompany.jsonrpc2.*;
import java.io.*;
import java.net.*;
import java.util.*;

class Client2 {

	public static void main(String args[])
		throws Exception
	{
		
		// Create client socket
		Socket s = new Socket("localhost", 888);
		// to send data to the server
		DataOutputStream dos= new DataOutputStream(s.getOutputStream());
		// to read data coming from the server
		BufferedReader br= new BufferedReader(new InputStreamReader(s.getInputStream()));

        String method = "duplicate";
		Scanner inp = new Scanner(System.in);

		while(true){

			System.out.println();
			System.out.println("1. Integer Array \n2. String Array \n3. Exit");
			System.out.print("Enter your choice: ");
			int choice = inp.nextInt();
			System.out.println();

			String outString="";

			if (choice == 1){
				System.out.print("Enter the number of elements in Array: ");
				int n = inp.nextInt();
				int[] arr = new int[n];
				for (int i=0;i<n;++i){
					System.out.print("Enter element "+(i+1)+": ");
					arr[i]=inp.nextInt();
				}
				String requestID = "Integer";
				List<Object> echoParam = new LinkedList();
				for (int i=0;i<arr.length;++i)
				echoParam.add(arr[i]);
				JSONRPC2Request request = new JSONRPC2Request(method, echoParam, requestID);
				outString = request.toString();

			}

			if (choice == 2){
				System.out.print("Enter the number of elements in Array: ");
				int n = inp.nextInt();
				String[] arr = new String[n];
				for (int i=0;i<n;++i){
					System.out.print("Enter element"+(i+1)+": ");
					arr[i]=inp.next();
				}
				String requestID = "String";
				List<Object> echoParam = new LinkedList();
				for (int i=0;i<arr.length;++i)
				echoParam.add(arr[i]);
				JSONRPC2Request request = new JSONRPC2Request(method, echoParam, requestID);
				outString = request.toString();
			}

			if (choice == 3){
				outString="Exit";
				dos.writeBytes(outString+ "\n");
				break;
			}

			System.out.println("\n");
			System.out.println("CLIENT INPUT JSON (REQUEST): ");
			System.out.println(outString);
			System.out.println();

			//send to the server
			dos.writeBytes(outString+ "\n");

			// receive from the server
			String instr = br.readLine();

			System.out.println();
			System.out.println("CLIENT OUTPUT JSON (RESPONSE): ");
			System.out.println(instr);
		}

		// close connection.
		dos.close();
		br.close();
		s.close();
		
	}
}