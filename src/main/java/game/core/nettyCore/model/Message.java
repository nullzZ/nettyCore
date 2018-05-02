package game.core.nettyCore.model;

/**
 * @author nullzZ
 */
public class Message {

    private int cmd;
    private Object content;

    public Message(int cmd, Object message) {
        this.cmd = cmd;
        this.content = message;
    }

    public int getCmd() {
        return cmd;
    }

    public Object getContent() {
        return content;
    }

    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }
}
