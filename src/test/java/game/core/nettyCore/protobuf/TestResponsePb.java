package game.core.nettyCore.protobuf;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * @author nullzZ
 *
 */

public class TestResponsePb {
	@Protobuf(fieldType=FieldType.INT32, order=1, required=false)
	public int id;

}
