package jamalian.sina.survivalswim.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import jamalian.sina.survivalswim.SurvivalSwim;

/**
 * Created by Sina on 02/10/16.
 */
public class MenuState extends State {
    // Textures for background, play button, high score button, and fish.
    private Texture background;
    private Texture playBtn;
    private Texture hsBtn;
    private Texture fish;

    // Bounds for play button and high score button.
    private Rectangle playBound;
    private float playBtnX;
    private float playBtnY;
    private Circle hsBound;

    // Used to display game title Survival Swim.
    BitmapFont displayTitle, displayTitle2;
    GlyphLayout title1, title2;

    public MenuState(GameStateManager gsm) {
        super(gsm);

        // Play music.
        SurvivalSwim.music.play();

        // Load textures.
        background = new Texture("bg.png");
        playBtn = new Texture("play2.png");
        hsBtn = new Texture("hs.png");
        fish = new Texture("livefish.png");

        // Set play button bound dimensions and location.
        playBtnX = cam.position.x - playBtn.getWidth()/2;
        playBtnY = cam.position.y - playBtn.getHeight()/2 - 60;
        playBound = new Rectangle(playBtnX, playBtnY, playBtn.getWidth(), playBtn.getHeight());

        // Set high score button bound.
        hsBound = new Circle(cam.position.x, playBtnY - (hsBtn.getHeight()/2) - 10, hsBtn.getHeight()/2);

        // For displaying game title.
        displayTitle = new BitmapFont(Gdx.files.internal("font2.fnt"),Gdx.files.internal("font2.png"),false);
        displayTitle.getData().setScale(.4f);
        displayTitle2 = new BitmapFont(Gdx.files.internal("font2.fnt"),Gdx.files.internal("font2.png"),false);
        displayTitle2.getData().setScale(.4f);
        title1 = new GlyphLayout(displayTitle, "SURVIVAL");
        title2 = new GlyphLayout(displayTitle2, "SWIM");
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPos);

            // Check if user pressed the play button to start the game.
            if (playBound.contains(touchPos.x, touchPos.y)) {
                SurvivalSwim.isGameOver = false;
                SurvivalSwim.isPaused = false;
                SurvivalSwim.music.stop();
                gsm.set(new PlayState(gsm));
            }
            // Check if user pressed high score button.
            else if (hsBound.contains(touchPos.x, touchPos.y)) {
                gsm.set(new ScoreState(gsm));
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        // Added for Android.
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        // Draw background.
        sb.draw(background, 0, 0);

        // Display game title.
        displayTitle.setUseIntegerPositions(false);
        displayTitle2.setUseIntegerPositions(false);
        float title1Y = cam.position.y + (cam.viewportHeight / 3);
        float title2Y = title1Y - title1.height - 20;
        displayTitle.draw(sb, title1, cam.position.x - (title1.width/2), title1Y);
        displayTitle2.draw(sb, title2, cam.position.x - (title2.width/2), title2Y);

        // Fish's width is determined by screen width.
        // scaleHeight is to maintain fish texture's aspect ratio.
        float scaleHeight = (float)fish.getHeight()/(float)fish.getWidth();
        float width = horizontalSpacing/6;
        float height = horizontalSpacing/6*scaleHeight;

        // Display play button.
        sb.draw(playBtn, playBtnX, playBtnY);

        // Display high score button.
        float hsY = playBtnY - hsBtn.getHeight() - 10;
        sb.draw(hsBtn, cam.position.x - (hsBtn.getWidth()/2), hsY);

        // Display fish.
        sb.draw(fish, cam.position.x - (width/2), playBtnY + playBtn.getHeight() + 30, width, height);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        hsBtn.dispose();
        fish.dispose();
    }
}
