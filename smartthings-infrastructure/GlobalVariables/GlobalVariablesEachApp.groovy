//Global variable for state[mode]
def state = [home:[],away:[],night:[]]
//Create a global logger object for methods
def log = new Logger()
//Create a global variable for Functions in Subscribe method
def functionList = new ArrayList(20)
//Create a global variable for Objects in Subscribe method
def objectList = new ArrayList(20)
//Create a global variable for Events in Subscribe method
def eventList = new ArrayList(20)
def valueList = new ArrayList(20)
//Create a global variable for settings
def settings
//Zip code
def zipCode = 92617
//atomicState variable
def atomicState = [version: "1.01"]
