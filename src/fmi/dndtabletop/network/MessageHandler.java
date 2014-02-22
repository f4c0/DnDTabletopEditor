package fmi.dndtabletop.network;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MessageHandler {

	private String m_host;
	private int m_port;
	private InetAddress m_serverIP;
	private Socket m_socket;
	
	private PrintStream outputFlow;
	
	public MessageHandler(String host, int port) throws UnknownHostException, SocketException, IOException
	{
		this.m_host = host;
		this.m_port = port;
		this.m_serverIP = InetAddress.getByName(m_host);
		this.m_socket = new Socket(m_serverIP, m_port);
		
		this.outputFlow = new PrintStream(m_socket.getOutputStream());
	}	
	
	public void sendRawMessage(String message) throws IOException
	{
		this.outputFlow.print(message);
	}
	
	public void reconfigure(String host, int port) throws UnknownHostException, SocketException, IOException
	{
		this.m_host = host;
		this.m_port = port;
		this.m_serverIP = InetAddress.getByName(m_host);
		this.m_socket = new Socket(m_serverIP, m_port);
		this.outputFlow = new PrintStream(m_socket.getOutputStream());
	}
	
	public void close() throws IOException
	{
		this.outputFlow.close();
		this.m_socket.close();	
	}

}
