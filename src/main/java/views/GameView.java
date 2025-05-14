package views;

import controllers.GameController;
import controllers.NPCGameController;
import models.Enums.commands.NPCGameCommand;
import models.Result;

import java.util.regex.Matcher;

public class GameView implements View {
    private final GameController controller = new GameController();
    private final NPCGameController npcController = new NPCGameController();

    @Override
    public void checkCommand(String command) {
        Matcher matcher;
        Result result;

        if ((matcher = NPCGameCommand.MeetNPC.getMatcher(command)) != null) {
            result = npcController.meetNPC(matcher);
            System.out.println(result.message());
        } else if ((matcher = NPCGameCommand.GiveGiftToNPC.getMatcher(command)) != null) {
            result = npcController.giveGift(matcher);
            System.out.println(result.message());
        } else if ((matcher = NPCGameCommand.ShowFriendShipList.getMatcher(command)) != null) {
            result = npcController.showFriendship();
            System.out.println(result.message());
        } else if ((matcher = NPCGameCommand.ShowQuestsList.getMatcher(command)) != null) {
            result = npcController.showQuestsList();
            System.out.println(result.message());
        } else if ((matcher = NPCGameCommand.QuestsFinish.getMatcher(command)) != null) {
            result = npcController.finishQuests(matcher);
            System.out.println(result.message());
        } else {
            System.out.println("invalid command!");
        }
    }
}
