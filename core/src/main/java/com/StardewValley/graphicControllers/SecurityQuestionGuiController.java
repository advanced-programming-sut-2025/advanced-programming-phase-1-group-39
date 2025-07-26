package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.graphicViews.LoginMenuScreen;
import com.StardewValley.graphicViews.SecurityQuestionMenuScreen;
import com.StardewValley.graphicViews.SignupMenuScreen;
import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.SecurityQuestion;
import com.StardewValley.models.services.HashSHA256;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SecurityQuestionGuiController {
    private SecurityQuestionMenuScreen view;

    public void setView(SecurityQuestionMenuScreen view) {
        this.view = view;
    }

    public void handleSecurityMenu() {
        if (view != null) {

            view.getQuestion1().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    view.getQuestion2().setChecked(false);
                    view.getQuestion3().setChecked(false);
                    view.getQuestion4().setChecked(false);
                    view.getQuestion5().setChecked(false);
                }
            });

            view.getQuestion2().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    view.getQuestion1().setChecked(false);
                    view.getQuestion3().setChecked(false);
                    view.getQuestion4().setChecked(false);
                    view.getQuestion5().setChecked(false);
                }
            });

            view.getQuestion3().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    view.getQuestion1().setChecked(false);
                    view.getQuestion2().setChecked(false);
                    view.getQuestion4().setChecked(false);
                    view.getQuestion5().setChecked(false);
                }
            });

            view.getQuestion4().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    view.getQuestion1().setChecked(false);
                    view.getQuestion2().setChecked(false);
                    view.getQuestion3().setChecked(false);
                    view.getQuestion5().setChecked(false);
                }
            });

            view.getQuestion5().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    view.getQuestion1().setChecked(false);
                    view.getQuestion2().setChecked(false);
                    view.getQuestion3().setChecked(false);
                    view.getQuestion4().setChecked(false);
                }
            });

            view.getRegisterButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    String answer = view.getAnswer().getText();
                    App app = App.getApp();
                        if (answer == null) {
                            view.getAnswerErrorLabel().setText("answer field cannot be empty");
                        } else if (setQuestionNumber().equals("0")) {
                            view.getAnswerErrorLabel().setText("Please choose your Question Number");
                        } else {
                            SecurityQuestion question = new SecurityQuestion(setQuestionNumber(), answer);
                            app.getPendingUser().setSecurityQuestion(question);
                            String hashPass = HashSHA256.hashPassword(app.getPendingUser().getPassword());
                            app.getPendingUser().setPassword(hashPass);
                            app.addUser(app.getPendingUser());
                            app.setPendingUser(null);
                            app.setCurrentMenu(Menu.LOGIN_MENU);
                            Main.getMain().switchScreen(new LoginMenuScreen());
                        }
                }
            });

            view.getBackButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    App.getApp().setCurrentMenu(Menu.SIGNUP_MENU);
                    Main.getMain().switchScreen(new SignupMenuScreen());
                }
            });
        }
    }

    // Auxiliary functions :

    public String setQuestionNumber() {
        if (view.getQuestion1().isChecked()) {
            return "1";
        } else if (view.getQuestion2().isChecked()) {
            return "2";
        } else if (view.getQuestion3().isChecked()) {
            return "3";
        } else if (view.getQuestion4().isChecked()) {
            return "4";
        } else if (view.getQuestion5().isChecked()) {
            return "5";
        } else {
            return "0";
        }
    }


}
