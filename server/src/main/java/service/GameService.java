package service;

public class GameService extends Service {
    static GameService staticGameService = new GameService();

    /**
     *
     * @return Returns a static instance of GameService
     */
    static GameService getInstance() {
        return staticGameService;
    }
}
