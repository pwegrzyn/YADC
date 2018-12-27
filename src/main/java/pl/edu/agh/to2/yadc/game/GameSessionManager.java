package pl.edu.agh.to2.yadc.game;

import java.util.Random;

import pl.edu.agh.to2.yadc.area.Area;
import pl.edu.agh.to2.yadc.area.AreaManager;
import pl.edu.agh.to2.yadc.config.GlobalConfig;
import pl.edu.agh.to2.yadc.entity.MeleeMob;
import pl.edu.agh.to2.yadc.entity.MobFactory;
import pl.edu.agh.to2.yadc.entity.Player;
import pl.edu.agh.to2.yadc.entity.RangedMob;
import pl.edu.agh.to2.yadc.hud.HUD;
import pl.edu.agh.to2.yadc.input.InputManager;
import pl.edu.agh.to2.yadc.quest.QuestBoard;
import pl.edu.agh.to2.yadc.render.ImageLoader;
import pl.edu.agh.to2.yadc.render.RenderManager;

public class GameSessionManager {

    private static InputManager inputManagerComp;
    private static ImageLoader imageLoaderComp;
    private static HUD hudComp;
    private static AreaManager areaManagerComp;
    private static RenderManager renderManagerComp;
    private static boolean firstGameRun = true;
    private static Thread runningThread;
    private static boolean isThreadActive = false;

    public static void stopCurrentSession() {
        if(!isThreadActive) return;
        runningThread.stop();
        runningThread = null;
        isThreadActive = false;
    }

    public static void newGameSession() {

        runningThread = new Thread() {

            @Override
            public void run() {
                Player player = new Player(100, 200);
                player.setInputManager(inputManagerComp);
                player.setTexture(imageLoaderComp.fetchImage("resources/test_entity.png"));
                player.setProjectileTexture(imageLoaderComp.fetchImage("resources/minibullet.png"));
                hudComp.bindPlayer(player);

                Area area = new Area("Knowhere");
                area.setTexture(imageLoaderComp.fetchImage("resources/grass_area.png"));
                area.addEntity(player);

                player.setArea(area);

                areaManagerComp.setCurrentArea(area);

                if(firstGameRun) {
                    renderManagerComp.startRendering(areaManagerComp);
                    firstGameRun = false;
                } else {
                    GlobalConfig.get().setFrozenRender(false);
                }
            
                MobFactory factory = new MobFactory();
            
                hudComp.printToChatBox("Started a new game session.");
            
                Random random = new Random();

                QuestBoard questBoard = new QuestBoard(50, 50);
                questBoard.setTexture(imageLoaderComp.fetchImage("resources/questboard.png"));
                area.addEntity(questBoard);

                for (;;) {
                    int randomLocX = random.nextInt(500 + 1 - 100) + 100;
                    int randomLocY = random.nextInt(500 + 1 - 100) + 100;
                    RangedMob mob = (RangedMob) factory.createRangedMob(randomLocX, randomLocY, 10,
                            imageLoaderComp.fetchImage("resources/ranged_enemy.png"),
                            imageLoaderComp.fetchImage("resources/arrow.png"));
                    area.addEntity(mob);
                    randomLocX = random.nextInt(500 + 1 - 100) + 100;
                    randomLocY = random.nextInt(500 + 1 - 100) + 100;
                    MeleeMob mob2 = (MeleeMob) factory.createMeleeMob(randomLocX, randomLocY, 10,
                            imageLoaderComp.fetchImage("resources/melee_enemy.png"));
                    area.addEntity(mob2);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };

        isThreadActive = true;
        runningThread.start();
    }
    
    public static void init(InputManager inputManager, ImageLoader imageLoader, HUD hud, AreaManager areaManager, RenderManager renderManager) {
        inputManagerComp = inputManager;
        imageLoaderComp = imageLoader;
        hudComp = hud;
        areaManagerComp = areaManager;
        renderManagerComp = renderManager;
    }

}