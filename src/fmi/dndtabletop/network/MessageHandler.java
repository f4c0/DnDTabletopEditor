package fmi.dndtabletop.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MessageHandler {

	private String m_host;
	private int m_port;
	private InetAddress m_serverIP;
	private DatagramSocket m_socket;
	
	public MessageHandler(String host, int port) throws UnknownHostException, SocketException
	{
		this.m_host = host;
		this.m_port = port;
		this.m_serverIP = InetAddress.getByName(m_host);
		this.m_socket = new DatagramSocket();
	}	
	
	public void sendRawMessage(String message) throws IOException
	{
		DatagramPacket dataP = new DatagramPacket(message.getBytes(),message.length(), this.m_serverIP, this.m_port); 
		this.m_socket.send(dataP);
	}
	
	public void reconfigure(String host, int port) throws UnknownHostException, SocketException
	{
		this.m_host = host;
		this.m_port = port;
		this.m_serverIP = InetAddress.getByName(m_host);
	}
	
	public void close()
	{
		this.m_socket.close();	
	}

}
