package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class LevelSkipButton extends Button {

    protected Image image;
    protected boolean levelsUnlocked = false;

    public LevelSkipButton() {
        super();

        width = 20;
        height = 20;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        image = Icons.get(Icons.RIGHTARROW);
        add(image);

        // Determine if player has unlocked level skip option
        // This can be tied to game progress or a setting
        levelsUnlocked = SPDSettings.levelSkipUnlocked();
    }

    @Override
    protected void layout() {
        super.layout();

        image.x = x + (width - image.width()) / 2;
        image.y = y + (height - image.height()) / 2;
    }

    @Override
    protected void onPointerDown() {
        image.brightness(1.5f);
    }

    @Override
    protected void onPointerUp() {
        image.resetColor();
    }

    @Override
    protected void onClick() {
        if (levelsUnlocked) {
            GameScene.show(new WndLevelSkip());
        } else {
            GameScene.show(new WndOptions(
                    Messages.get(this, "locked_title"),
                    Messages.get(this, "locked_desc"),
                    Messages.get(this, "ok")));
        }
    }

    private class WndLevelSkip extends WndOptions {
        WndLevelSkip() {
            super(Messages.get(LevelSkipButton.class, "title"),
                    Messages.get(LevelSkipButton.class, "desc"),
                    Messages.get(LevelSkipButton.class, "next_level"),
                    Messages.get(LevelSkipButton.class, "next_boss", getNextBossFloor()),
                    Messages.get(LevelSkipButton.class, "cancel"));
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
            GLog.w(Messages.get(this, "skipping", depth));
            Dungeon.hero.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

            // Set the target depth
            Dungeon.depth = depth - 1; // -1 because the descent will add 1

            // Transition to the new floor
            Game.switchScene(InterlevelScene.class);
        } else {
            GLog.w(Messages.get(this, "cant_skip"));
        }
    }
}
