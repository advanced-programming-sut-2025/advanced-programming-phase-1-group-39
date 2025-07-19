package com.StardewValley.graphicControllers;

import com.StardewValley.Main;
import com.StardewValley.graphicViews.SecurityQuestionMenuView;
import com.StardewValley.models.App;
import com.StardewValley.models.Enums.Menu;
import com.StardewValley.models.SecurityQuestion;
import com.StardewValley.models.services.HashSHA256;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SecurityQuestionController {
    private SecurityQuestionMenuView view;

    public void setView(SecurityQuestionMenuView view) {
        this.view = view;
    }

    public void handleSecurityMenu() {
        if (view != null) {

            view.getQuestion1().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getQuestion1().isChecked()) {
                        view.getQuestion2().setChecked(false);
                        view.getQuestion3().setChecked(false);
                        view.getQuestion4().setChecked(false);
                        view.getQuestion5().setChecked(false);
                    }
                }
            });

            view.getQuestion2().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getQuestion2().isChecked()) {
                        view.getQuestion1().setChecked(false);
                        view.getQuestion3().setChecked(false);
                        view.getQuestion4().setChecked(false);
                        view.getQuestion5().setChecked(false);
                    }
                }
            });

            view.getQuestion3().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getQuestion3().isChecked()) {
                        view.getQuestion1().setChecked(false);
                        view.getQuestion2().setChecked(false);
                        view.getQuestion4().setChecked(false);
                        view.getQuestion5().setChecked(false);
                    }
                }
            });

            view.getQuestion4().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getQuestion4().isChecked()) {
                        view.getQuestion1().setChecked(false);
                        view.getQuestion2().setChecked(false);
                        view.getQuestion3().setChecked(false);
                        view.getQuestion5().setChecked(false);
                    }
                }
            });

            view.getQuestion5().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    if (view.getQuestion5().isChecked()) {
                        view.getQuestion1().setChecked(false);
                        view.getQuestion2().setChecked(false);
                        view.getQuestion3().setChecked(false);
                        view.getQuestion4().setChecked(false);
                    }
                }
            });

            view.getRegisterButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    String answer = view.getAnswer().getText();
                    App app = App.getApp();
                    if (view.getRegisterButton().isChecked()) {
                        if (answer == null || answer.equals("Enter your Answer")) {
                            view.getAnswerErrorLabel().setText("answer cannot be empty");
                            view.getRegisterButton().setChecked(false);
                        } else if (setQuestionNumber().equals("0")) {
                            view.getAnswerErrorLabel().setText("Please chose your Question Number");
                            view.getRegisterButton().setChecked(false);
                        } else {
                            SecurityQuestion question = new SecurityQuestion(setQuestionNumber(), answer);
                            app.getPendingUser().setSecurityQuestion(question);
                            String hashPass = HashSHA256.hashPassword(app.getPendingUser().getPassword());
                            app.getPendingUser().setPassword(hashPass);
                            app.addUser(app.getPendingUser());
                            app.setPendingUser(null);
                            Main.getMain().setScreen(Menu.LOGIN_MENU.getScreen());
                        }

                    }
                }
            });

            view.getBackButton().addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    Main.getMain().setScreen(Menu.SIGNUP_MENU.getScreen());
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
