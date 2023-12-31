package BuxomMod.powers;

import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import BuxomMod.BuxomMod;
import BuxomMod.cards.BrokenBraM;
import BuxomMod.util.TextureLoader;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class MCupPower extends BraPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BuxomMod.makeID("MCupPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("BuxomModResources/images/powers/MCup84.png");
    private static final Texture tex32 = TextureLoader.getTexture("BuxomModResources/images/powers/MCup32.png");
    private int buffAmount = 1;

    public MCupPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.amount2 = 20;
        this.source = source;
        this.minCapacity = 10;
        this.bounceBonus = 3;
        this.buffAmount = 2;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    /*public void onInitialApplication() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new StrengthPower(owner, buffAmount), buffAmount));
    }*/

    public void broken(){
        flash();
        AbstractDungeon.actionManager.addToBottom(
                new ReducePowerAction(owner, owner, this, this.amount));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new BrokenBraM(), 1, true, true));
    }


    public void onGrow(int howMuch) {
        if (this.inCapacity() == true) {
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new VigorPower(this.owner, this.amount), this.amount));
        }
    }

    public void onRemove() {
        addToBot(new ReducePowerAction(owner, owner, owner.getPower(StrengthPower.POWER_ID), buffAmount));
    }

    /*public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) { // At the end of your turn
        if (power instanceof CommonPower) {
            if ((target.getPower("BuxomMod:CommonPower") != null) && (target.getPower("BuxomMod:CommonPower").amount > this.amount2)) {
                flash();
                AbstractDungeon.actionManager.addToBottom(
                        new ReducePowerAction(owner, owner, this, this.amount));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new BrokenBraM(), 1, true, true));
            }
        }
    }*/

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new MCupPower(owner, source, amount);
    }
}
