package jamalian.sina.survivalswim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import jamalian.sina.survivalswim.states.GameStateManager;
import jamalian.sina.survivalswim.states.MenuState;

public class SurvivalSwim extends ApplicationAdapter {
	// Specify screen dimensions and title.
    public static final int WIDTH = 420;
    public static final int HEIGHT = 700;
	public static final String TITLE = "Survival Swim";

	// Manage the game's states.
	private GameStateManager gsm;
	// Used to render the textures.
	private SpriteBatch batch;

    // Keep track if game is paused or game over.
	public static boolean isPaused;
	public static boolean isGameOver;

	// For menu music.
	public static Music music;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		gsm = new GameStateManager();

		// Clears the screen.
		Gdx.gl.glClearColor(1, 0, 0, 1);

		isPaused = false;
		isGameOver = false;

		// Set up the menu music.
		music = Gdx.audio.newMusic(Gdx.files.internal("menumusic.mp3"));
		music.setLooping(true);
		// 0.1f is 10% of max volume.
		music.setVolume(1.0f);

		// Start the first state: displays the menu.
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render() {
		// Check for user action.
		gsm.handleInput();
		// Update is game is in progress.
		if (!isPaused && !isGameOver) {
			gsm.update(Gdx.graphics.getDeltaTime());
		}

		// Render changes.
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.render(batch);
	}

	@Override
	public void pause() {
		super.pause();
		isPaused = true;
	}

	@Override
    public void dispose() {
		music.dispose();
        super.dispose();
    }
}
