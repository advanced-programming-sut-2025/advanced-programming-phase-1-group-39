package models.tools;

import models.Result;

public class Shear extends Tool {
    public Shear(String name, ToolType type) {
        super(name, type);
    }

    @Override
    public Result useTool() {
        return null;
    }
}
