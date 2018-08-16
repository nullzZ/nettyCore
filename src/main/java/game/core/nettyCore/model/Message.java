package game.core.nettyCore.model;

/**
 * @author nullzZ
 */
public class Message {

    private short cmd;
    private Object content;

    public Message(short cmd, Object message) {
        this.cmd = cmd;
        this.content = message;
    }

    public short getCmd() {
        return cmd;
    }

    public Object getContent() {
        return content;
    }

    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }
}
