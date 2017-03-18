package jamalian.sina.survivalswim.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import jamalian.sina.survivalswim.SurvivalSwim;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SurvivalSwim.WIDTH;
		config.height = SurvivalSwim.HEIGHT;
		config.title = SurvivalSwim.TITLE;
		new LwjglApplication(new SurvivalSwim(), config);
	}
}
