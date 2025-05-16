package controllers;

import models.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignupMenuTest {
    private SignupMenuController controller = new SignupMenuController();

    @BeforeEach
    void setup() {
        controller = new SignupMenuController();
    }
}
