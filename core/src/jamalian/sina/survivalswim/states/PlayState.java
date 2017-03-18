package jamalian.sina.survivalswim.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import jamalian.sina.survivalswim.SurvivalSwim;
import jamalian.sina.survivalswim.sprites.Fish;
import jamalian.sina.survivalswim.sprites.Hook;
import jamalian.sina.survivalswim.sprites.Plant;

/**
 * Created by Sina on 02/10/16.
 */
public class PlayState extends State {
    // How many objects will we keep track of at one time (on each side top/bottom).
    private static final int OBSTACLE_COUNT = 4;
    // How high the ground will be drawn.
    private static final int GROUND_Y_OFFSET = 0;

    // The horizontal gap between objects.
    private int obstacleGap;

    // For the game music.
    private Music music;

    // Sound made when fish collides and dies.
    private Sound gameOver;

    // Keep track of the fish (player).
    private Fish fish;

    // Background and ground.
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;

    // Pause and play buttons.
    private Texture pauseBtn;
    private Rectangle pauseBound;
    private Texture playBtn;
    private Rectangle playBound;

    // Keep track of obstacles.
    private Array<Hook> hooks;
    private Array<Plant> plants;

    // Keep track of score.
    private int score;
    private String scoreText;
    BitmapFont displayScore;
    GlyphLayout layoutCurrentScore;

    // Quit and restart buttons after game ends.
    private Texture quitBtn;
    private Texture restartBtn;
    private Rectangle quitBound;
    private Rectangle restartBound;
    private Texture deadfish;

    // Display game over and final score.
    BitmapFont displayGameOver;
    BitmapFont displayFinalScore, displayFinalScore2;
    GlyphLayout layoutGameOver;
    GlyphLayout layoutScore, layoutScore2;

    // For saved high score.
    Preferences prefs;
    boolean newHighScore;

    // Max portion of obstacle gap obstacle width can be.
    private float widthFactor;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        // Play the game music.
        music = Gdx.audio.newMusic(Gdx.files.internal("music2.wav"));
        music.setLooping(true);
        music.setVolume(1.0f);
        music.play();

        // Sound played when game over.
        gameOver = Gdx.audio.newSound(Gdx.files.internal("gameover.wav"));

        widthFactor = 1.0f/2.0f;

        // For the current score.
        score = 0;
        scoreText = "0";
        displayScore = new BitmapFont(Gdx.files.internal("font.fnt"),Gdx.files.internal("font.png"),false);
        displayScore.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        displayScore.getData().setScale(.2f);
        layoutCurrentScore = new GlyphLayout(displayScore, scoreText);

        // Display game over and final score.
        displayGameOver = new BitmapFont(Gdx.files.internal("font2.fnt"),Gdx.files.internal("font2.png"),false);
        displayGameOver.getData().setScale(.4f);
        layoutGameOver = new GlyphLayout(displayGameOver, "GAME OVER");
        displayFinalScore = new BitmapFont(Gdx.files.internal("font2.fnt"),Gdx.files.internal("font2.png"),false);
        displayFinalScore.getData().setScale(.25f);
        displayFinalScore2 = new BitmapFont(Gdx.files.internal("font2.fnt"),Gdx.files.internal("font2.png"),false);
        displayFinalScore2.getData().setScale(.25f);
        layoutScore = new GlyphLayout(displayFinalScore, "SCORE");
        layoutScore2 = new GlyphLayout(displayFinalScore2, scoreText);

        // Create the fish.
        obstacleGap = (int)(cam.viewportWidth*2/3);
        fish = new Fish(80, 300, obstacleGap/6);
        deadfish = new Texture("deadfish.png");

        // Set up the background and ground.
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth/2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth/2) + ground.getWidth(), GROUND_Y_OFFSET);

        // Set up the pause, play, quit, and restart buttons.
        pauseBtn = new Texture("pause.png");
        playBtn = new Texture("play.png");
        pauseBound = new Rectangle((cam.position.x - cam.viewportWidth/2) + 20, (ground.getHeight() / 2) - (pauseBtn.getHeight() / 2), pauseBtn.getWidth(), pauseBtn.getHeight());
        playBound = new Rectangle(cam.position.x - (playBtn.getWidth()/2), cam.position.y + (ground.getHeight()/2) - (playBtn.getHeight()/2), playBtn.getWidth(), playBtn.getHeight());
        quitBtn = new Texture("quit.png");
        restartBtn = new Texture("restart.png");
        float buttonWidth = cam.viewportWidth/2;
        float buttonHeight = buttonWidth * ((float)restartBtn.getHeight()/((float)restartBtn.getWidth()));
        restartBound = new Rectangle(cam.position.x - buttonWidth/2, cam.position.y - (cam.viewportHeight/2) + (buttonHeight*3), buttonWidth, buttonHeight);
        quitBound = new Rectangle(cam.position.x - buttonWidth/2, cam.position.y - (cam.viewportHeight/2) + buttonHeight, buttonWidth, buttonHeight);

        // Set up the obstacles.
        hooks = new Array<Hook>();
        plants = new Array<Plant>();

        for (int i = 1; i <= OBSTACLE_COUNT; i++) {
            hooks.add(new Hook(150 + i * (obstacleGap + Hook.HOOK_WIDTH), (int)cam.viewportHeight, obstacleGap));
            plants.add(new Plant(hooks.get(i-1).getPosHook().x, hooks.get(i-1).getPosHook().y, obstacleGap, fish.getBounds().getHeight()*5));
        }

        newHighScore = false;
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            // Project: Turns world co-ordinates into screen co-ordinates.
            // Unproject: Turns screen co-ordinates into world co-ordinates.
            cam.unproject(touchPos);

            // Update button positions.
            pauseBound.setPosition((cam.position.x - cam.viewportWidth / 2) + 20, (ground.getHeight() / 2) - (pauseBtn.getHeight() / 2));
            playBound.setPosition(cam.position.x - (playBtn.getWidth() / 2), cam.position.y + (ground.getHeight() / 2) - (playBtn.getHeight() / 2));
            float buttonWidth = cam.viewportWidth/2;
            float buttonHeight = buttonWidth * ((float)restartBtn.getHeight()/((float)restartBtn.getWidth()));
            restartBound.setPosition(cam.position.x - buttonWidth/2, cam.position.y - (cam.viewportHeight/2) + (buttonHeight*3));
            quitBound.setPosition(cam.position.x - buttonWidth/2, cam.position.y - (cam.viewportHeight/2) + buttonHeight);

            // Checks if game is over, game is paused, or game is in progress.
            if (SurvivalSwim.isGameOver) {
                // Game over, quit to main menu or restart game.
                if (quitBound.contains(touchPos.x, touchPos.y)) {
                    gsm.set(new MenuState(gsm));
                    gameOver.stop();
                }
                else if (restartBound.contains(touchPos.x, touchPos.y)) {
                    SurvivalSwim.isGameOver = false;
                    gsm.set(new PlayState(gsm));
                    gameOver.stop();
                }
            }
            else if (SurvivalSwim.isPaused) {
                // Game is paused, check to see if user resumed play.
                if (touchPos.y > (ground.getHeight() + GROUND_Y_OFFSET)) {
                    SurvivalSwim.isPaused = false;
                    music.play();
                }
                else {
                    music.pause();
                }
            }
            else {
                // Game is in progress, check to see if user paused or tapped to move fish.
                if (pauseBound.contains(touchPos.x, touchPos.y)) {
                    SurvivalSwim.isPaused = true;
                    music.pause();
                }
                else {
                    fish.jump();
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        // Update position of ground and fish.
        updateGround();

        float scale = (float)fish.getFishFrames().getRegionHeight()/(float)(fish.getFishFrames().getRegionWidth());
        float fishHeight = obstacleGap/6*scale;
        float maxHeight = cam.viewportHeight-fishHeight;
        fish.update(dt, ground.getHeight() + GROUND_Y_OFFSET, maxHeight);

        // Update camera's position in game world based on fish's position (to follow it).
        cam.position.x = fish.getPosition().x + 80;

        // Check if fish has passed obstacles or collided into one.
        for (int i = 0; i < hooks.size; i++) {
            Hook hook = hooks.get(i);
            Plant plant = plants.get(i);

            // Increase score if fish passes obstacle.
            if ((fish.getPosition().x > (hook.getPosHook().x + hook.getWidth())) && !hook.isPassed() && (fish.getPosition().x > (plant.getPosPlant().x + plant.getWidth())) && !plant.isPassed()) {
                hook.setPassed(true);
                plant.setPassed(true);
                score += 10;
                scoreText = "" + score;
            }

            // Reposition obstacles that can no longer be seen.
            if ((cam.position.x - cam.viewportWidth/2 > hook.getPosHook().x + hook.getHook().getWidth()) &&
                    (cam.position.x - cam.viewportWidth/2 > plant.getPosPlant().x + plant.getPlant().getWidth())) {
                hook.reposition(hook.getPosHook().x + ((Hook.HOOK_WIDTH + obstacleGap) * OBSTACLE_COUNT), (int)cam.viewportHeight, obstacleGap);
                plant.reposition(hooks.get(i).getPosHook().x, hooks.get(i).getPosHook().y, obstacleGap, fish.getBounds().getHeight()*5);
            }

            // Check for a collision between the fish and an obstacle.
            if (hook.collides(fish.getBounds()) || plant.collides(fish.getBounds())) {
                SurvivalSwim.isGameOver = true;
                music.stop();
                gameOver.play(0.5f);

                // Update high score.
                prefs = Gdx.app.getPreferences("My Preferences");
                int highScore = prefs.getInteger("score", 0);
                if (score > highScore) {
                    prefs.putInteger("score", score);
                    prefs.flush();
                    newHighScore = true;
                }
            }
        }

        // Have to update camera any time you change its position.
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        // Draw the background.
        sb.draw(bg, cam.position.x - cam.viewportWidth / 2, 0);

        // If game is in progress, render everything based on new positions.
        if (!SurvivalSwim.isGameOver) {
            float scale = (float)fish.getFishFrames().getRegionHeight()/(float)(fish.getFishFrames().getRegionWidth());
            // Draw the fish.
            sb.draw(fish.getFishFrames(), fish.getPosition().x, fish.getPosition().y, obstacleGap /6, obstacleGap /6*scale);

            // Draw the obstacles.
            for (Hook hook : hooks) {
                int select = hook.getSelect();
                // Octopus, turtle or shark.
                if (select >= 4) {
                    if (select == 6) {
                        widthFactor = 2.0f/3.0f;
                    }
                    else {
                        widthFactor = 1.0f/2.0f;
                    }
                    float scale2 = (float)hook.getHook().getHeight()/(float)hook.getHook().getWidth();
                    sb.draw(hook.getHook(), hook.getPosHook().x, hook.getPosHook().y, obstacleGap*widthFactor, obstacleGap*widthFactor*scale2);
                }
                // Hook.
                else {
                    sb.draw(hook.getHook(), hook.getPosHook().x, hook.getPosHook().y);
                }
            }
            for (Plant plant : plants) {
                int select = plant.getSelect();
                // Octopus, turtle or shark.
                if (select >= 6) {
                    if (select == 8) {
                        widthFactor = 2.0f/3.0f;
                    }
                    else {
                        widthFactor = 1.0f/2.0f;
                    }
                    float scale2 = (float)plant.getPlant().getHeight()/(float)plant.getPlant().getWidth();
                    sb.draw(plant.getPlant(), plant.getPosPlant().x, plant.getPosPlant().y, obstacleGap*widthFactor, obstacleGap*widthFactor*scale2);
                }
                // Plant.
                else {
                    sb.draw(plant.getPlant(), plant.getPosPlant().x, plant.getPosPlant().y);
                }
            }

            // Draws the ground.
            sb.draw(ground, groundPos1.x, groundPos1.y);
            sb.draw(ground, groundPos2.x, groundPos2.y);

            // Draws the pause button.
            sb.draw(pauseBtn, (cam.position.x - cam.viewportWidth / 2) + 20, (ground.getHeight() / 2) - (pauseBtn.getHeight() / 2));

            // Draws the play button if paused.
            if (SurvivalSwim.isPaused) {
                sb.draw(playBtn, cam.position.x - (playBtn.getWidth() / 2), cam.position.y + (ground.getHeight() / 2) - (playBtn.getHeight() / 2));
            }

            // Displays the current score.
            displayScore.setUseIntegerPositions(false);
            if (SurvivalSwim.isPaused) {
                layoutCurrentScore.setText(displayScore, "PAUSED");
            }
            else {
                layoutCurrentScore.setText(displayScore, scoreText);
            }
            displayScore.draw(sb, layoutCurrentScore, (cam.position.x - cam.viewportWidth / 2) + pauseBtn.getWidth() + 40, (ground.getHeight() / 2) + (layoutCurrentScore.height/2));
        }
        // Game is over.
        else {
            float buttonWidth = cam.viewportWidth/2;
            float buttonHeight = buttonWidth * ((float)restartBtn.getHeight()/((float)restartBtn.getWidth()));

            // Display game over.
            displayGameOver.setUseIntegerPositions(false);
            displayGameOver.draw(sb, layoutGameOver, cam.position.x - (layoutGameOver.width / 2), cam.position.y + (cam.viewportHeight / 3));

            // Calculate width and height of dead fish and scale to maintain aspect ratio.
            float scale = (float)deadfish.getHeight()/(float)deadfish.getWidth();
            float width = obstacleGap/6;
            float height = obstacleGap/6*scale;

            // Display the final score.
            if (newHighScore) {
                layoutScore.setText(displayFinalScore, "NEW HIGH SCORE");
            }
            displayFinalScore.setUseIntegerPositions(false);
            displayFinalScore2.setUseIntegerPositions(false);
            layoutScore2.setText(displayFinalScore2, scoreText);
            displayFinalScore.draw(sb, layoutScore, cam.position.x - (layoutScore.width/2), cam.position.y - (cam.viewportHeight/2) + (buttonHeight*4) + (height*8) + layoutScore2.height + 10);
            displayFinalScore2.draw(sb, layoutScore2, cam.position.x - (layoutScore2.width/2), cam.position.y - (cam.viewportHeight/2) + (buttonHeight*4) + (height*8));

            // Draw the dead fish.
            sb.draw(deadfish, cam.position.x - (width / 2), cam.position.y - (cam.viewportHeight/2) + (buttonHeight*4) + (height*3), width, height);

            // Show the restart and quit buttons.
            sb.draw(restartBtn, cam.position.x - buttonWidth / 2, cam.position.y - (cam.viewportHeight/2) + (buttonHeight * 3), buttonWidth, buttonHeight);
            sb.draw(quitBtn, cam.position.x - buttonWidth / 2, cam.position.y - (cam.viewportHeight / 2) + buttonHeight, buttonWidth, buttonHeight);
        }
        sb.end();
    }

    // Updates position of ground texture.
    private void updateGround() {
        if (cam.position.x - cam.viewportWidth/2 > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }
        if (cam.position.x - cam.viewportWidth/2 > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }

    @Override
    public void dispose() {
        bg.dispose();
        fish.dispose();
        ground.dispose();
        deadfish.dispose();
        quitBtn.dispose();
        restartBtn.dispose();
        music.dispose();
        gameOver.dispose();
        for (Hook hook : hooks) {
            hook.dispose();
        }
        for (Plant plant : plants) {
            plant.dispose();
        }
    }
}
