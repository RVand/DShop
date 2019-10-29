public class NodeCus {
	public int service;
	public int wait;
	
	public NodeCus(int rand) {
		wait = 0;
		service = rand;
	}
	public int getService() {
		return service;
	}
	public void setService(int serviceTime) {
		this.service = serviceTime;
	}
	public int getWait() {
		return wait;
	}
	public void setWait(int waitTime) {
		this.wait = waitTime;
	}
}