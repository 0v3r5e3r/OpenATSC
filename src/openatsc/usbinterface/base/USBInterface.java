package openatsc.usbinterface.base;

import java.nio.ByteBuffer;

import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class USBInterface {

	private static Context m_context;
	private static short target_pid = Short.parseShort("692f",16);
	
	public USBInterface()
	{
		if(m_context != null)
			return;
		
		m_context = new Context();
		int result = LibUsb.init(m_context);
		if (result != LibUsb.SUCCESS)
		{
			System.err.println("LibUsb Failed to Initialize.\r\nQuitting.\r\n");
			System.exit(1);
		}
	}
	
	public static void scanDevicesForTuner() throws LibUsbException
	{
		DeviceList list = new DeviceList();
		int result = LibUsb.getDeviceList(m_context, list);
		if(result < 0)
			throw new LibUsbException("Unable to get device list", result);
		
		try {
			for(Device device:list)
			{
				DeviceDescriptor descriptor = new DeviceDescriptor();
				result = LibUsb.getDeviceDescriptor(device, descriptor);
				if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read device description", result);
				
				short vendor_id = descriptor.idVendor();
				short product_id = descriptor.idProduct();
				
				if(product_id != target_pid)
					continue;
				
				System.out.println("==DEVICE==\r\nvendor: " + String.format("0x%08X", vendor_id) + "\r\nproduct: " + String.format("0x%08X", product_id));
				
				DeviceHandle handle = new DeviceHandle();
				int ress = LibUsb.open(device, handle);
				if(ress == LibUsb.SUCCESS) {
					String manufacturerString = getUsbString(handle,0);
					String productString = getUsbString(handle,1);
					String serialNumberString = getUsbString(handle,2);
					LibUsb.close(handle);
					
					if(manufacturerString.contains("Gen")) {
						TVDeviceUSB test = new TVDeviceUSB(0, productString, device);
						test.createUsbConnection();
					}
				}
			}
		}finally
		{
			LibUsb.freeDeviceList(list, true);
		}
	}
	
	public static String getUsbString(DeviceHandle handle, int stringID) {
		String string = "";
		ByteBuffer buf = ByteBuffer.allocateDirect(128);
		int bytesRead = LibUsb.controlTransfer(handle, (byte)128, (byte)6, (short)256, (short)0, buf, (long)1000);
		if(bytesRead == 18) {
			int index = 0;
			if (stringID == 0)
				index = buf.get(14);
			else if(stringID == 1)
				index = buf.get(15);
			else if(stringID == 2)
				index = buf.get(16);
			if(index > 0) {
				int bytesRead2 = LibUsb.controlTransfer(handle, (byte)128, (byte)6, (short)768, (short)0, buf, (long)1000);
				if(bytesRead2 > 3)
				{
					int language = (buf.get(2) << 8) + (buf.get(3) & 255);
					int bytesRead3 = LibUsb.controlTransfer(handle, (byte)128, (byte)6, (short)(index | 768), (short)language, buf, (long)1000);
					if(bytesRead3 > 2) {
						for(int i = 2; i < bytesRead3 && buf.get(i) != 0; i += 2)
							string = String.valueOf(string) + ((char) buf.get(i));
					}else {
						System.out.println("Cannot read string descriptor");
					}
				}else {
					System.out.println("Cannot read string descriptor for getting language");
				}
			}else {
				System.out.println("Invalid stringID");
			}
		}else {
			System.out.println("Cannot read device descriptor");
		}
		
		return string;
	}
	
	public static Context getGlobalContext() {
		return m_context;
	}
}
