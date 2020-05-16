package floralmechanics.guis.generators;

import java.util.ArrayList;
import java.util.List;

import floralmechanics.containers.generators.ContainerGeneratorBase;
import floralmechanics.tileentities.generators.TileEntityGeneratorBase;
import floralmechanics.util.Constants;
import floralmechanics.util.GuiHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiGeneratorBase extends GuiContainer {
	protected ResourceLocation TEXTURES = new ResourceLocation(Constants.MODID + ":textures/gui/generators/floral/floral_generator.png");
	protected final InventoryPlayer player;
	protected final TileEntityGeneratorBase te;
	
	public GuiGeneratorBase(InventoryPlayer player, TileEntityGeneratorBase te, ContainerGeneratorBase cgb, String guiPath) {
		super(cgb);
		this.player = player;
		this.te = te;
		this.TEXTURES = new ResourceLocation(Constants.MODID + ":textures/gui/generators/" + guiPath + ".png");
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		drawBars();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String tileName = this.te.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(tileName, (this.xSize / 2 - this.fontRenderer.getStringWidth(tileName) / 2), 8, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 122, this.ySize - 96 + 2, 4210752);
		drawPowerHover(mouseX, mouseY);
	}
	
	protected int getEnergyStoredScaled(int pixels) {
		int i = this.te.getEnergyStored();
		int j = this.te.getMaxEnergyStored();
		return i != 0 && j != 0 ? i * pixels / j : 0;
	}
	
	protected void drawBars() {
		int k = this.getEnergyStoredScaled(59);
		this.drawTexturedModalRect(this.guiLeft + 151, this.guiTop + 12 + 59 - k, 176, 59 - k, 16, k + 1);
	}
	
	protected void drawPowerHover(int mouseX, int mouseY) {
		if (GuiHelper.isMouseInRect(this.guiLeft + 151, this.guiTop + 14, 16, 59, mouseX, mouseY)) {
			int k = (this.width - this.xSize) / 2;
			int l = (this.height - this.ySize) / 2;
			List<String> list = new ArrayList<String>();
			list.add(TextFormatting.RED + "Power: ");
			list.add(TextFormatting.RED + (this.te.getEnergyStored() + " FE / " + this.te.getMaxEnergyStored() + " FE"));
			this.drawHoveringText(list, mouseX - k, mouseY - l, this.fontRenderer);
		}
	}
}