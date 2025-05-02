package models.tools;

import models.Result;

public class MilkPail extends Tool {
    public MilkPail(String name) {
        super("Axe", ToolType.BASIC);
    }
//    public MilkPail(String name, ToolType type) {
//        super(name, type);
//    }

    @Override
    public Result useTool() {
        return null;
    }
}
