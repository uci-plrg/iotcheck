/////////////////////////////////////////////////////////////////////
////sendNotificationToContacts(text, recipients)
def sendNotificationToContacts(String text, String recipients) {
	for (int i = 0;i < recipients.size();i++) {
		for (int j = 0;j < location.contacts.size();j++) {
			if (recipients[i] == location.contacts[j]) {
				println("Sending \""+text+"\" to "+location.phoneNumbers[j].toString())
			}
		}
	}
}

def sendNotificationToContacts(String text, String recipients, LinkedHashMap metaData) {
	for (int i = 0;i < recipients.size();i++) {
		for (int j = 0;j < location.contacts.size();j++) {
			if (recipients[i] == location.contacts[j]) {
				println("Sending \""+text+"\" to "+location.phoneNumbers[j].toString())
			}
		}
	}
}
