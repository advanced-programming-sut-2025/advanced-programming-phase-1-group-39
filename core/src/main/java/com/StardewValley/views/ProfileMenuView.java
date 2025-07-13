package com.StardewValley.views;


import com.StardewValley.controllers.AppControllers;
import com.StardewValley.controllers.ProfileMenuController;
import com.StardewValley.models.Enums.commands.ProfileMenuCommands;
import com.StardewValley.models.Result;

import java.util.regex.Matcher;

public class ProfileMenuView implements View {
    private final ProfileMenuController controller = AppControllers.profileMenuController;
    
    @Override
    public void checkCommand(String command) {
        Matcher matcher;
        Result result;
        
        if ((matcher = ProfileMenuCommands.ChangeUsername.getMatcher(command)) != null) {
            result = controller.changeUsername(matcher);
            System.out.println(result.message());
        } else if ((matcher = ProfileMenuCommands.ChangeNickname.getMatcher(command)) != null) {
            result = controller.changeNickname(matcher);
            System.out.println(result.message());
        } else if ((matcher = ProfileMenuCommands.ChangeEmail.getMatcher(command)) != null) {
            result = controller.changeEmail(matcher);
            System.out.println(result.message());            
        } else if ((matcher = ProfileMenuCommands.ChangePassword.getMatcher(command)) != null) {
            result = controller.changePassword(matcher);
            System.out.println(result.message());
        } else if ((matcher = ProfileMenuCommands.ShowUserInfo.getMatcher(command)) != null) {
            result = controller.showUserInfo();
            System.out.println(result.message());
        } else if ((matcher = ProfileMenuCommands.ShowCurrentMenu.getMatcher(command)) != null) {
            result = controller.showCurrentMenu();
            System.out.println(result.message());
        } else if ((matcher = ProfileMenuCommands.GoToMainMenu.getMatcher(command)) != null) {
            result = controller.gotoMainMenu();
            System.out.println(result.message());
        } else {
            System.out.println("invalid command!");
        }
    }
}
