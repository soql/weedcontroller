package pl.net.oth.weedcontroller.helpers;

import java.util.HashMap;
import java.util.Map;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class PinHelper {
	private Map<Integer, Pin> pinHelpers=new HashMap<Integer, Pin>();
	private static PinHelper instance=null;
	
	private PinHelper(){		
		pinHelpers.put(0, RaspiPin.GPIO_00);
		pinHelpers.put(1, RaspiPin.GPIO_01);
		pinHelpers.put(2, RaspiPin.GPIO_02);
		pinHelpers.put(3, RaspiPin.GPIO_03);
		pinHelpers.put(4, RaspiPin.GPIO_04);
		pinHelpers.put(5, RaspiPin.GPIO_05);
		pinHelpers.put(6, RaspiPin.GPIO_06);
		pinHelpers.put(7, RaspiPin.GPIO_07);
		pinHelpers.put(8, RaspiPin.GPIO_08);
		pinHelpers.put(9, RaspiPin.GPIO_09);
		pinHelpers.put(10, RaspiPin.GPIO_10);
		pinHelpers.put(11, RaspiPin.GPIO_11);
		pinHelpers.put(12, RaspiPin.GPIO_12);
		pinHelpers.put(13, RaspiPin.GPIO_13);
		pinHelpers.put(14, RaspiPin.GPIO_14);
		pinHelpers.put(15, RaspiPin.GPIO_15);
		pinHelpers.put(16, RaspiPin.GPIO_16);
		pinHelpers.put(17, RaspiPin.GPIO_17);
		pinHelpers.put(18, RaspiPin.GPIO_18);
		pinHelpers.put(19, RaspiPin.GPIO_19);
		pinHelpers.put(20, RaspiPin.GPIO_20);
		pinHelpers.put(21, RaspiPin.GPIO_21);
		pinHelpers.put(22, RaspiPin.GPIO_22);
		pinHelpers.put(23, RaspiPin.GPIO_23);
		pinHelpers.put(24, RaspiPin.GPIO_24);
		pinHelpers.put(25, RaspiPin.GPIO_25);
		pinHelpers.put(26, RaspiPin.GPIO_26);
		pinHelpers.put(27, RaspiPin.GPIO_27);
		pinHelpers.put(28, RaspiPin.GPIO_28);
		pinHelpers.put(29, RaspiPin.GPIO_29);
		pinHelpers.put(30, RaspiPin.GPIO_30);
		pinHelpers.put(31, RaspiPin.GPIO_31);
		
	}
	public static PinHelper getInstance(){
		if(instance==null){
			instance=new PinHelper();
		}
		return instance;
	}
	public Pin getPin(Integer pin){
		return pinHelpers.get(pin);
	}
}
