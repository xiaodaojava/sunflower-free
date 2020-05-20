package red.lixiang.tools.desktop.utils.notification.notification;

public enum Notifications implements Notification {

	INFORMATION("/tools/notification/images/info.png", "#2C54AB"),
	NOTICE("/tools/notification/images/notice.png", "#8D9695"),
	SUCCESS("/tools/notification/images/success.png", "#009961"),
	WARNING("/tools/notification/images/warning.png", "#E23E0A"),
	ERROR("/tools/notification/images/error.png", "#CC0033");

	private final String urlResource;
	private final String paintHex;

	Notifications(String urlResource, String paintHex) {
		this.urlResource = urlResource;
		this.paintHex = paintHex;
	}

	@Override
	public String getURLResource() {
		return urlResource;
	}

	@Override
	public String getPaintHex() {
		return paintHex;
	}

}
