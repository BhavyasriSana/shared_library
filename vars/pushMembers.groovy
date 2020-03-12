def call(IP)
{
	sh "curl -X POST --header 'Content-Type: application/json' --data @/var/lib/jenkins/workspace/${JOB_NAME}/game.json  ${IP}/api/metrics/members/add"
  echo "Sending data to Gamification API"
}
