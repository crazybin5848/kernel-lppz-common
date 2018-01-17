package com.lpp.util.test.thrift;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class JavaClient {
  public static void main(String [] args) {
	  args=new String[]{"simple"};
    if (args.length != 1) {
      System.out.println("Please enter 'simple' or 'secure'");
      System.exit(0);
    }

    try {
      TTransport transport;
      if (args[0].contains("simple")) {
        transport = new TSocket("10.6.30.58", 9090);
        transport.open();
      }
      else {
        /*
         * Similar to the server, you can use the parameters to setup client parameters or
         * use the default settings. On the client side, you will need a TrustStore which
         * contains the trusted certificate along with the public key. 
         * For this example it's a self-signed cert. 
         */
        TSSLTransportParameters params = new TSSLTransportParameters();
        params.setTrustStore("../../lib/java/test/.truststore", "thrift", "SunX509", "JKS");
        /*
         * Get a client transport instead of a server transport. The connection is opened on
         * invocation of the factory method, no need to specifically call open()
         */
        transport = TSSLTransportFactory.getClientSocket("localhost", 9091, 0, params);
      }

      TProtocol protocol = new TBinaryProtocol(transport);
//      UserService.Client client =new UserService.Client(protocol);
//      client.add(new User("fuck1",null,false,(short) 11));
//      User u=client.get("fuck1");
		RedisMicroService.Client client=new RedisMicroService.Client(protocol);
		try {
			long num=client.getTableSequenceNo("nodefuck");
			System.out.println(num);
		} catch (TException e) {
			e.printStackTrace();
		}finally{
			transport.close();
		}
    } catch (TException x) {
      x.printStackTrace();
    } 
  }
}