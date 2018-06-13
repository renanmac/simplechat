import java.util.*;
import java.io.*;

public class Chat implements Serializable{
	String name;
	String username;
	String dest;
	String text;
    Set<String> setOnline = new HashSet<String>();
    Action action;

    public enum Action {
        CONNECT, DISCONNECT, SEND_ONE, SEND_ALL, USERS_ONLINE
    }

}