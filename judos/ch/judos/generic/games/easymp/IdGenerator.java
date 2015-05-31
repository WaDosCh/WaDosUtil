package ch.judos.generic.games.easymp;

import ch.judos.generic.games.easymp.ObjectId.Type;

/**
 * @since 31.05.2015
 * @author Julian Schelker
 */
public class IdGenerator {

	private int		currentStaticNr	= 0;
	private int		currentDynamicNr	= 0;
	private String	clientId;

	public IdGenerator(String clientId) {
		this.clientId = clientId;
	}

	public ObjectId generateStaticId() {
		return new ObjectId(Type.STATIC, this.currentStaticNr++, null);
	}

	public ObjectId generateDynamicId() {
		return new ObjectId(Type.DYNAMIC, this.currentDynamicNr++, this.clientId);
	}

	public ObjectId[] getAllStaticIds() {
		ObjectId[] result = new ObjectId[this.currentStaticNr];
		for(int i=0;i<result.length;i++)
			result[i] = new ObjectId(Type.STATIC,i,null);
		return result;
	}

}
