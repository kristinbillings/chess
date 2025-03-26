package client;

import dataaccess.ResponseException;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import requests.*;
import requests.LogoutRequest;
import requests.RegisterRequest;
import results.GameResult;
import server.Server;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static String serverUrl;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverUrl = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverUrl);
    }

    @AfterAll
    static void stopServer() throws ResponseException {
        server.stop();
    }

    @BeforeEach
    public void clearDatabase() throws ResponseException {
        serverFacade.clear();
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void registerPosTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    void registerNegTest() throws Exception {
        Assertions.assertThrows(ResponseException.class, () -> {
            var authData = serverFacade.register(new RegisterRequest("bob", "password", null));
        });
    }

    @Test
    void loginPosTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        var login = serverFacade.login(new LoginRequest("bob", "password"));
        assertTrue(login.authToken().length() > 10);
        assertEquals("bob",login.username());
    }

    @Test
    void loginNegTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        Assertions.assertThrows(ResponseException.class, () -> {
            var login = serverFacade.login(new LoginRequest("bob", "passwor"));
        });
    }

    @Test
    void logoutPosTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        var login = serverFacade.login(new LoginRequest("bob", "password"));
        var logout = serverFacade.logout(new LogoutRequest(authData.authToken()));
        assertEquals("OK",logout.message());
    }

    @Test
    void logoutNegTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        var login = serverFacade.login(new LoginRequest("bob", "password"));

        Assertions.assertThrows(ResponseException.class, () -> {
            var logout = serverFacade.logout(new LogoutRequest("kljdflkjsdf"));
        });
    }

    @Test
    void createPosTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        var login = serverFacade.login(new LoginRequest("bob", "password"));
        var create = serverFacade.create(new CreateRequest("winners", authData.authToken()));
        assertEquals(1,create.gameID());
    }

    @Test
    void createNegTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        var login = serverFacade.login(new LoginRequest("bob", "password"));

        Assertions.assertThrows(ResponseException.class, () -> {
            var create = serverFacade.create(new CreateRequest("winners", "sdfsdf"));
        });
    }

    @Test
    void listPosTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        var login = serverFacade.login(new LoginRequest("bob", "password"));
        var create = serverFacade.create(new CreateRequest("winners", authData.authToken()));
        var list = serverFacade.list(new ListRequest(authData.authToken()));

        List<GameResult> games = new ArrayList<>();
        games.add(new GameResult(create.gameID(), null,null,"winners"));

        assertEquals(games,list.games());
    }

    @Test
    void listNegTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        var login = serverFacade.login(new LoginRequest("bob", "password"));
        var create = serverFacade.create(new CreateRequest("winners", authData.authToken()));

        Assertions.assertThrows(ResponseException.class, () -> {
            var list = serverFacade.list(new ListRequest("sdfsdf"));
        });
    }

    @Test
    void joinPosTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        var login = serverFacade.login(new LoginRequest("bob", "password"));
        var create = serverFacade.create(new CreateRequest("winners", authData.authToken()));
        var list = serverFacade.list(new ListRequest(authData.authToken()));
        var join = serverFacade.join(new JoinRequest(authData.authToken(),"WHITE", create.gameID()));

        assertEquals("",join.message());
    }

    @Test
    void joinNegBadColorTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        var login = serverFacade.login(new LoginRequest("bob", "password"));
        var create = serverFacade.create(new CreateRequest("winners", authData.authToken()));
        var list = serverFacade.list(new ListRequest(authData.authToken()));

        Assertions.assertThrows(ResponseException.class, () -> {
            var join = serverFacade.join(new JoinRequest(authData.authToken(),"WHIT", create.gameID()));

        });
    }

    @Test
    void joinNegBadAuthTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        var login = serverFacade.login(new LoginRequest("bob", "password"));
        var create = serverFacade.create(new CreateRequest("winners", authData.authToken()));
        var list = serverFacade.list(new ListRequest(authData.authToken()));

        Assertions.assertThrows(ResponseException.class, () -> {
            var join = serverFacade.join(new JoinRequest("adfsasa","WHITE", create.gameID()));

        });
    }

    @Test
    void joinNegBadGameIDTest() throws Exception {
        var authData = serverFacade.register(new RegisterRequest("bob", "password", "p1@email.com"));
        var login = serverFacade.login(new LoginRequest("bob", "password"));
        var create = serverFacade.create(new CreateRequest("winners", authData.authToken()));
        var list = serverFacade.list(new ListRequest(authData.authToken()));

        Assertions.assertThrows(ResponseException.class, () -> {
            var join = serverFacade.join(new JoinRequest(authData.authToken(),"WHITE", 352));

        });
    }








}
