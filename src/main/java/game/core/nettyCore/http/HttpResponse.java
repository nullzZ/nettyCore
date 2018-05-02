package game.core.nettyCore.http;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class HttpResponse {
    @JSONField(serialize = false)
    private int ret;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }
}
