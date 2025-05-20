package com.shatteredpixel.shatteredpixeldungeon.items.strategy;

import com.mygdx.game.entities.Monster;
import com.mygdx.game.entities.Player;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.Constants;

public class CharmMushroomStrategy implements UseStrategy {

    @Override
    public boolean use(Player player, GameScreen gameScreen) {
        boolean used = false;

        // 寻找血量低于25%的非boss敌人
        for (Monster monster : gameScreen.getMonsters()) {
            if (!monster.isBoss() && monster.getHealth() < monster.getMaxHealth() * 0.25f && !monster.isPet()) {
                // 将怪物转换为宠物
                convertToPet(monster, player);
                used = true;
                break;  // 一次只转换一个怪物
            }
        }

        return used;
    }

    private void convertToPet(Monster monster, Player player) {
        monster.setPet(true);
        monster.setPetOwner(player);
        // 设置宠物标识，例如改变颜色
        monster.setPetColor(Constants.PET_COLOR);
    }
}
