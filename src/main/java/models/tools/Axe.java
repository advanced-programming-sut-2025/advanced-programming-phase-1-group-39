package models.tools;

import models.Result;

public class Axe extends Tool {
    public Axe(String name) {
        super("Axe", ToolType.BASIC);
    }
//    public Axe(String name, ToolType type) {
//        super(name, type);
//    }

    @Override
    public Result useTool() {
        return null;
    }
}
