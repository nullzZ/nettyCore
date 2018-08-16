package game.core.nettyCore.protobuf;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLProxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PBGenerateUtil {
    public static void main(String[] args) {
//        InputStream fis = PBGenerateUtil.class.getResourceAsStream("test.proto");
        try {
            InputStream fis = new FileInputStream("D:\\nettyCore\\src\\test\\resources\\test.proto");
            ProtobufIDLProxy.generateSource(fis, new File("D:\\test"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
