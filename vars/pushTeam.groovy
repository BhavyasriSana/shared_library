def call(data,IP){
def jsonString = data
def jsonObj = readJSON text: jsonString
	
	sh "curl -X POST --header 'Content-Type: application/json' --data ${jsonObj} ${IP}/api/metrics/teams/add"
  echo "Sending data to Gamification API"
}
