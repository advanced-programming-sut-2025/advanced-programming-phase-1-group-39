package models.tools;

import models.Result;

public class Scythe extends Tool {
    public Scythe(String name) {
        super("Axe", ToolType.BASIC);
    }
//    public Scythe(String name, ToolType type) {
//        super(name, type);
//    }

    @Override
    public Result useTool() {
        return null;
    }
}
