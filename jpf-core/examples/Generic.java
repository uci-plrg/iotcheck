import java.util.List;
import java.util.ArrayList;

public class Generic<T,E> {
	
	private T t;
	
	public T returnT(List<String> test) {
		return this.t;
	}

	public List<String> returnL(List<String> test) {
		return test;
	}
	
	public static void main(String[] args) {
	
		Generic gen = new Generic();
		List<String> list = new ArrayList<>();
		List<String> ret = gen.returnL(list);
	}
}
