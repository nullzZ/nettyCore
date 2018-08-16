package game.core.nettyCore.model;

public class MessageBuilder {

    private short cmd;
    private Object message;

    public MessageBuilder cmd(short cmd) {
        this.cmd = cmd;
        return this;
    }

    public MessageBuilder message(Object message) {
        this.message = message;
        return this;
    }

    public Message build() {
        checkState(cmd != 0, "cmd not defined!");
//        checkState(message != null, "message not defined!");
        Message ret = new Message(cmd, message);
        return ret;
    }

    public static void checkState(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }
}
