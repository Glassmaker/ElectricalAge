package mods.eln.sixnode.electricalrelay;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import mods.eln.Eln;
import mods.eln.cable.CableRender;
import mods.eln.cable.CableRenderDescriptor;
import mods.eln.client.ClientProxy;
import mods.eln.misc.Direction;
import mods.eln.misc.LRDU;
import mods.eln.misc.Obj3D;
import mods.eln.misc.RcInterpolator;
import mods.eln.misc.Utils;
import mods.eln.misc.UtilsClient;
import mods.eln.misc.Obj3D.Obj3DPart;
import mods.eln.node.NodeBase;
import mods.eln.node.six.SixNodeDescriptor;
import mods.eln.node.six.SixNodeElementInventory;
import mods.eln.node.six.SixNodeElementRender;
import mods.eln.node.six.SixNodeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ElectricalRelayRender extends SixNodeElementRender {

	SixNodeElementInventory inventory = new SixNodeElementInventory(0, 64, this);
	ElectricalRelayDescriptor descriptor;
	long time;
	
	public ElectricalRelayRender(SixNodeEntity tileEntity, Direction side, SixNodeDescriptor descriptor) {
		super(tileEntity, side, descriptor);
		this.descriptor = (ElectricalRelayDescriptor) descriptor;
		time = System.currentTimeMillis();
		interpolator = new RcInterpolator(this.descriptor.speed);
	}

	RcInterpolator interpolator;
	
	@Override
	public void draw() {
		super.draw();
		//UtilsClient.enableDepthTest();
		drawSignalPin(front,new float[]{2.5f,2.5f,2.5f,2.5f});
		front.glRotateOnX();
		

		descriptor.draw(interpolator.get());
	}
	
	@Override
	public void refresh(float deltaT) {
		interpolator.step(deltaT);
	}
	
	boolean boot = true;
	float switchAlpha = 0;
	public boolean switchState, defaultOutput;
	
	@Override
	public void publishUnserialize(DataInputStream stream) {
		super.publishUnserialize(stream);
		try {
			switchState = stream.readBoolean();
			defaultOutput = stream.readBoolean();

			interpolator.setTarget(switchState ? 1f : 0f);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		if(boot) {
			interpolator.setValueFromTarget();
		}
		boot = false;
	}
	
	public void clientToogleDefaultOutput() {
        clientSend(ElectricalRelayElement.toogleOutputDefaultId);
	}
	
	@Override
	public GuiScreen newGuiDraw(Direction side, EntityPlayer player) {
		return new ElectricalRelayGui(player,this);
	}
	
	@Override
	public CableRenderDescriptor getCableRender(LRDU lrdu) {
		if(lrdu == front) return Eln.instance.signalCableDescriptor.render;
		if(lrdu == front.left() || lrdu == front.right()) return descriptor.cable.render;
		return null;
	}
}
