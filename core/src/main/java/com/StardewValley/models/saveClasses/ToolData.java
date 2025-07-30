package com.StardewValley.models.saveClasses;

import com.StardewValley.models.tools.FishingPole;
import com.StardewValley.models.tools.FishingPoleType;
import com.StardewValley.models.tools.Tool;
import com.StardewValley.models.tools.ToolType;

public class ToolData {
    public String toolName;
    public ToolType type;
    public FishingPoleType poleType = null;

    public ToolData(Tool tool) {
        toolName = tool.getName();
        type = tool.getType();
        if (tool instanceof FishingPole) {
            poleType = ((FishingPole) tool).getPoleType();
        }
    }
    public ToolData() {}

    public Tool getTool() {
        return Tool.getToolByDetail(toolName, type, poleType);
    }
}
