
// Add this class to the items.scrolls package
package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;

public class ScrollOfLevelSkip extends Scroll {

    {
        image = ItemSpriteSheet.Icons.SCROLL_TELEPORT; // Note the Icons class
        unique = true;
    }

    @Override
    public void doRead() {
        collect(); // Don't consume the scroll
        GameScene.show(new WndLevelSkip());
        Sample.INSTANCE.play(Assets.Sounds.READ);
        Invisibility.dispel();
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isKnown() {
        return true;
    }

    @Override
    public int value() {
        return 50;
    }

    private class WndLevelSkip extends WndOptions {
        WndLevelSkip() {
            super("Level Skip", "Choose a level to skip to:",
                    "Next Level",
                    "Skip to Boss (Floor " + getNextBossFloor() + ")",
                    "Cancel");
        }

        @Override
        protected void onSelect(int index) {
            if (index == 0) {
                skipToFloor(Dungeon.depth + 1);
            } else if (index == 1) {
                skipToFloor(getNextBossFloor());
            }
        }
    }

    private int getNextBossFloor() {
        // Boss floors are 5, 10, 15, 20, 25
        int currentDepth = Dungeon.depth;
        return ((currentDepth / 5) + 1) * 5;
    }

    private void skipToFloor(int depth) {
        if (depth > 0 && depth <= 26) {
            GLog.w("Skipping to floor " + depth + "...");
            Dungeon.hero.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

            // Set the target depth
            Dungeon.depth = depth - 1; // -1 because the descent will add 1

            // Transition to the new floor
            Game.switchScene(InterlevelScene.class);
        } else {
            GLog.w("Cannot skip to that floor.");
        }
    }
}
