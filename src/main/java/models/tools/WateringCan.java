package models.tools;

import models.Result;

public class WateringCan extends Tool {
    public WateringCan(String name, ToolType type) {
        super(name, type);
    }

    @Override
    public Result useTool() {
        return null;
    }

    public void upgradeTo(ToolType type) {
        this.type = type;
    }
}
