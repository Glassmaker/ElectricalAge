package mods.eln.sixnode.electricalgatesource;

import java.awt.Color;
import java.util.List;

import org.lwjgl.opengl.GL11;

import mods.eln.Eln;
import mods.eln.cable.CableRenderDescriptor;
import mods.eln.client.ClientProxy;

import mods.eln.misc.IFunction;
import mods.eln.misc.Obj3D;
import mods.eln.misc.Utils;
import mods.eln.misc.Obj3D.Obj3DPart;
import mods.eln.misc.UtilsClient;
import mods.eln.node.six.SixNodeDescriptor;
import mods.eln.sim.ElectricalLoad;
import mods.eln.sim.ThermalLoadInitializer;
import mods.eln.wiki.Data;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;

import com.google.common.base.Function;

public class ElectricalGateSourceDescriptor extends SixNodeDescriptor {


	public boolean onOffOnly;

	public boolean autoReset = false;

	public ElectricalGateSourceDescriptor(String name, ElectricalGateSourceRenderObj render, boolean onOffOnly) {
		super(name, ElectricalGateSourceElement.class, ElectricalGateSourceRender.class);
		this.render = render;
		this.onOffOnly = onOffOnly;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer,
			List list, boolean par4) {
		super.addInformation(itemStack, entityPlayer, list, par4);
		list.add("Provides signal voltage");
		list.add("from user control");
	}

	public void setWithAutoReset() {
		autoReset = true;
	}

	enum ObjType {
		Pot, Button
	};

	ObjType objType;
	float leverTx;
	ElectricalGateSourceRenderObj render;
	void draw(float factor, float distance, TileEntity e) {
		render.draw(factor, distance, e);
	}
	@Override
	public boolean use2DIcon() {
		return false;
	}
	@Override
	public void setParent(Item item, int damage) {
		super.setParent(item, damage);
		Data.addSignal(newItemStack());
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelperEln(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glScalef(1.5f, 1.5f, 1.5f);
		if (type == ItemRenderType.INVENTORY) GL11.glScalef(1.5f, 1.5f, 1.5f);
		draw(0f, 1f, null);
	}
}
