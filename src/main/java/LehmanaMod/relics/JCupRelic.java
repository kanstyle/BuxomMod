package LehmanaMod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import LehmanaMod.DefaultMod;
import LehmanaMod.powers.CommonPower;
import LehmanaMod.util.TextureLoader;

import static LehmanaMod.DefaultMod.makeRelicOutlinePath;
import static LehmanaMod.DefaultMod.makeRelicPath;

public class JCupRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = DefaultMod.makeID("JCupRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("JCupRelic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("JCupRelic.png"));

    public JCupRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    // Flash at the start of Battle.

    public void atBattleStart() {
        flash();
        CardCrawlGame.sound.playV(DefaultMod.makeID("GASP"), 0.5F);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CommonPower(AbstractDungeon.player, AbstractDungeon.player, 1), 1));
    }
    /*
    // Gain 1 energy on equip.
    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    // Lose 1 energy on unequip.
    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }
*/
    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
