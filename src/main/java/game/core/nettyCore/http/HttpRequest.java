package game.core.nettyCore.http;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class HttpRequest {
    @JSONField(serialize = false)
    private String domain;
    /**
     * header中的user-agent字段
     */
    @JSONField(serialize = false)
    private String ua;
    /**
     * header中的x-forwarded-for字段
     */
    @JSONField(serialize = false)
    private String ip;
    /**
     * header中的referer字段
     */
    @JSONField(serialize = false)
    private String referer;
    /**
     * 渠道
     */
    @JSONField(serialize = false)
    private String channelId;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
