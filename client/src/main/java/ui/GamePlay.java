package ui;

import net.ServerFacade;

public class GamePlay {
    private final String serverUrl;
    private ServerFacade serverFacade;

    public GamePlay(String serverUrl) {
        this.serverUrl = serverUrl;
        this.serverFacade = new ServerFacade(serverUrl);
    }

}
