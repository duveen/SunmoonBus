package kr.o3selab.sunmoonbus.Model;

public class NetworkGroup {

    private String url;
    private int tabs;
    private int skip;

    public NetworkGroup(String url, int tabs, int skip) {
        this.url = url;
        this.tabs = tabs;
        this.skip = skip;
    }

    public String getUrl() {
        return url;
    }

    public int getTabs() {
        return tabs;
    }

    public int getSkip() {
        return skip;
    }
}
