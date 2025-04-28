package models.tools;

import models.Item;
import models.Result;

public abstract class Tool extends Item {
    protected String name;
    protected ToolType type;
    protected int usingEnergy;

    public Tool(String name, ToolType type) {
        super(name);
        this.name = name;
        this.type = type;
    }

    public abstract Result useTool();
}
