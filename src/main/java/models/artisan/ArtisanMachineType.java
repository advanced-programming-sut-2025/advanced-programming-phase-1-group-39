package models.artisan;

public enum ArtisanMachineType {
    BEE_HOUSE(new ArtisanMachine()),
    CHEESE_PRESS(new ArtisanMachine()),
    MAYO_MACHINE(new ArtisanMachine()),
    KEG(new ArtisanMachine()),
    PRESERVE_JAR(new ArtisanMachine()),
    OIL_MAKER(new ArtisanMachine()),
    LOOM(new ArtisanMachine()),
    CHARCOAL_KILN(new ArtisanMachine()),
    FURNACE(new ArtisanMachine()),
    FISH_SMOKER(new ArtisanMachine()),
    MUSHROOM_DRIER(new ArtisanMachine());

    public final ArtisanMachine machine;
    ArtisanMachineType(ArtisanMachine machine) {
        this.machine = machine;
    }
}

