package game.core.nettyCore.proto.protostuff;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.common.io.Closer;

import game.core.nettyCore.coder.IMessageProtocol;

public class ProtostuffProtocol implements IMessageProtocol {
	private static Closer closer = Closer.create();
	private ProtostuffSerializePool pool = ProtostuffSerializePool.getProtostuffPoolInstance();
	private boolean rpcDirect = false;

	public boolean isRpcDirect() {
		return rpcDirect;
	}

	public void setRpcDirect(boolean rpcDirect) {
		this.rpcDirect = rpcDirect;
	}

	@Override
	public byte[] encode(final Object message) throws IOException {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			closer.register(byteArrayOutputStream);
			ProtostuffSerialize protostuffSerialization = pool.borrow();
			protostuffSerialization.serialize(byteArrayOutputStream, message);
			byte[] body = byteArrayOutputStream.toByteArray();
			pool.restore(protostuffSerialization);
			return body;
		} finally {
			closer.close();
		}
	}

	@Override
	public <T> T decode(byte[] body, Class<T> clazz) throws IOException {
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
			closer.register(byteArrayInputStream);
			ProtostuffSerialize protostuffSerialization = pool.borrow();
			// protostuffSerialization.setRpcDirect(rpcDirect);
			T obj = protostuffSerialization.deserialize(byteArrayInputStream, clazz);
			pool.restore(protostuffSerialization);
			return obj;
		} finally {
			closer.close();
		}
	}

}
