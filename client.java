
import java.io.IOException;  
import java.net.DatagramPacket;  
import java.net.DatagramSocket;  
import java.net.InetAddress;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;  
import java.util.ArrayList;
  
class client {  
	private static Logger logger = Logger.getLogger("client");
    public static void main(String[] args) throws IOException { 
    	startLog();
    	System.out.println("client started");
        DatagramSocket client = new DatagramSocket();  
  
        Date curTime=new Date();
    	int hour = curTime.getHours()+1;
    	int min = curTime.getMinutes();
        String sendStr = "Kefei FU,MeatCushion3D,"+hour+":"+min;
        int numServer = 4;
        int stop=0;
        ArrayList<ArrayList<String>> servers = new ArrayList<ArrayList<String>>();
        ArrayList<String> ser1 = new ArrayList<String>();
        ArrayList<String> ser2 = new ArrayList<String>();
        ArrayList<String> ser3 = new ArrayList<String>();
        ArrayList<String> ser4 = new ArrayList<String>();
        servers.add(ser1);
        servers.add(ser2);
        servers.add(ser3);
        servers.add(ser4);
        
        //start sending
        byte[] sendBuf;
        sendBuf = sendStr.getBytes();
        int port = 5050;
        DatagramPacket sendPacket;
        InetAddress addr;
        int serNum;
        for(int i=0;i<numServer;i++)
        {
            serNum = i+1;
            addr = InetAddress.getByName("10.10."+serNum+".1");
            sendPacket = new DatagramPacket(sendBuf, sendBuf.length,addr, port);
            System.out.println("client sending server"+i);
            client.send(sendPacket);// send packet
            System.out.println("client sent server"+i);
        }
        
        //start recieving
        byte[] recvBuf = new byte[1400];
        DatagramPacket recvPacket;
        String recvStr;
        while(stop==0)
        {
            recvBuf = new byte[100];
            recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
            System.out.println("client receiving");
            client.receive(recvPacket);// receive packet
            System.out.println("client received");
            recvStr = new String(recvPacket.getData(), 0,recvPacket.getLength());
            serNum = Integer.parseInt(recvStr.substring(0,4));
            servers.get(serNum%4).add(serNum/4,recvStr);
            if(serNum==35000)
            {
                stop=1;
            }
            //System.out.println("client " + recvStr);
            logger.info(recvStr);
        }
        
        client.close();
        logger.info("client end");
        for(int i=0;i<servers.size();i++)
        {
            System.out.println("array["+i+"]: "+servers.get(i).get(0));
            for(int k=1;k<servers.get(i).size();k++)
            {
                System.out.print(", "+servers.get(i).get(k));
            }
                
        }
        
        
    }  
    public static void startLog()
    { 
        FileHandler fh;  
        try {  

            // This block configure the logger with handler and formatter  
            fh = new FileHandler("client_log.log");  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  

            // the following statement is used to log any messages  
            logger.info("client_log");  

        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
}  