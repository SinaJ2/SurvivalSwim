package jamalian.sina.survivalswim.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Sina on 02/10/16.
 */
public class ScoreState extends State {
    // Background texture.
    private Texture background;

    // Back button.
    private Texture backBtn;
    private Rectangle backBound;

    // Reset button.
    private Texture resetBtn;
    private Circle resetBound;
    private float diameter;

    // Used to display high score.
    BitmapFont displayHighScore;
    BitmapFont displayScore;
    GlyphLayout layoutHighScore;
    GlyphLayout layoutScore;

    // Keep track of high score.
    private int score;
    private String scoreText;
    Preferences prefs;

    public ScoreState(GameStateManager gsm) {
        super(gsm);

        // Load textures.
        background = new Texture("bg.png");
        backBtn = new Texture("back.png");
        resetBtn = new Texture("reset.png");

        // Set play button bound dimensions and location.
        float buttonWidth = cam.viewportWidth/2;
        float buttonHeight = buttonWidth * ((float)backBtn.getHeight()/((float)backBtn.getWidth()));
        backBound = new Rectangle(cam.position.x - buttonWidth/2, cam.position.y - (cam.viewportHeight/2) + buttonHeight, buttonWidth, buttonHeight);

        // Set reset button bound.
        diameter = 69/2;
        resetBound = new Circle(cam.position.x, cam.position.y - diameter - 10, diameter/2);

        // Display high score text.
        displayHighScore = new BitmapFont(Gdx.files.internal("font2.fnt"),Gdx.files.internal("font2.png"),false);
        displayHighScore.getData().setScale(.4f);
        layoutHighScore = new GlyphLayout(displayHighScore, "HIGH SCORE");

        // Load high score.
        prefs = Gdx.app.getPreferences("My Preferences");
        score = prefs.getInteger("score", 0);
        scoreText = "" + score;

        // Display high score.
        displayScore = new BitmapFont(Gdx.files.internal("font2.fnt"),Gdx.files.internal("font2.png"),false);
        displayScore.getData().setScale(.35f);
        layoutScore = new GlyphLayout(displayScore, scoreText);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchPos);

            // Go back to main menu.
            if (backBound.contains(touchPos.x, touchPos.y)) {
                gsm.set(new MenuState(gsm));
            }
            // Reset high score.
            else if (resetBound.contains(touchPos.x, touchPos.y)) {
                prefs = Gdx.app.getPreferences("My Preferences");
                prefs.putInteger("score", 0);
                prefs.flush();
                score = prefs.getInteger("score", 0);
                scoreText = "" + score;
                layoutScore = new GlyphLayout(displayScore, scoreText);
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

        // Display high score text.
        displayHighScore.setUseIntegerPositions(false);
        displayHighScore.draw(sb, layoutHighScore, cam.position.x - (layoutHighScore.width / 2), cam.position.y + (cam.viewportHeight / 3));

        // Display high score.
        displayScore.setUseIntegerPositions(false);
        displayScore.draw(sb, layoutScore, cam.position.x - (layoutScore.width / 2), cam.position.y + 30);

        // Display reset and back buttons.
        float buttonWidth = cam.viewportWidth/2;
        float buttonHeight = buttonWidth * ((float)backBtn.getHeight()/((float)backBtn.getWidth()));
        sb.draw(backBtn, cam.position.x - buttonWidth/2, cam.position.y - (cam.viewportHeight/2) + buttonHeight, buttonWidth, buttonHeight);
        sb.draw(resetBtn, cam.position.x - (diameter/2), cam.position.y - (diameter*3/2) - 10, diameter, diameter);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        backBtn.dispose();
    }
}
