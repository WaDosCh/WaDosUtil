package ch.judos.generic.games.easymp;

/**
 * @since 31.05.2015
 * @author Julian Schelker
 */
public class ObjectId {

	private Type	type;
	private int		nr;
	private String	clientId;	// only set if this id is of type dynamic

	protected ObjectId(Type type, int nr, String clientId) {
		this.type = type;
		this.nr = nr;
		this.clientId = clientId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.clientId == null) ? 0 : this.clientId.hashCode());
		result = prime * result + this.nr;
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ObjectId))
			return false;
		ObjectId other = (ObjectId) obj;
		if (this.nr != other.nr)
			return false;
		if (this.type != other.type)
			return false;
		if (this.clientId == null) {
			if (other.clientId != null)
				return false;
		}
		else if (!this.clientId.equals(other.clientId))
			return false;
		return true;
	}

	enum Type {
		STATIC, DYNAMIC;
	}
}
