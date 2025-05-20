package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CharmMushroom extends Item {

    {
        image = ItemSpriteSheet.CHARM_MUSHROOM; // You'll need to add this to ItemSpriteSheet.java
        stackable = true;
        defaultAction = AC_THROW;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_EAT);
        return actions;
    }

    public static final String AC_EAT = "EAT";

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_EAT)) {
            detach(hero.belongings.backpack);

            hero.sprite.operate(hero.pos);
            hero.busy();

            hero.sprite.emitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
            Sample.INSTANCE.play(Assets.Sounds.MELD);

            GLog.i(Messages.get(this, "eat_msg"));

            // Self-charm effect
            Buff.affect(hero, Charm.class, 10f);

            hero.spendAndNext(1f);
        }
    }

    @Override
    protected void onThrow(int cell) {
        Char ch = Actor.findChar(cell);

        if (ch != null && ch != Dungeon.hero) {
            // Charm effect on throw
            if (Random.Int(3) == 0) { // 33% chance to charm
                Buff.affect(ch, Charm.class, Random.NormalIntRange(3, 6));
                ch.sprite.emitter().start(Speck.factory(Speck.HEART), 0.2f, 5);
                Sample.INSTANCE.play(Assets.Sounds.CHARMS);
            }
        }

        // Spread spores to adjacent enemies
        if (ch != null) {
            for (int i : PathFinder.NEIGHBOURS8) {
                Char neighbor = Actor.findChar(cell + i);
                if (neighbor != null && neighbor != Dungeon.hero && Random.Int(5) == 0) { // 20% chance
                    Buff.affect(neighbor, Charm.class, Random.NormalIntRange(2, 4));
                    neighbor.sprite.emitter().burst(Speck.factory(Speck.HEART), 3);
                }
            }
        }

        super.onThrow(cell);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 35 * quantity;
    }
}