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
	
	private static MessageHandler m_instance;
	private boolean m_initialized;
	
	public enum Direction
	{
		LEFT,
		RIGHT,
		UP,
		DOWN
	};
	
	private MessageHandler()
	{
		m_initialized = false;
	}
	
	public static MessageHandler getInstance()
	{
		if(m_instance == null)
		{
			m_instance = new MessageHandler();
		}
		
		return m_instance;
	}
	
	public void sendRawMessage(String message) throws IOException, Exception
	{
		if(!m_initialized)
		{
			throw new Exception("MessageHandler not configured !");
		}
		
		this.outputFlow.print(message);
	}
	
	public void sendCmd_TransfertBattlefield(String message) throws IOException, Exception
	{
		sendRawMessage(MessageDescription.CMD_TRANSFERT_BATTLEFIELD+message);
	}
	
	public void sendCmd_Shutdown() throws IOException, Exception
	{
		sendRawMessage(MessageDescription.CMD_SHUTDOWN);
	}
	
	public void sendCmd_moveCam(Direction moveDir, int value) throws IOException, Exception
	{
		switch(moveDir)
		{
		case LEFT:
			sendRawMessage(MessageDescription.CMD_MOVE_CAM_LEFT+value);
			break;
		case RIGHT:
			sendRawMessage(MessageDescription.CMD_MOVE_CAM_RIGHT+value);
			break;
		case UP:
			sendRawMessage(MessageDescription.CMD_MOVE_CAM_UP+value);
		break;
		case DOWN:
			sendRawMessage(MessageDescription.CMD_MOVE_CAM_DOWN+value);
		break;
		default:
			throw new Exception("sendCmd_moveCam: Unknown Direction");		
		}
		
	}
	
	public void reconfigure(String host, int port) throws UnknownHostException, SocketException, IOException
	{
		this.m_host = host;
		this.m_port = port;
		this.m_serverIP = InetAddress.getByName(m_host);
		this.m_socket = new Socket(m_serverIP, m_port);
		this.outputFlow = new PrintStream(m_socket.getOutputStream());
		
		this.m_initialized = true;
	}
	
	public void close() throws IOException
	{
		if(this.m_initialized)
		{
			this.outputFlow.close();
			this.m_socket.close();	
		}		
	}

}
