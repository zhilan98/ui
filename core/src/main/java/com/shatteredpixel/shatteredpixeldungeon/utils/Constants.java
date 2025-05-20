package com.shatteredpixel.shatteredpixeldungeon.utils;

public class Constants {
    // 宠物相关常量
    public static final int PET_COLOR = 0x339DFFFF;  // ARGB格式: 蓝色(透明度100%)
    public static final float PET_FOLLOW_DISTANCE = 2f;  // 宠物跟随距离

    // 默认物品价值
    public static final int DEFAULT_ITEM_VALUE = 20;

    // 物品名称和描述
    public static class ItemNames {
        // 现有物品名...
        public static final String CHARM_MUSHROOM = "Charm_mushroom";
    }

    public static class ItemDescriptions {
        // 现有物品描述...
        public static final String CHARM_MUSHROOM_DESC = "You can turn non-boss enemies with less than 25% health into your pet.";
    }
}