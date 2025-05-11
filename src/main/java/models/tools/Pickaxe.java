package models.tools;

import models.Result;

public class Pickaxe extends Tool {
    public Pickaxe(String name, ToolType type) {
        super(name, type, 5);
    }

    @Override
    public Result useTool() {
        return null;
    }

    @Override
    public int getUsingEnergy() {
        return 0;
    }
}
