package BuxomMod.powers;

import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import BuxomMod.BuxomMod;
import BuxomMod.cards.BrokenBraK;
import BuxomMod.util.TextureLoader;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;

public class KCupPower extends BraPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = BuxomMod.makeID("KCupPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("BuxomModResources/images/powers/KCup84.png");
    private static final Texture tex32 = TextureLoader.getTexture("BuxomModResources/images/powers/KCup32.png");

    public KCupPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.amount2 = 10;
        this.source = source;
        this.minCapacity = 0;
        this.bounceBonus = 5;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    /*public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        AbstractPlayer p = AbstractDungeon.player;// At the end of your turn
        if ((p.getPower("BuxomMod:CommonPower") != null) && (p.getPower("BuxomMod:CommonPower").amount >= this.amount2)) {
            flash();
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(owner, owner, new StrengthPower(owner, -2), -2));
            AbstractDungeon.actionManager.addToBottom(
                    new ReducePowerAction(owner, owner, this, this.amount));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new BrokenBraK(), 1, true, true));
        }
    }*/
    private int buffAmount = 2;
    public void onGrow(int howMuch) {
        if (this.inCapacity() == true) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.source, this.source, howMuch));
        }
    }
    public void onInitialApplication() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new DexterityPower(owner, this.amount), this.amount));
    }

    /*public void update() {
        if ((this.owner.getPower("BuxomMod:CommonPower") != null) && (this.owner.getPower("BuxomMod:CommonPower").amount >= this.amount2)) {
                flash();
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(owner, owner, new StrengthPower(owner, -2), -2));
                AbstractDungeon.actionManager.addToBottom(
                        new ReducePowerAction(owner, owner, this, this.amount));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new BrokenBraK(), 1, true, true));
            }
        }*/

    public void broken() {
        flash();

        AbstractDungeon.actionManager.addToTop(
                new ReducePowerAction(owner, owner, this, this.amount));
        AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction((AbstractCard) new BrokenBraK(), 1, true, true));
    }
    public void onRemove() {
        addToBot(new ReducePowerAction(owner, owner, owner.getPower(DexterityPower.POWER_ID), buffAmount));
    }

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
        return new KCupPower(owner, source, amount);
    }
}
