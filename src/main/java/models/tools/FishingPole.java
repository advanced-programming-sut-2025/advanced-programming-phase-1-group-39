package models.tools;

import models.Result;

public class FishingPole extends Tool {
    public FishingPole(String name, ToolType type) {
        super(name, type);
    }

    @Override
    public Result useTool() {
        return null;
    }
}
