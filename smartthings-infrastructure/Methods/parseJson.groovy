/////////////////////////////////////////////////////////////////////
def parseJson(String data) {
	return new groovy.json.JsonSlurper().parseText(data)
}