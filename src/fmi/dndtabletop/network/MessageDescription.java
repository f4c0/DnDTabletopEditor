package fmi.dndtabletop.network;

public interface MessageDescription {

	public static final String CMD_TRANSFERT_BATTLEFIELD 	= "[A0]";
	public static final String CMD_SHUTDOWN 				= "[A1]";
	public static final String CMD_MOVE_CAM_LEFT 			= "[M1]";
	public static final String CMD_MOVE_CAM_RIGHT 			= "[M2]";
	public static final String CMD_MOVE_CAM_UP 				= "[M3]";
	public static final String CMD_MOVE_CAM_DOWN 			= "[M4]";
	public static final String CMD_PROJ_ORTHO 				= "[C1]";
	public static final String CMD_PROJ_PERSPECTIVE 		= "[C2]";
	public static final String CMD_POLYGON_MODE_SOLID		= "[P1]";
	public static final String CMD_POLYGON_MODE_WIRE       	= "[P2]";
}
