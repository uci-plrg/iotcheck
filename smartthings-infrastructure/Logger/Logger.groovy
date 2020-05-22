//Adding a Logger class for log.debug
package Logger

class Logger {
    private boolean printToConsole = true

    def methodMissing(String name, args) {
        def messsage = args[0]
        if (printToConsole) {
            println messsage
        }
    }
}
