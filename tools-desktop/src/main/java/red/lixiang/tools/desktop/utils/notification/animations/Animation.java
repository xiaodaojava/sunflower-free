package red.lixiang.tools.desktop.utils.notification.animations;

import javafx.util.Duration;
import red.lixiang.tools.desktop.utils.notification.models.CustomStage;

public interface Animation {

	/**
	 * Plays both the show and dismiss animation using a sequential transition object
	 *
	 * @param dismissDelay Amount of delay before starting the dismiss animation
	 */
	void playSequential(Duration dismissDelay);

	/**
	 * Plays the implemented show animation
	 */
	void playShowAnimation();

	/**
	 * Plays the implemented dismiss animation
	 */
	void playDismissAnimation();

	/**
	 * @return whether or not the tray is current showing
	 */
	boolean isShowing();

	/**
	 * @return The custom stage of the animation
	 */
	CustomStage getStage();

}
