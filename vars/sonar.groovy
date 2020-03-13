import groovy.json.*
/*@NonCPS
create(){
def resultJson = readJSON file: 'metrics.json'
  def jsonBuilder = new groovy.json.JsonBuilder()
  jsonBuilder.Sonar(
  "Metrics" : resultJson
  )
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/metrics.json")
  file.write(jsonBuilder.toPrettyString())
  return jsonBuilder
}*/

def call(jsondata,rig){
def jsonString = jsondata
def jsonObj = readJSON text: jsonString

String a = jsonObj.code_quality.projects.project.project_key
String ProjectKey=a.replaceAll("\\[", "").replaceAll("\\]","");
  
  def jsonObja = readJSON text: rig
	def IP=jsonObja.url
	def user=jsonObja.userName
	def pass=jsonObja.password

  sh "curl -u ${user}:${pass} -X GET '${IP}/api/measures/component?component=${ProjectKey}&metricKeys=coverage,vulnerabilities,bugs,violations,complexity,tests,duplicated_lines,sqale_index' -o metrics.json"
  echo 'metrics collected'
	
	def resultJson = readJSON file: 'metrics.json'
  def jsonBuilder = new groovy.json.JsonBuilder()
  jsonBuilder.Sonar(
  "Metrics" : resultJson
  )
  File file = new File("/var/lib/jenkins/workspace/${JOB_NAME}/metrics.json")
  file.write(jsonBuilder.toPrettyString())
  return jsonBuilder

 // create()
}
