package mods.eln.sixnode.energymeter;

import java.text.NumberFormat;
import java.text.ParseException;

import mods.eln.gui.GuiButtonEln;
import mods.eln.gui.GuiContainerEln;
import mods.eln.gui.GuiHelperContainer;
import mods.eln.gui.GuiTextFieldEln;
import mods.eln.gui.HelperStdContainer;
import mods.eln.gui.IGuiObject;
import mods.eln.misc.Utils;
import mods.eln.sixnode.energymeter.EnergyMeterElement.Mod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class EnergyMeterGui extends GuiContainerEln {

	public EnergyMeterGui(EntityPlayer player, IInventory inventory, EnergyMeterRender render) {
		super(new EnergyMeterContainer(player, inventory));
		this.render = render;
	}

	GuiButtonEln stateBt, passwordBt, modBt, setEnergyBt, resetTimeBt, energyUnitBt, timeUnitBt;
	GuiTextFieldEln passwordFeild, energyFeild;
	EnergyMeterRender render;

	enum SelectedType {
		none, min, max
	};

	@Override
	public void initGui() {
		super.initGui();
		int x = 6, y = 6;

		isLogged = render.password.equals("");
		passwordFeild = newGuiTextField(x, y + 4, 70);
		x += 74;
		passwordBt = newGuiButton(x, y, 106, "");
		passwordFeild.setComment(0, "Enter password");

		x = 6;
		y += 28;
		x = 6;
		stateBt = newGuiButton(x, y, 70, "");
		x += 74;

		modBt = newGuiButton(x, y, 106, "");
		y += 22;
		x = 6;
		energyFeild = newGuiTextField(x, y + 4, 70);
		x += 74;
		setEnergyBt = newGuiButton(x, y, 106, "Set energy counter");
		energyFeild.setComment(0, "Enter new energy value");
		energyFeild.setComment(1, "In KJ");
		energyFeild.setText("0");

		y += 22;
		x = 6;
		energyUnitBt = newGuiButton(x, y, 34, "");
		x += 34 + 2;
		timeUnitBt = newGuiButton(x, y, 34, "");
		x += 34 + 4;
		resetTimeBt = newGuiButton(x, y, 106, "Reset time counter");
		y += 22;
		x = 6;
		
		
		if(render.descriptor.timeNumberWheel.length == 0){
			energyUnitBt.enabled = false;
			timeUnitBt.enabled = false;
		}
	}

	@Override
	public void guiObjectEvent(IGuiObject object) {
		super.guiObjectEvent(object);

		if (object == stateBt) {
			render.clientSend(EnergyMeterElement.clientToggleStateId);
		}
		if (object == passwordBt) {
			if (isLogged) {
				render.clientSetString(EnergyMeterElement.clientPasswordId, passwordFeild.getText());
			} else {
				if (passwordFeild.getText().equals(render.password)) {
					isLogged = true;
				}
			}
		}

		if (object == modBt) {
			switch (render.mod) {
			case ModCounter:
				render.clientSetString(EnergyMeterElement.clientModId, Mod.ModPrepay.name());
				break;
			case ModPrepay:
				render.clientSetString(EnergyMeterElement.clientModId, Mod.ModCounter.name());
				break;
			}
		}

		if (object == setEnergyBt) {
			double newVoltage;
			try {
				newVoltage = NumberFormat.getInstance().parse(energyFeild.getText()).doubleValue();
			} catch (ParseException e) {
				return;
			}

			render.clientSetDouble(EnergyMeterElement.clientEnergyStackId, newVoltage * 1000);
		}

		if (object == resetTimeBt) {
			render.clientSend(EnergyMeterElement.clientTimeCounterId);
		}
		if (object == energyUnitBt) {
			render.clientSend(EnergyMeterElement.clientEnergyUnitId);
		}
		if (object == timeUnitBt) {
			render.clientSend(EnergyMeterElement.clientTimeUnitId);
		}
	}

	boolean isLogged;

	@Override
	protected void preDraw(float f, int x, int y) {
		super.preDraw(f, x, y);
		if (!render.switchState)
			stateBt.displayString = "is OFF";
		else
			stateBt.displayString = "is ON";

		if (isLogged)
			passwordBt.displayString = "Change password";
		else
			passwordBt.displayString = "Try password";

		switch (render.mod) {
		case ModCounter:
			modBt.displayString = "In counter mod";

			modBt.clearComment();
			modBt.setComment(0, "In this mod, it");
			modBt.setComment(1, "count energy from");
			modBt.setComment(2, "\u00a74red\u00a7f to \u00a71blue\u00a7f side");
			break;
		case ModPrepay:
			modBt.displayString = "In prepay mod";

			modBt.clearComment();
			modBt.setComment(0, "In this mod, it");
			modBt.setComment(1, "deduct energy from");
			modBt.setComment(2, "\u00a74red\u00a7f to \u00a71blue\u00a7f side.");
			modBt.setComment(3, "You must set a initial");
			modBt.setComment(4, "amout of energy and when");
			modBt.setComment(5, "the counter hit zero");
			modBt.setComment(6, "it cut off the line.");

			break;
		}

		if(energyUnitBt != null)
		switch (render.energyUnit) {
		case 0:
			energyUnitBt.displayString = "J";
			break;
		case 1:
			energyUnitBt.displayString = "KJ";
			break;
		case 2:
			energyUnitBt.displayString = "MJ";
			break;
		case 3:
			energyUnitBt.displayString = "GJ";
			break;
		default:
			energyUnitBt.displayString = "??";
			break;
		}
		
		if(timeUnitBt != null)
		switch (render.timeUnit) {
		case 0:
			timeUnitBt.displayString = "H";
			break;
		case 1:
			timeUnitBt.displayString = "D";
			break;
		default:
			timeUnitBt.displayString = "??";
			break;
		}		
		modBt.enabled = isLogged;
		stateBt.enabled = isLogged;
		resetTimeBt.enabled = isLogged;
		setEnergyBt.enabled = isLogged;
		energyUnitBt.enabled = isLogged && render.descriptor.timeNumberWheel.length != 0;
		timeUnitBt.enabled = isLogged && render.descriptor.timeNumberWheel.length != 0;
	}

	@Override
	protected void postDraw(float f, int x, int y) {
		// TODO Auto-generated method stub
		super.postDraw(f, x, y);
		helper.drawRect(6, 29, helper.xSize - 6, 29 + 1, 0xff404040);

		y = 101;
		helper.drawRect(6, y, helper.xSize - 6, y + 1, 0xff404040);

		y += 3;
		helper.drawString(6 + 16 / 2, y, 0xff000000, Utils.plotEnergy("Energy counter :", (int) (render.energyStack)));
		y += 10;
		helper.drawString(6 + 16 / 2, y, 0xff000000, Utils.plotTime("Time counter :", (int) (render.timerCouter)));
	}

	@Override
	protected GuiHelperContainer newHelper() {
		return new GuiHelperContainer(this, 176 + 16, 42 + 166, 8 + 16 / 2, 42 + 84);
	}
}
