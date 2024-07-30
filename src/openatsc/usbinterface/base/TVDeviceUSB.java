package openatsc.usbinterface.base;

import org.usb4java.ConfigDescriptor;
import org.usb4java.Device;
import org.usb4java.DeviceHandle;
import org.usb4java.EndpointDescriptor;
import org.usb4java.Interface;
import org.usb4java.InterfaceDescriptor;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class TVDeviceUSB extends TVDevice {
	
	protected static final String TAG = "TVDeviceUSB";
	protected DeviceHandle connection;
	protected Device device;
	protected int[] mEndpointBufferSizes;
	protected byte[][] mEndpointBuffers;
	protected EndpointDescriptor[] mEndpoints;
	protected InterfaceDescriptor mInterface;
	
	public TVDeviceUSB(int deviceType, String deviceName, Device device) {
		super(deviceType, 2, deviceName);
		this.device = device;
		this.mEndpointBuffers = new byte[256][];
		this.mEndpointBufferSizes = new int[256];
		this.connection = new DeviceHandle();
		
		mEndpoints = new EndpointDescriptor[256];
	}

	
	protected void createUsbConnection() {
		int result = LibUsb.open(this.device, this.connection);
		if(result == LibUsb.SUCCESS) {
			System.out.println("Managed to open the USB Connection!");
			
			ConfigDescriptor configs = new ConfigDescriptor();
			LibUsb.getActiveConfigDescriptor(device, configs);
			Interface[] ifaces = configs.iface();
			
			mInterface = ifaces[0].altsetting()[0];
			
			int interfaceResult = LibUsb.claimInterface(this.connection, mInterface.bInterfaceNumber());
			if(interfaceResult == LibUsb.SUCCESS)
			{
				//TODO: run the program, you have the endpoint addresses in the configs somehwere
				// You need to extract the endpoint addresses and save them,
				// You also need to extract the interface and save it.
				// Then you can resume working on the full TVDeviceUSB which in the padtv app is called "EyeTVDeviceUSB"
				EndpointDescriptor[] endpoints = mInterface.endpoint();
				for(int i = 0; i < mInterface.bNumEndpoints(); i++) {
					EndpointDescriptor ep = endpoints[i];
					if(ep != null) {
						int address = Byte.toUnsignedInt(ep.bEndpointAddress());
						this.mEndpoints[address] = ep;
					}
				}
			}
		}else {
			System.out.println("Couldn't open the device!");
		}
	}
	
	protected void releaseUsbConnection() {
		if(this.connection != null)
		{
			try {
				if(this.mInterface != null)
				{
					LibUsb.releaseInterface(connection, this.mInterface.bInterfaceNumber());
				}
				LibUsb.close(connection);
			}catch(LibUsbException e) {
				e.printStackTrace();
			}
		}
		this.connection = null;
		this.mInterface = null;
		for(int i = 0; i < 256; i++) {
			this.mEndpoints[i] = null;
		}
	}
	
	private byte[] assureEndpointBuffer(int inEndpoint, int inMinSize) {
		if(inMinSize < 512)
			inMinSize = 512;
		
		if(this.mEndpointBuffers[inEndpoint] == null || this.mEndpointBufferSizes[inEndpoint] < inMinSize) {
			this.mEndpointBuffers[inEndpoint] = new byte[inMinSize];
			this.mEndpointBufferSizes[inEndpoint] = inMinSize;
		}
		return this.mEndpointBuffers[inEndpoint];
	}
	
	
}
