package dev.emmaguy.fruitninja;

import java.util.Random;

public enum FruitType {
    WATERMELON(R.drawable.watermelon), STRAWBERRY(R.drawable.strawberry), 
    PINEAPPLE(R.drawable.pineapple), PAPAYA(R.drawable.papaya),
    GRAPES(R.drawable.grape), APPLE(R.drawable.apple), 
    BANANA(R.drawable.banana), ORANGE(R.drawable.orange);

    private final int resourceId;

    private FruitType(int resourceId) {
	this.resourceId = resourceId;
    }

    public int getResourceId() {
	return resourceId;
    }

    private static final Random random = new Random();

    public static FruitType randomFruit() {
	return FruitType.values()[random.nextInt(FruitType.values().length)];
    }
}