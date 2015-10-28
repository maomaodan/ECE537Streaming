
import java.net.DatagramPacket;  
import java.net.DatagramSocket;  
import java.net.InetAddress;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;  
import java.util.Random;
import java.io.*;

class server {  
	private static Logger logger = Logger.getLogger("server");
	public static void main(String[] args) throws IOException { 
		
		int index = 0;
		int frame = 0;
		//initialize connection, wait for client
		startLog();
		System.out.println("server started");
		DatagramSocket server = new DatagramSocket(5050);  
		byte[] recvBuf = new byte[100];  
		DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);  
		System.out.println("server receiving");
		server.receive(recvPacket);// wait for packet
		System.out.println("server received");
		String recvStr = new String(recvPacket.getData(), 0,  
				recvPacket.getLength());  
		System.out.println("server " + recvStr);
		logger.info(recvStr);
		int port = recvPacket.getPort();  
		InetAddress addr = recvPacket.getAddress(); 
		
		//create packet timestamp
        for (frame = 0;frame<=30000; frame++=frame+4)
        {
		Date curTime=new Date();
		int hour = curTime.getHours();
		int min = curTime.getMinutes();
		int seconds = curTime.getSeconds();
		String sendStr = frame+","+hour+":"+min+":"+seconds;

		
		//call get movie bytes
		byte[] strBuf;
		byte[] movieBuf;
		
		strBuf = sendStr.getBytes(); 
		movieBuf = readMovie(index);
		byte[] sendBuf = new byte[strBuf.length+movieBuf.length];
		System.arraycopy(strBuf,0 , sendBuf, 0, strBuf.length);
		System.arraycopy(movieBuf, 0, sendBuf, strBuf.length, movieBuf.length);
		index = index+4096;
		
		
		 
		
		 
		DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length,  
				addr, port);  
		System.out.println("server sending");
		server.send(sendPacket);// send packet
		System.out.println("server sent");
            
        }
		server.close();
		logger.info("server end");
	}
	public static void startLog()
	{ 
		FileHandler fh;  
		try {  

			// This block configure the logger with handler and formatter  
			fh = new FileHandler("server_log.log");  
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  

			// the following statement is used to log any messages  
			logger.info("server_log");  

		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}
	public static byte[] readMovie(int index)
	{
		String fileName = "movie";
		File mv = new File(fileName);
		byte[][] buffer = new byte[1024*30000];
		byte[] returnBuffer = new byte[1024];
		try
		{
			
			FileInputStream inputStream = new FileInputStream(fileName);
			inputStream.read(buffer);
		}catch (Exception e){}
		System.arraycopy(buffer, index, returnBuffer, 0, 1024);
		
		return returnBuffer;
	}
} 
