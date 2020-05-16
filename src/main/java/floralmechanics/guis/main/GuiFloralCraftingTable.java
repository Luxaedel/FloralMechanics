package floralmechanics.guis.main;

import floralmechanics.containers.main.ContainerFloralCraftingTable;
import floralmechanics.tileentities.main.TileEntityFloralCraftingTable;
import floralmechanics.util.Constants;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiFloralCraftingTable extends GuiContainer {
	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/floral_crafting_table.png");
	protected final TileEntityFloralCraftingTable te;
	protected final InventoryPlayer player;
	
	public GuiFloralCraftingTable(InventoryPlayer player, World worldIn, BlockPos pos, TileEntityFloralCraftingTable te) {
		super(new ContainerFloralCraftingTable(player, worldIn, pos, te));
		this.te = te;
		this.player = player;
		this.xSize = 176;
		this.ySize = 209;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String tileName = this.te.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString("Floral crafting", (this.xSize / 2 - this.fontRenderer.getStringWidth("Floral crafting") / 2), 8, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 119, this.ySize - 96 + 2, 4210752);
	}
}