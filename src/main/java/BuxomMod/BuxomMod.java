package BuxomMod;

import BuxomMod.patches.CustomTags;
import BuxomMod.potions.DragonMilkPotion;
import BuxomMod.potions.PermaGrowthPotion;
import BuxomMod.powers.BraBrokenPower;
import BuxomMod.powers.BraPower;
import BuxomMod.powers.MilkPower;
import BuxomMod.relics.CowRelic;
import BuxomMod.ui.BraPanel;
import BuxomMod.ui.BuxomPanel;
import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import BuxomMod.cards.*;
import BuxomMod.characters.TheBuxom;
//import BuxomMod.events.IdentityCrisisEvent;
import BuxomMod.potions.FlatteningPotion;
import BuxomMod.relics.BottledPlaceholderRelic;
import BuxomMod.relics.DwarfBoobsRelic;
import BuxomMod.relics.WashboardRelic;
import BuxomMod.util.IDCheckDontTouchPls;
import BuxomMod.util.TextureLoader;
import BuxomMod.variables.DefaultCustomVariable;
import BuxomMod.variables.DefaultSecondMagicNumber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "TheDefault" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 4 places.
// I comment those places below, under the place where you set your ID.

//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "TheDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "TheDefault", and change to "yourmodname" rather than "TheDefault".
// You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories, and press alt+c to make the replace case sensitive (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class BuxomMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber{
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(BuxomMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties TheDefaultDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true; // The boolean we'll be setting on/off (true/false)

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Default Mod";
    private static final String AUTHOR = "Gremious"; // And pretty soon - You!
    private static final String DESCRIPTION = "A base for Slay the Spire to start your own mod from, feat. the Default.";

    // =============== INPUT TEXTURE LOCATION =================

    // Colors (RGB)
    // Character Color
    public static final Color BUXOM_PINK = CardHelper.getColor(215.0f, 119.0f, 157.0f);

    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown
    public static final Color DRAGON_MILK_POTION_LIQUID = CardHelper.getColor(255.0f, 255.0f, 255.0f);
    public static final Color DRAGON_MILK_POTION_HYBRID = CardHelper.getColor(255.0f, 203.0f, 246.0f);
    public static final Color CHIBI_POTION_LIQUID = CardHelper.getColor(255.0f, 228.0f, 138.0f);
    public static final Color CHIBI_POTION_HYBRID = CardHelper.getColor(58.0f, 172.0f, 255.0f);
    public static final Color PERMA_POTION_LIQUID = CardHelper.getColor(67.0f, 16.0f, 64.0f);
    public static final Color PERMA_POTION_HYBRID = CardHelper.getColor(164.0f, 23.0f, 157.0f);

    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!

    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_BUXOM_PINK = "BuxomModResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_BUXOM_PINK = "BuxomModResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_BUXOM_PINK = "BuxomModResources/images/512/bg_power_default_gray.png";

    private static final String ENERGY_ORB_BUXOM_PINK = "BuxomModResources/images/512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "BuxomModResources/images/512/card_small_orb.png";

    private static final String ATTACK_BUXOM_PINK_PORTRAIT = "BuxomModResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_BUXOM_PINK_PORTRAIT = "BuxomModResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_BUXOM_PINK_PORTRAIT = "BuxomModResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_BUXOM_PINK_PORTRAIT = "BuxomModResources/images/1024/card_default_gray_orb.png";

    // Character assets
    private static final String THE_BUXOM_BUTTON = "BuxomModResources/images/charSelect/DefaultCharacterButton.png";
    private static final String THE_BUXOM_PORTRAIT = "BuxomModResources/images/charSelect/DefaultCharacterPortraitBG.png";
    public static final String THE_BUXOM_SHOULDER_1 = "BuxomModResources/images/char/defaultCharacter/shoulder.png";
    public static final String THE_BUXOM_SHOULDER_2 = "BuxomModResources/images/char/defaultCharacter/shoulder.png";
    public static final String THE_BUXOM_CORPSE = "BuxomModResources/images/char/character/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "BuxomModResources/images/Badge.png";

    // Atlas and JSON files for the Animations
    public static final String THE_BUXOM_SKELETON_ATLAS = "BuxomModResources/images/char/character/LehmanaSprite4.atlas";
    public static final String THE_BUXOM_SKELETON_JSON = "BuxomModResources/images/char/character/LehmanaSprite4_Armaturelehmana sprite.json";

    //VFX

    public static final String EXPAND_EFFECT = "BuxomModResources/images/vfx/expand_effect.png";


    // =============== MAKE IMAGE PATHS =================

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    // =============== /MAKE IMAGE PATHS/ =================

    // =============== /INPUT TEXTURE LOCATION/ =================
    public static BuxomPanel buxomPanel;
    public static BraPanel braPanel;

    // =============== SUBSCRIBE, CREATE THE COLOR_PINK, INITIALIZE =================

    public BuxomMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */

        setModID("BuxomMod");
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:

        // 1. Go to your resources folder in the project panel, and refactor> rename TheDefaultResources to
        // yourModIDResources.

        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project) and press alt+c (or mark the match case option)
        // replace all instances of TheDefault with yourModID, and all instances of TheDefault with yourmodid (the same but all lowercase).
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        // It's important that the mod ID prefix for keywords used in the cards descriptions is lowercase!

        // 3. Scroll down (or search for "ADD CARDS") till you reach the ADD CARDS section, and follow the TODO instructions

        // 4. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than TheDefault. They get loaded before getID is a thing.

        logger.info("Done subscribing");

        logger.info("Creating the color " + TheBuxom.Enums.COLOR_PINK.toString());

        BaseMod.addColor(TheBuxom.Enums.COLOR_PINK, BUXOM_PINK, BUXOM_PINK, BUXOM_PINK,
                BUXOM_PINK, BUXOM_PINK, BUXOM_PINK, BUXOM_PINK,
                ATTACK_BUXOM_PINK, SKILL_BUXOM_PINK, POWER_BUXOM_PINK, ENERGY_ORB_BUXOM_PINK,
                ATTACK_BUXOM_PINK_PORTRAIT, SKILL_BUXOM_PINK_PORTRAIT, POWER_BUXOM_PINK_PORTRAIT,
                ENERGY_ORB_BUXOM_PINK_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Done creating the color");


        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        TheDefaultDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("defaultMod", "TheDefaultConfig", TheDefaultDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");

    }


    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP

    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = BuxomMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO

    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = BuxomMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = BuxomMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO

    // ====== YOU CAN EDIT AGAIN ======


    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        BuxomMod buxommod = new BuxomMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }

    // ============== /SUBSCRIBE, CREATE THE COLOR_PINK, INITIALIZE/ =================


    // =============== LOAD THE CHARACTER =================

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheBuxom.Enums.THE_BUXOM.toString());

        BaseMod.addCharacter(new TheBuxom("the Default", TheBuxom.Enums.THE_BUXOM),
                THE_BUXOM_BUTTON, THE_BUXOM_PORTRAIT, TheBuxom.Enums.THE_BUXOM);

        receiveEditPotions();
        logger.info("Added " + TheBuxom.Enums.THE_BUXOM.toString());
    }

    // =============== /LOAD THE CHARACTER/ =================


    // =============== POST-INITIALIZE =================

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

            enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("defaultMod", "TheDefaultConfig", TheDefaultDefaultSettings);
                config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
        buxomPanel = new BuxomPanel();
        braPanel = new BraPanel();


        // =============== EVENTS =================
        // https://github.com/daviscook477/BaseMod/wiki/Custom-Events

        // You can add the event like so:
        // BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);
        // Then, this event will be exclusive to the City (act 2), and will show up for all characters.
        // If you want an event that's present at any part of the game, simply don't include the dungeon ID

        // If you want to have more specific event spawning (e.g. character-specific or so)
        // deffo take a look at that basemod wiki link as well, as it explains things very in-depth
        // btw if you don't provide event type, normal is assumed by default

        // Create a new event builder
        // Since this is a builder these method calls (outside of create()) can be skipped/added as necessary
        /*AddEventParams eventParams = new AddEventParams.Builder(IdentityCrisisEvent.ID, IdentityCrisisEvent.class) // for this specific event
            .dungeonID(TheCity.ID) // The dungeon (act) this event will appear in
            .playerClass(TheBuxom.Enums.THE_BUXOM) // Character specific event
            .create();

        // Add the event
        BaseMod.addEvent(eventParams);*/

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }

    // =============== / POST-INITIALIZE/ =================

    // ================ ADD POTIONS ===================

    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_BUXOM".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        BaseMod.addPotion(FlatteningPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, FlatteningPotion.POTION_ID, TheBuxom.Enums.THE_BUXOM);
        BaseMod.addPotion(DragonMilkPotion.class, DRAGON_MILK_POTION_LIQUID, DRAGON_MILK_POTION_HYBRID, null, DragonMilkPotion.POTION_ID, TheBuxom.Enums.THE_BUXOM);
        //BaseMod.addPotion(ChibiPotion.class, CHIBI_POTION_LIQUID, CHIBI_POTION_HYBRID, null, ChibiPotion.POTION_ID, TheBuxom.Enums.THE_BUXOM);
        BaseMod.addPotion(PermaGrowthPotion.class, PERMA_POTION_LIQUID, PERMA_POTION_HYBRID, null, PermaGrowthPotion.POTION_ID, TheBuxom.Enums.THE_BUXOM);

        logger.info("Done editing potions");
    }

    // ================ /ADD POTIONS/ ===================


    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // Take a look at https://github.com/daviscook477/BaseMod/wiki/AutoAdd
        // as well as
        // https://github.com/kiooeht/Bard/blob/e023c4089cc347c60331c78c6415f489d19b6eb9/src/main/java/com/evacipated/cardcrawl/mod/bard/BardMod.java#L319
        // for reference as to how to turn this into an "Auto-Add" rather than having to list every relic individually.
        // Of note is that the bard mod uses it's own custom relic class (not dissimilar to our AbstractDefaultCard class for cards) that adds the 'color' field,
        // in order to automatically differentiate which pool to add the relic too.

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new WashboardRelic(), TheBuxom.Enums.COLOR_PINK);
        BaseMod.addRelicToCustomPool(new DwarfBoobsRelic(), TheBuxom.Enums.COLOR_PINK);
        BaseMod.addRelicToCustomPool(new CowRelic(), TheBuxom.Enums.COLOR_PINK);

        // This adds a relic to the Shared pool. Every character can find this relic.

        // Mark relics as seen - makes it visible in the compendium immediately
        // If you don't have this it won't be visible in the compendium until you see them in game
        // (the others are all starters so they're marked as seen in the character file)
        UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
        logger.info("Done adding relics!");
    }

    // ================ /ADD RELICS/ ===================


    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variables");
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());

        logger.info("Adding cards");
        // Add the cards
        // Don't delete these default cards yet. You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        // This method automatically adds any cards so you don't have to manually load them 1 by 1
        // For more specific info, including how to exclude cards from being added:
        // https://github.com/daviscook477/BaseMod/wiki/AutoAdd

        // The ID for this function isn't actually your modid as used for prefixes/by the getModID() method.
        // It's the mod id you give MTS in ModTheSpire.json - by default your artifact ID in your pom.xml

        //TODO: Rename the "DefaultMod" with the modid in your ModTheSpire.json file
        //TODO: The artifact mentioned in ModTheSpire.json is the artifactId in pom.xml you should've edited earlier
        new AutoAdd("BuxomMod") // ${project.artifactId}
            .packageFilter(AbstractDefaultCard.class) // filters to any class in the same package as AbstractDefaultCard, nested packages included
            .setDefaultSeen(true)
            .cards();

        // .setDefaultSeen(true) unlocks the cards
        // This is so that they are all "seen" in the library,
        // for people who like to look at the card list before playing your mod

        logger.info("Done adding cards!");
    }

    // ================ /ADD CARDS/ ===================


    // ================ LOAD THE TEXT ===================
    private static String makeLocPath(Settings.GameLanguage language, String filename)
    {
        String ret = "localization/";
        switch (language) {
            case ZHS:
                ret += "zhs/";
                break;
            case KOR:
                ret += "kor/";
                break;
            default:
                ret += "eng/";
                break;
        }
        return getModID() + "Resources/" + ret + filename + ".json";
    }

    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocPath(Settings.language, "DefaultMod-Card-Strings"));
        logger.info("card strings path: " + makeLocPath(Settings.language, "DefaultMod-Card-Strings"));

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocPath(Settings.language, "DefaultMod-Power-Strings"));

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocPath(Settings.language, "DefaultMod-Relic-Strings"));

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocPath(Settings.language, "DefaultMod-Event-Strings"));

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocPath(Settings.language, "DefaultMod-Potion-Strings"));

        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class, makeLocPath(Settings.language, "DefaultMod-Character-Strings"));

        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class, makeLocPath(Settings.language, "DefaultMod-Orb-Strings"));
        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocPath(Settings.language, "UIStrings"));

        logger.info("Done edittting strings");
    }

    // ================ /LOAD THE TEXT/ ===================

    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

        Gson gson = new Gson();
        String json = Gdx.files.internal(makeLocPath(Settings.language, "DefaultMod-Keyword-Strings")).readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }

    // ================ /LOAD THE KEYWORDS/ ===================    

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    // ================ /AUDIO/ ===================

    @Override
    public void receiveAddAudio()
    {
        BaseMod.addAudio(makeID("GASP"), getModID() + "Resources/audio/gasp.ogg");
        BaseMod.addAudio(makeID("INTENSE_GASP"), getModID() + "Resources/audio/intense_gasp.ogg");
        BaseMod.addAudio(makeID("SUDDEN_GASP"), getModID() + "Resources/audio/sudden_gasp.ogg");
        BaseMod.addAudio(makeID("LOW_GASP"), getModID() + "Resources/audio/low_gasp.ogg");
        BaseMod.addAudio(makeID("WHAT"), getModID() + "Resources/audio/what.ogg");
        BaseMod.addAudio(makeID("HEARTBEAT"), getModID() + "Resources/audio/heartbeat.ogg");
        BaseMod.addAudio(makeID("RIP_SHORT"), getModID() + "Resources/audio/rip2.ogg");
        BaseMod.addAudio(makeID("RIP_LONG"), getModID() + "Resources/audio/riplong.ogg");
        logger.info("Added Audio");
    }

    //utility
    public static List<String> monstersWithBreasts = Arrays.asList(new String[]{"Mystic", "Looter", "Mugger", "Reptomancer", "TheCollector"});
    public static List<String> monstersWithBreastsMaybe = Arrays.asList(new String[]{"TheChamp", "GremlinLeader", "Chosen", "AwakenedOne"});

    public static int getPwrAmt(AbstractCreature check, String ID) {
        AbstractPower found = check.getPower(ID);
        if (found != null) {
            return found.amount;
        }
        return 0;
    }

    public static boolean isMilkEffect(int milkCost) {
        AbstractPlayer p = AbstractDungeon.player;
        logger.info(getPwrAmt(p, MilkPower.POWER_ID));
        logger.info(" milk found");
        logger.info(milkCost);
        logger.info(" milk required");
        if (getPwrAmt(p, MilkPower.POWER_ID) >= milkCost) {
            logger.info("Enough milk found");
            return true;
        }
        logger.info("Not enough milk found");
        return false;
    }
    public static boolean payMilkCost(AbstractPlayer p, int milkCost) {
        if (isMilkEffect(milkCost)) {
            logger.info("Paying milk cost...");
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, MilkPower.POWER_ID, milkCost));
            return true;
        } else {
            logger.info("Not enough milk to pay.");
            return false;
        }
    }
    public static boolean hasBra(AbstractCreature c) {
        for (AbstractPower pow : c.powers) {
            if (pow instanceof BraPower) {
                return true;
            }
        }
        return false;
    }
    public static boolean inBraCapacity(AbstractCreature c) {
        for (AbstractPower pow : c.powers) {
            if (pow instanceof BraPower) {
                if (((BraPower) pow).inCapacity() == true) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getStatusCount(AbstractPlayer p) {

        int statusCount = 0;
        CardGroup statusCardsHand = BuxomMod.specialGetCardsOfType(p.hand, AbstractCard.CardType.STATUS);
        CardGroup statusCardsDraw = BuxomMod.specialGetCardsOfType(p.drawPile, AbstractCard.CardType.STATUS);
        CardGroup statusCardsDiscard = BuxomMod.specialGetCardsOfType(p.discardPile, AbstractCard.CardType.STATUS);
        BuxomMod.logger.info("Hand size: " + statusCardsHand.group.size());
        BuxomMod.logger.info("Draw size: " + statusCardsDraw.group.size());
        BuxomMod.logger.info("Discard size: " + statusCardsDiscard.group.size());
        for (AbstractCard card : statusCardsHand.group) {
            ++statusCount;
            BuxomMod.logger.info("Found status card in hand. Status count is " + statusCount);
            if (card.cardID == BigBounceStatus.ID) {
                ++statusCount;
                BuxomMod.logger.info("Found Big Bounce status card in hand. Status count is " + statusCount);
            } else if (card.cardID.contains("BrokenBra")) {
                statusCount += 2;
                BuxomMod.logger.info("Found Broken Bra status card in hand. Status count is " + statusCount);
            }
        }
        for (AbstractCard card : statusCardsDraw.group) {
            ++statusCount;
            BuxomMod.logger.info("Found status card in draw. Status count is " + statusCount);
            if (card.cardID == BigBounceStatus.ID) {
                ++statusCount;
                BuxomMod.logger.info("Found Big Bounce status card in draw. Status count is " + statusCount);
            } else if (card.cardID.contains("BrokenBra")) {
                statusCount += 2;
                BuxomMod.logger.info("Found Broken Bra status card in draw. Status count  " + statusCount);
            }
        }
        for (AbstractCard card : statusCardsDiscard.group) {
            ++statusCount;
            BuxomMod.logger.info("Found status card in discard. Status count is " + statusCount);
            if (card.cardID == BigBounceStatus.ID) {
                ++statusCount;
                BuxomMod.logger.info("Found Big Bounce status card in discard. Status count is " + statusCount);
            } else if (card.cardID.contains("BrokenBra")) {
                statusCount += 2;
                BuxomMod.logger.info("Found Broken Bra status card in discard. Status count is " + statusCount);
            }
        }
        statusCount += getPwrAmt(p, BraBrokenPower.POWER_ID);
        BuxomMod.logger.info("Final status count is " + statusCount);
        return statusCount;
    }

    public static AbstractCard.CardType getType(AbstractCard card) {
        AbstractCard.CardType type = card.type;
        if (card.hasTag(CustomTags.BOUNCY) == true) {
            type = AbstractCard.CardType.STATUS;
        }
        return type;
    }

    public static CardGroup specialGetCardsOfType(CardGroup group, AbstractCard.CardType cardType) {
        CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Iterator var3 = group.group.iterator();

        while(var3.hasNext()) {
            AbstractCard card = (AbstractCard)var3.next();
            if (getType(card) == cardType) {
                retVal.addToBottom(card);
            }
        }

        return retVal;
    }

    public static boolean stringContainsItemFromList(String inputStr, String[] items)
    {
        for(int i =0; i < items.length; i++)
        {
            if(inputStr.contains(items[i]))
            {
                return true;
            }
        }
        return false;
    }

}
