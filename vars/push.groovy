def call()
{
	sh "curl -X POST --header 'Content-Type: application/json' --data @/var/lib/jenkins/workspace/${JOB_NAME}/game.json  http://ec2-13-232-248-254.ap-south-1.compute.amazonaws.com:3000/api/metrics/members/add"
  echo "Sending data to Gamification API"
}
