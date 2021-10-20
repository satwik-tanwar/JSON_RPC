import com.thetransactioncompany.jsonrpc2.*;
import com.thetransactioncompany.jsonrpc2.server.*;
import java.util.*;
import java.io.*;
import java.net.*;

class Server2 {

    public static class DuplicateHandler implements RequestHandler {

        // Reports the method names of the handled requests
        public String[] handledRequests() {
            return new String[]{"duplicate"};
        }
         // Processes the requests
         public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {
			
             if (req.getMethod().equals("duplicate")) {
                List<Object> input = (List)req.getPositionalParams();

                 for (int i=0;i<input.size();++i){
                    for (int j=i+1;j<input.size();++j){
                        if (input.get(i).equals(input.get(j))) input.set(j,0); 
                    }
                }

                int count = 0;
                for (int i=0;i<input.size();++i){
                    if (!input.get(i).equals(0)) count+=1;
                }

                Object[] output=new Object[count];
                for (int i=0;i<output.length;++i) output[i]=0;

                for (int i=0;i<input.size();++i){
                    for(int j=0;j<output.length;++j){
                        if (!input.get(i).equals(0) && output[j].equals(0)) {
                            output[j]=input.get(i);
                            break;
                        }
                        
                    }
                } 
                
	            return new JSONRPC2Response(output, req.getID());
            }
            else {
                return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req.getID());
	    }
        }
    }


	public static void main(String args[]){
		try
        {
            Dispatcher dispatcher =  new Dispatcher();
            dispatcher.register(new DuplicateHandler());

            // Create server Socket
            ServerSocket ss = new ServerSocket(888);

            // connect it to client socket
            Socket s = ss.accept();
            System.out.println("\nConnection established");

            // to send data to the client
            PrintStream ps= new PrintStream(s.getOutputStream());

            // to read data coming from the client
            BufferedReader br= new BufferedReader(new InputStreamReader(s.getInputStream()));

            // server executes continuously
            while (true) {

                String str;
                JSONRPC2Request req;
                
            
                // repeat as long as the client does not send Exit.
                while (!(str=br.readLine()).equals("Exit")){

                    try {
                        req = JSONRPC2Request.parse(str);
                    } catch (JSONRPC2ParseException e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    System.out.println();
                    System.out.println("Request: \n" + req);
                    
                    JSONRPC2Response resp = dispatcher.process(req, null);
                    System.out.println("\n");
                    System.out.println("Response: \n" + resp);
                    System.out.println();
                    String jsonString = resp.toString();
                    
                    ps.println(jsonString);
                }

                // close connection
                ps.close();
                br.close();
                ss.close();
                s.close();

                // terminate application
                System.exit(0);

            } 
	    }
            catch(IOException e){
            e.printStackTrace();
        }
    }
}
