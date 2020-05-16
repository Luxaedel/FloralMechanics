package floralmechanics.util;

public class GuiHelper {
	public static boolean isMouseInRect(int guiLeft, int guiTop, int guiWidth, int guiHeight, int mouseX, int mouseY) {
		return ((mouseX >= guiLeft && mouseX <= guiLeft + guiWidth) && (mouseY >= guiTop && mouseY <= guiTop + guiHeight));
	}
}