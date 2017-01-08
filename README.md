# openshift-jee-services

<b>Prerequisite:</b> 
apache-maven-3.3.9 or higher

How to build project:
<li>The following method doensn't start unit test, because for default profile there are deactivated:<br>
<b>mvn clean install</b><br/>
</li>
<li>Using "full" profile which also starts Unit Tests:<br>
<b>mvn clean install -P full</b><br/>
</li>

<b>The project uses CDI (Weld implementation) container and was tested and deployed to Wildfly 10 Application Server</b><br>
<b>The unit tests use Mockito as well as JUnit frameworks</b>




