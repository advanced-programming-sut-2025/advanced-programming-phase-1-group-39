package models.tools;

import models.Item;
import models.Result;

public abstract class Tool extends Item {
    protected String name;
    protected ToolType type;
    protected int baseUsingEnergy;

    public Tool(String name, ToolType type) {
        super(name);
        this.type = type;
    }

    public abstract Result useTool();
    public ToolType getType() {
        return type;
    }
    public void upgradeType() {
        this.type = ToolType.getNext(type);
    }
}