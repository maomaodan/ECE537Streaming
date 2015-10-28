
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
        ArrayList<String> feedbacks = new ArrayList<String>();
        byte[] recvBuf = new byte[1400];
        DatagramPacket recvPacket;
        String recvStr;
        for(int i=0;i<numServer;i++)
        {
            recvBuf = new byte[100];
            recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
            System.out.println("client receiving");
            client.receive(recvPacket);// receive packet
            System.out.println("client received");
            recvStr = new String(recvPacket.getData(), 0,recvPacket.getLength());
            serNum = Character.getNumericValue(recvStr.charAt(0));
            feedbacks.add(serNum,recvStr);
            //System.out.println("client " + recvStr);
            logger.info(recvStr);
        }
        client.close();
        logger.info("client end");
        for(int i=0;i<feedbacks.size();i++)
        {
            System.out.println("array["+i+"]: "+feedbacks.get(i));
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