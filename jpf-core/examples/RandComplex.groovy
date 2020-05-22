// This function runs when the SmartApp is installed
def installed(a, b, c) {
	println("a=" + a)
	initialize(a, b, c)
}

// This function is where you initialize callbacks for event listeners
def initialize(a, b, c) {
	println("  b=" + b)
	finalize(a, b, c)
}

def finalize(a, b, c) {
	println("    c=" + c)
	result(a, b, c)
}

def result(a, b, c) {
	int d = a/(b+c-10)
	println("       d=" + d)
}

Random random = new Random(42)
int a = random.nextInt(10)
int b = random.nextInt(10)
int c = random.nextInt(10)
installed(a,b,c)
println "End of call!"