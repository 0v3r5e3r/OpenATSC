package openatsc.usbinterface;

public class GenericDevice {

	protected boolean swigCMemOwn;
	private long swigCPtr;
	
	public GenericDevice(long cPtr, boolean cMemoryOwn) {
		this.swigCMemOwn = cMemoryOwn;
		this.swigCPtr = cPtr;
	}
	
	public static long getCPtr(GenericDevice obj)
	{
		if(obj == null)
			return 0L;
		return obj.swigCPtr;
	}
	
	protected void finalize() {
		delete();
	}
	
	public synchronized void delete() {
		if(this.swigCPtr != 0)
		{
			if(this.swigCMemOwn) {
				this.swigCMemOwn = false;
				
			}
			this.swigCPtr = 0L;
		}
	}
	
}
