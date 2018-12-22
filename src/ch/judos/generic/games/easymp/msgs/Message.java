package ch.judos.generic.games.easymp.msgs;

import ch.judos.generic.games.easymp.api.PlayerI;

/**
 * @since 22.05.2015
 * @author Julian Schelker
 */
public class Message {

	public UpdateMsg data;
	public PlayerI source;

	public Message(PlayerI from, UpdateMsg data) {
		this.source = from;
		this.data = data;
	}

}
