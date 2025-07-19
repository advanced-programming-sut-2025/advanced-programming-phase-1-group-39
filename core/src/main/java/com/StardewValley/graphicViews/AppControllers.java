package com.StardewValley.graphicViews;

import com.StardewValley.graphicControllers.ForgetPasswordMenuController;
import com.StardewValley.graphicControllers.LoginMenuController;
import com.StardewValley.graphicControllers.SecurityQuestionController;
import com.StardewValley.graphicControllers.SignupMenuController;

public class AppControllers {
    public static SignupMenuController signupMenuController = new SignupMenuController();
    public static SecurityQuestionController securityQuestionController = new SecurityQuestionController();
    public static LoginMenuController loginMenuController = new LoginMenuController();
    public static ForgetPasswordMenuController forgetPasswordMenuController = new ForgetPasswordMenuController();
}
