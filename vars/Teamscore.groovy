import groovy.json.*

def call(jsondata,github,jenkins,sonar){
def jsonString = jsondata
def jsonObj = readJSON text: jsonString
int ecount = jsonObj.riglet_info.auth_users.size()
	def team=jsonObj.riglet_info.name
List<String> jsonStringa= new ArrayList<String>();
  jsonStringa.add(github)
   jsonStringa.add(jenkins)
	jsonStringa.add(sonar)
   List<String> LIST = new ArrayList<String>();
   for(i=0;i<jsonStringa.size();i++)
  { 
    int score=0
    String name="  "
	  String metric=" "
if(jsonStringa[i].contains("GITHUB"))
    {
    name="github"
    def jsonObj1 = readJSON text: jsonStringa[i]
  def count =jsonObj1.GITHUB.commits_count
  LIST.add(["toolName":name,"metricName":"commits","value":count])
   }
   if(jsonStringa[i].contains("JENKINS"))
    {
      name="jenkins"
      def jsonObjb = readJSON text: jsonStringa[i]
	   // print jsonObjb
      def total=jsonObjb.JENKINS.teambuild_cnt
  def scnt =jsonObjb.JENKINS.teamsuccessbuild_cnt
	    def fcnt=jsonObjb.JENKINS.teamfailurebuild_cnt
	    LIST.add(["toolName":name,"metricName":"total_builds","value":total])
	    LIST.add(["toolName":name,"metricName":"successful_builds","value":scnt])
	    LIST.add(["toolName":name,"metricName":"failure_builds","value":fcnt])
      }
      if(jsonStringa[i].contains("Sonar"))
    {
	    name="sonar"
	    def jsonObjc = readJSON text: jsonStringa[i]
	    //print jsonObjc
	    for(i=0;i<jsonObjc.Sonar.Metrics.component.measures.size();i++){
		    //print jsonObjc.Sonar.Metrics.component.measures
    def sonar_metric=jsonObjc.Sonar.Metrics.component.measures[i].metric
		    def d=jsonObjc.Sonar.Metrics.component.measures[i].value
    double data = Double.parseDouble(d); 
       LIST.add(["toolName":name,"metricName":sonar_metric,"value":data])
	    }
    }
     }
def jsonBuilder = new groovy.json.JsonBuilder()

jsonBuilder(
 "teamName":team,
  "metrics" : LIST
  
) 
  
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/Teamscore.json")
file.write(jsonBuilder.toPrettyString())	
}
sh 'curl -H "Content-Type: application/json" -X POST --data @/var/lib/jenkins/workspace/collector/Teamscore.json http://ec2-13-232-248-254.ap-south-1.compute.amazonaws.com:3000/api/metrics/teams/add'
echo "data"
/*""" curl --location --request POST 'http://ec2-13-232-248-254.ap-south-1.compute.amazonaws.com:3000/api/metrics/teams/add' \
--header 'Content-Type: application/json' \
--data-raw '{
    "teamName": "Avengers",
    "metrics": [
        {
            "toolName": "github",
            "metricName": "commits",
            "value": 30
        },
        {
            "toolName": "jenkins",
            "metricName": "total_builds",
            "value": 24
        },
        {
            "toolName": "jenkins",
            "metricName": "successful_builds",
            "value": 3
        },
        {
            "toolName": "jenkins",
            "metricName": "failure_builds",
            "value": 19
        },
        {
            "toolName": "sonar",
            "metricName": "violations",
            "value": 2.0
        },
        {
            "toolName": "sonar",
            "metricName": "complexity",
            "value": 2.0
        },
        {
            "toolName": "sonar",
            "metricName": "sqale_index",
            "value": 12.0
        },
        {
            "toolName": "sonar",
            "metricName": "bugs",
            "value": 0.0
        },
        {
            "toolName": "sonar",
            "metricName": "vulnerabilities",
            "value": 0.0
        },
        {
            "toolName": "sonar",
            "metricName": "coverage",
            "value": 0.0
        },
        {
            "toolName": "sonar",
            "metricName": "duplicated_lines",
            "value": 0.0
        }
    ]
}'"""*/
