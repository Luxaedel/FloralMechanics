package floralmechanics.util.patterns;

public class FloralGeneratorPattern extends Pattern {
	public int rfTick = 100;

	public FloralGeneratorPattern(int rfPerTick) {
		super();
		this.rfTick = rfPerTick;
	}
	
	public void setRFTick(int rfTick) {
		this.rfTick = rfTick;
	}
}
