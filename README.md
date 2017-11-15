tycho-rules
======================

# Project Description
A [Maven][1]/[Tycho][2] project to add some validation rules for [OSGI][3] development.
Most of the rules are pretty simple and are used to maintain coherence in a multi-plugin project.

#Status
Codeship [ ![Codeship Status for bmaggi/tycho-rules](https://codeship.com/projects/df4dead0-12a2-0134-4498-76fd620179ca/status?branch=master)](https://codeship.com/projects/157406)

License [![License](https://img.shields.io/badge/license-EPL-blue.svg)](https://www.eclipse.org/legal/epl-v10.html)

# How to build

This project is built using Maven.
To build locally, simply execute the command line:

```
mvn clean install
```

You can also chose the it test with this command 

```
mvn invoker:run -Dinvoker.test=checkexportpackage,checkexportpackage.failing
```

# How to use

Configuration to add in the pom.xml of your project:
```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-enforcer-plugin</artifactId>
			<version>${enforcer.api.version}</version>
			<dependencies>
				<dependency>
					<groupId>com.github.bmaggi.checks</groupId>
					<artifactId>tycho-rules</artifactId>
					<version>0.1.0-SNAPSHOT</version>
				</dependency>
			</dependencies>
			<executions>
				<execution>
					<id>custom-enforce</id>
					<phase>validate</phase>
					<goals>
						<goal>enforce</goal>
					</goals>
					<configuration>
						<rules>
						<!-- define your rules here see Rules.md -->
						</rules>
					</configuration>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```  

# How to make a release
## Check that you are using latest version
```  
mvn versions:display-dependency-updates
mvn versions:display-plugin-updates
```  

## To release on maven central.
```  
mvn release:clean release:prepare 
```  
follow by
```  
mvn release:perform
```  

[1]:https://maven.apache.org/
[2]:https://eclipse.org/tycho/
[3]:http://www.osgi.org/
