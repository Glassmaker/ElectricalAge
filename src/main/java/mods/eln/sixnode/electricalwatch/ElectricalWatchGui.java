package mods.eln.sixnode.electricalwatch;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import mods.eln.gui.GuiContainerEln;
import mods.eln.gui.GuiHelper;
import mods.eln.gui.GuiHelperContainer;
import mods.eln.gui.GuiTextFieldEln;
import mods.eln.gui.HelperStdContainer;
import mods.eln.gui.HelperStdContainerSmall;
import mods.eln.gui.IGuiObject;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ElectricalWatchGui extends GuiContainerEln {

	public ElectricalWatchGui(EntityPlayer player, IInventory inventory, ElectricalWatchRender render) {
		super(new ElectricalWatchContainer(player, inventory));
		this.render = render;
	}

	ElectricalWatchRender render;

	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	protected GuiHelperContainer newHelper() {
		return new GuiHelperContainer(this, 176, 166 - 52, 8, 84 - 52);
	}
}
