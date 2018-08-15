package game.core.nettyCore.proto.protostuff;

import java.io.InputStream;
import java.io.OutputStream;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;

public class ProtostuffSerialize implements IMessageSerialize {
	private static SchemaCache cachedSchema = SchemaCache.getInstance();
	private static Objenesis objenesis = new ObjenesisStd(true);
	// private boolean rpcDirect = false;
	//
	// public boolean isRpcDirect() {
	// return rpcDirect;
	// }
	//
	// public void setRpcDirect(boolean rpcDirect) {
	// this.rpcDirect = rpcDirect;
	// }

	@SuppressWarnings("unchecked")
	private static <T> Schema<T> getSchema(Class<T> cls) {
		return (Schema<T>) cachedSchema.get(cls);
	}

	public <T> T deserialize(InputStream input, Class<T> cls) {
		try {
			T message = objenesis.newInstance(cls);
			Schema<T> schema = getSchema(cls);
			ProtostuffIOUtil.mergeFrom(input, message, schema);
			return message;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void serialize(OutputStream output, Object object) {
		Class cls = (Class) object.getClass();
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try {
			Schema schema = getSchema(cls);
			ProtostuffIOUtil.writeTo(output, object, schema, buffer);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			buffer.clear();
		}
	}
}
