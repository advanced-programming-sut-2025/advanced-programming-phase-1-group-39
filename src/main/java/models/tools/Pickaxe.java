package models.tools;

import models.Result;

public class Pickaxe extends Tool {
    public Pickaxe(String name, ToolType type) {
        super(name, type);
    }

    @Override
    public Result useTool() {
        return null;
    }
}
