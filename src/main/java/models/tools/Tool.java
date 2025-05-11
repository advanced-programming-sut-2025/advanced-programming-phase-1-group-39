package models.tools;

import models.Item;
import models.Result;

public abstract class Tool extends Item {
    protected String name;
    protected ToolType type;
    protected int baseUsingEnergy;

    public Tool(String name, ToolType type, int baseUsingEnergy) {
        super(name);
        this.type = type;
        this.baseUsingEnergy = baseUsingEnergy;
    }

    public abstract Result useTool();
    public ToolType getType() {
        return type;
    }
    public void upgradeType() {
        this.type = ToolType.getNext(type);
    }

    public abstract int getUsingEnergy();
}