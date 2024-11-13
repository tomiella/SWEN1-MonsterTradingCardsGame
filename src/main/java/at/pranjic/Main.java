package at.pranjic;

import at.pranjic.application.mtcg.MonsterTradingCardsGameApplication;
import at.pranjic.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(new MonsterTradingCardsGameApplication());
        server.start();
    }

}