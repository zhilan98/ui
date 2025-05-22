package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Component;

public class StatsPanel extends Component {

    private NinePatch bg;
    private BitmapText statsText;
    private float elapsed = 0f;

    private static final float PANEL_WIDTH = 120;
    private static final float PANEL_HEIGHT = 48;

    public StatsPanel() {
        super();

        // 用和StatusPane一样的资源
        String asset = com.shatteredpixel.shatteredpixeldungeon.Assets.Interfaces.STATUS;

        bg = new NinePatch(asset, 0, 0, 128, 36, 85, 0, 45, 0);
        bg.size(PANEL_WIDTH, PANEL_HEIGHT);
        add(bg);

        statsText = new BitmapText(PixelScene.pixelFont);
        statsText.hardlight(0xFFFFFF); // 白色字体
        add(statsText);

        setStatsText();
    }

    @Override
    public void update() {
        super.update();
        elapsed += Game.elapsed;
        if (elapsed >= 1f) {
            elapsed = 0f;
            setStatsText();
        }
    }

    private void setStatsText() {
        long now = System.currentTimeMillis();
        long floorSec = (now - Statistics.floorStartMillis) / 1000;
        long totalSec = (now - Statistics.gameStartMillis) / 1000;
        statsText.text(String.format(
                "伤害输出: %d\n承受伤害: %d\n击杀: %d\n楼层: %d\n本层: %02d:%02d\n总计: %02d:%02d",
                Statistics.damageDealt, Statistics.damageTaken, Statistics.enemiesSlain,
                Dungeon.depth,
                floorSec / 60, floorSec % 60,
                totalSec / 60, totalSec % 60
        ));
        statsText.measure();
        layout();
    }

    @Override
    protected void layout() {
        super.layout();
        // 显示在屏幕右上角，微调避免重叠
        float x = Game.width - PANEL_WIDTH - 5;
        float y = 5;
        bg.x = x;
        bg.y = y;
        bg.size(PANEL_WIDTH, PANEL_HEIGHT);

        statsText.x = x + 8;
        statsText.y = y + 4;
        statsText.measure();
    }
}