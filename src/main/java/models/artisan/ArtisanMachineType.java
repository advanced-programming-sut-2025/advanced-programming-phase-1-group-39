package models.artisan;

public enum ArtisanMachineType {
    CHARCOAL_KILN(new ArtisanMachine(null)),
    FURNACE(new ArtisanMachine(null)),
    BEE_HOUSE(new ArtisanMachine(null)),
    CHEESE_PRESS(new ArtisanMachine(null)),
    KEG(new ArtisanMachine(null)),
    LOOM(new ArtisanMachine(null)),
    MAYO_MACHINE(new ArtisanMachine(null)),
    OIL_MAKER(new ArtisanMachine(null)),
    PRESERVE_JAR(new ArtisanMachine(null)),
    DEHYDRATOR(new ArtisanMachine(null)),
    GRASS_STARTER(new ArtisanMachine(null)),
    FISH_SMOKER(new ArtisanMachine(null));

    public final ArtisanMachine machine;
    ArtisanMachineType(ArtisanMachine machine) {
        this.machine = machine;
    }
}

