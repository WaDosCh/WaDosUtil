package ch.judos.generic.games.easymp.api;

import ch.judos.generic.games.easymp.msgs.Message;


/**
 * @since 22.05.2015
 * @author Julian Schelker
 */
public interface CommunicatorI {

	/**
	 * @return null if no message is available
	 */
	public Message receive();
	
	public void send(Object data,PlayerI to);
	
	/**
	 * @param data
	 * @param exclude players who shouldn't receive this object
	 */
	public void sendToAll(Object data, PlayerI ... exclude);

	/**
	 * @return a unique id for this client
	 */
	public String getClientId();
	
	
}
