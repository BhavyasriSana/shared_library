def call(IP)
{
	sh "curl -X POST --header 'Content-Type: application/json' --data @/var/lib/jenkins/workspace/${JOB_NAME}/Teamscore.json  ${IP}/api/metrics/teams/add"
  echo "Sending data to Gamification API"
}
