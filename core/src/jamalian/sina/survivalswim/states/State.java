package jamalian.sina.survivalswim.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import jamalian.sina.survivalswim.SurvivalSwim;

/**
 * Created by Sina on 02/10/16.
 */
public abstract class State {
    // Camera to locate position in the world.
    protected OrthographicCamera cam;
    // Mouse/pointer for Vector3 (XYZ) co-ordinate system.
    protected Vector3 mouse;
    // Manage states.
    protected GameStateManager gsm;
    // Horizontal spacing between objects.
    protected float horizontalSpacing;

    protected State(GameStateManager gsm) {
        this.gsm = gsm;

        // Setup camera and how much of the game area will be seen.
        cam = new OrthographicCamera();
        cam.setToOrtho(false, SurvivalSwim.WIDTH / 2, SurvivalSwim.HEIGHT / 2);

        // Horizontal spacing between objects is 2/3 of view area.
        horizontalSpacing = cam.viewportWidth*2/3;

        // Used to track user input.
        mouse = new Vector3();
    }

    protected abstract void handleInput();

    // dt = delta time
    // Difference between one frame rendered and the next frame rendered.
    public abstract void update(float dt);

    // SpriteBatch is a container for everything we need to render to the screen (eg. textures).
    public abstract void render(SpriteBatch sb);

    // Dispose textures when done with them to prevent memory leaks.
    public abstract void dispose();
}
