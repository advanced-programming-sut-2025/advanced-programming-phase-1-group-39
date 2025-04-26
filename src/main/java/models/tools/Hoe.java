package models.tools;

import models.Result;

public class Hoe extends Tool {
    public Hoe(String name, ToolType type) {
        super(name, type);
    }

    @Override
    public Result useTool() {
        return null;
    }
}
