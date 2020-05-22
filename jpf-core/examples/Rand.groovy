class Rand {
	static void main (String[] args) {
		println("Groovy model-checking")
		Random random = new Random(42);
	
		int a = random.nextInt(10)
		int b = random.nextInt(10)
		println("a=" + a)
		println("  b=" + b)

		int c = a/(b+a -5)
		println("    c=" + c)
	}
}

