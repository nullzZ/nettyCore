package game.core.nettyCore.proto.protostuff;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IMessageSerialize {

	void serialize(OutputStream output, Object object) throws IOException;

	<T> T deserialize(InputStream input, Class<T> cls) throws IOException;
}
